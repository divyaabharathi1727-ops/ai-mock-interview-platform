package com.mockinterview.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockinterview.backend.entity.InterviewQuestionType;
import com.mockinterview.backend.exception.AIServiceException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService implements AIService {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final String apiKey;
    private final String model;
    private final String apiUrl;
    private final Duration timeout;

    public OpenAIService(ObjectMapper objectMapper,
                         @Value("${ai.api-key:}") String apiKey,
                         @Value("${ai.model:gpt-4o-mini}") String model,
                         @Value("${ai.api-url:https://api.openai.com/v1/chat/completions}") String apiUrl,
                         @Value("${ai.timeout-ms:20000}") long timeoutMs) {
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.model = model;
        this.apiUrl = apiUrl;
        this.timeout = Duration.ofMillis(timeoutMs);
        this.httpClient = HttpClient.newBuilder().connectTimeout(timeout).build();
    }

    @Override
    public List<GeneratedQuestion> generateQuestions(String jobRole, String interviewType,
                                                      String difficulty, int questionCount) {
        JsonNode response = call("Generate exactly " + questionCount + " interview questions. "
                + "Return JSON only with a questions array. Each item must have questionText, "
                + "questionType (TEXT, MCQ, MULTIPLE_SELECT, TRUE_FALSE), options, correctAnswer, "
                + "and correctAnswers. Mix types; match role " + jobRole + ", type " + interviewType
                + ", difficulty " + difficulty + ". Avoid duplicates.");
        JsonNode questions = response.path("questions");
        if (!questions.isArray()) throw new AIServiceException("AI returned invalid question data");
        List<GeneratedQuestion> result = new ArrayList<>();
        for (JsonNode item : questions) {
            try {
                InterviewQuestionType type = InterviewQuestionType.valueOf(item.path("questionType").asText());
                List<String> options = textList(item.path("options"));
                List<String> correctAnswers = textList(item.path("correctAnswers"));
                String correctAnswer = item.path("correctAnswer").isMissingNode()
                        ? null : item.path("correctAnswer").asText(null);
                validateQuestion(item.path("questionText").asText(), type, options, correctAnswer, correctAnswers);
                result.add(new GeneratedQuestion(item.path("questionText").asText(), type,
                        options, correctAnswer, correctAnswers));
            } catch (IllegalArgumentException exception) {
                throw new AIServiceException("AI returned an unsupported question", exception);
            }
        }
        return result;
    }

    @Override
    public TextEvaluation evaluateTextAnswer(String jobRole, String interviewType, String difficulty,
                                             String question, String answer) {
        JsonNode response = call("Evaluate this interview answer and return JSON only with numeric "
                + "overallScore, technicalScore, relevanceScore, clarityScore, completenessScore "
                + "from 0 to 10, plus feedback, strengths array, improvements array. Role: " + jobRole
                + ". Interview type: " + interviewType + ". Difficulty: " + difficulty
                + ". Question: " + question + ". Candidate answer: " + answer);
        return new TextEvaluation(response.path("overallScore").asDouble(),
            response.path("technicalScore").asDouble(), response.path("relevanceScore").asDouble(),
            response.path("clarityScore").asDouble(), response.path("completenessScore").asDouble(),
            response.path("feedback").asText("Evaluation unavailable"), textList(response.path("strengths")),
            textList(response.path("improvements")));
    }

    private JsonNode call(String prompt) {
        if (apiKey == null || apiKey.isBlank()) throw new AIServiceException("AI provider is not configured");
        try {
            String requestBody = objectMapper.writeValueAsString(java.util.Map.of(
                    "model", model,
                    "temperature", 0.3,
                    "response_format", java.util.Map.of("type", "json_object"),
                    "messages", List.of(java.util.Map.of("role", "user", "content", prompt))));
            HttpRequest request = HttpRequest.newBuilder(URI.create(apiUrl)).timeout(timeout)
                    .header("Authorization", "Bearer " + apiKey).header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) throw new AIServiceException("AI provider request failed");
            String content = objectMapper.readTree(response.body()).path("choices").path(0)
                    .path("message").path("content").asText();
            return objectMapper.readTree(content);
        } catch (AIServiceException exception) {
            throw exception;
        } catch (IOException | InterruptedException | IllegalArgumentException exception) {
            throw new AIServiceException("AI provider is unavailable", exception);
        }
    }

    private List<String> textList(JsonNode node) {
        List<String> values = new ArrayList<>();
        if (node != null && node.isArray()) node.forEach(value -> values.add(value.asText()));
        return values;
    }

    private void validateQuestion(String text, InterviewQuestionType type, List<String> options,
                                  String correctAnswer, List<String> correctAnswers) {
        if (text == null || text.isBlank()) throw new AIServiceException("AI returned an empty question");
        if (type == InterviewQuestionType.MCQ && (options.size() < 2 || correctAnswer == null))
            throw new AIServiceException("AI returned invalid MCQ data");
        if (type == InterviewQuestionType.MULTIPLE_SELECT && (options.size() < 2 || correctAnswers.isEmpty()))
            throw new AIServiceException("AI returned invalid multiple-select data");
        if (type == InterviewQuestionType.TRUE_FALSE && (options.size() != 2 || correctAnswer == null))
            throw new AIServiceException("AI returned invalid true/false data");
    }
}
