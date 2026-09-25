package com.mockinterview.backend.service;

import com.mockinterview.backend.dto.AnswerRequest;
import com.mockinterview.backend.dto.AnswerResponse;
import com.mockinterview.backend.dto.InterviewCompletionResponse;
import com.mockinterview.backend.dto.InterviewSessionResponse;
import com.mockinterview.backend.dto.QuestionResponse;
import com.mockinterview.backend.dto.EvaluationResponse;
import com.mockinterview.backend.dto.InterviewResultResponse;
import com.mockinterview.backend.dto.QuestionResultResponse;
import com.mockinterview.backend.dto.InterviewAnalyticsResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mockinterview.backend.config.InterviewProperties;
import com.mockinterview.backend.entity.AnswerEvaluation;
import com.mockinterview.backend.entity.InterviewQuestionType;
import com.mockinterview.backend.exception.AIServiceException;
import com.mockinterview.backend.entity.Interview;
import com.mockinterview.backend.entity.InterviewAnswer;
import com.mockinterview.backend.entity.InterviewQuestion;
import com.mockinterview.backend.entity.InterviewStatus;
import com.mockinterview.backend.exception.InterviewNotFoundException;
import com.mockinterview.backend.repository.InterviewAnswerRepository;
import com.mockinterview.backend.repository.InterviewQuestionRepository;
import com.mockinterview.backend.repository.InterviewRepository;
import com.mockinterview.backend.repository.AnswerEvaluationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterviewQuestionService {

    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewAnswerRepository answerRepository;
    private final QuestionBankService questionBankService;
    private final AIService aiService;
    private final AnswerEvaluationRepository evaluationRepository;
    private final InterviewProperties properties;
    private final ObjectMapper objectMapper;

    @Autowired
    public InterviewQuestionService(InterviewRepository interviewRepository,
                                    InterviewQuestionRepository questionRepository,
                                    InterviewAnswerRepository answerRepository,
                                    QuestionBankService questionBankService,
                                    AIService aiService,
                                    AnswerEvaluationRepository evaluationRepository,
                                    InterviewProperties properties,
                                    ObjectMapper objectMapper) {
        this.interviewRepository = interviewRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.questionBankService = questionBankService;
        this.aiService = aiService;
        this.evaluationRepository = evaluationRepository;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    public InterviewQuestionService(InterviewRepository interviewRepository,
                                    InterviewQuestionRepository questionRepository,
                                    InterviewAnswerRepository answerRepository,
                                    QuestionBankService questionBankService) {
        this(interviewRepository, questionRepository, answerRepository, questionBankService,
                null, null, new InterviewProperties(), new ObjectMapper());
    }

    @Transactional
    public InterviewSessionResponse startInterview(String email, Long interviewId) {
        Interview interview = findOwnedInterview(email, interviewId);
        if (interview.getStatus() == InterviewStatus.COMPLETED) {
            throw new IllegalArgumentException("Completed interviews cannot be started again");
        }
        if (interview.getStatus() == InterviewStatus.CANCELLED) {
            throw new IllegalArgumentException("Cancelled interviews cannot be started");
        }

        List<InterviewQuestion> questions = questionRepository
                .findByInterviewIdOrderByQuestionOrder(interviewId);
        if (questions.isEmpty()) {
            questions = generateInitialQuestions(interview);
        }
        if (interview.getStatus() == InterviewStatus.CREATED) {
            interview.setStatus(InterviewStatus.IN_PROGRESS);
            interview.setStartedAt(LocalDateTime.now());
            interviewRepository.save(interview);
        }
        return buildSession(questions);
    }

    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestions(String email, Long interviewId) {
        findOwnedInterview(email, interviewId);
        return questionRepository.findByInterviewIdOrderByQuestionOrder(interviewId)
                .stream().map(this::toQuestionResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InterviewSessionResponse getCurrentQuestion(String email, Long interviewId) {
        Interview interview = findOwnedInterview(email, interviewId);
        ensureStarted(interview);
        return buildSession(questionRepository.findByInterviewIdOrderByQuestionOrder(interviewId));
    }

    @Transactional
    public AnswerResponse submitAnswer(String email, Long interviewId, Long questionId,
                                       AnswerRequest request) {
        Interview interview = findOwnedInterview(email, interviewId);
        ensureInProgress(interview);
        InterviewQuestion question = questionRepository.findByIdAndInterviewId(questionId, interviewId)
                .orElseThrow(() -> new InterviewNotFoundException("Question not found"));
        if (answerRepository.existsByQuestionId(questionId)) {
            throw new IllegalArgumentException("This question has already been answered");
        }
        String storedAnswer = answerValue(question, request);
        InterviewAnswer answer = new InterviewAnswer();
        answer.setQuestion(question);
        answer.setAnswerText(storedAnswer);
        answer.setAnsweredAt(LocalDateTime.now());
        InterviewAnswer persistedAnswer = answerRepository.save(answer);
        if (persistedAnswer != null) answer = persistedAnswer;
        saveEvaluation(interview, question, answer);
        return toAnswerResponse(interviewId);
    }

    @Transactional
    public InterviewCompletionResponse finishInterview(String email, Long interviewId) {
        Interview interview = findOwnedInterview(email, interviewId);
        if (interview.getStatus() == InterviewStatus.COMPLETED) {
            throw new IllegalArgumentException("Interview is already completed");
        }
        ensureInProgress(interview);
        int totalQuestions = questionRepository.findByInterviewIdOrderByQuestionOrder(interviewId).size();
        int answeredQuestions = Math.toIntExact(answerRepository.countByQuestionInterviewId(interviewId));
        interview.setStatus(InterviewStatus.COMPLETED);
        interview.setCompletedAt(LocalDateTime.now());
        if (interview.getStartedAt() != null) {
            interview.setDurationSeconds(java.time.Duration.between(
                interview.getStartedAt(), interview.getCompletedAt()).getSeconds());
        }
        interviewRepository.save(interview);
        return new InterviewCompletionResponse(
            interview.getStatus(), totalQuestions, answeredQuestions, interview.getCompletedAt(),
            interview.getDurationSeconds());
    }

    @Transactional(readOnly = true)
    public InterviewResultResponse getResults(String email, Long interviewId) {
        Interview interview = findOwnedInterview(email, interviewId);
        List<InterviewQuestion> questions = questionRepository.findByInterviewIdOrderByQuestionOrder(interviewId);
        List<InterviewAnswer> answers = answerRepository.findByQuestionInterviewIdOrderByAnsweredAtAsc(interviewId);
        List<QuestionResultResponse> results = questions.stream().map(question -> {
            InterviewAnswer answer = answers.stream().filter(item -> item.getQuestion().getId().equals(question.getId())).findFirst().orElse(null);
            AnswerEvaluation evaluation = answer == null ? null : evaluationRepository.findByAnswerId(answer.getId()).orElse(null);
            return new QuestionResultResponse(question.getId(), question.getQuestionText(), question.getQuestionType(),
                    readList(question.getOptionsJson()), answer == null ? null : answer.getAnswerText(), toEvaluationResponse(evaluation));
        }).toList();
        List<AnswerEvaluation> evaluations = answers.stream().map(answer -> evaluationRepository.findByAnswerId(answer.getId()).orElse(null)).filter(java.util.Objects::nonNull).filter(AnswerEvaluation::isAvailable).toList();
        int correct = (int) evaluations.stream().filter(item -> Boolean.TRUE.equals(item.getCorrect())).count();
        int incorrect = (int) evaluations.stream().filter(item -> Boolean.FALSE.equals(item.getCorrect())).count();
        int unanswered = questions.size() - answers.size();
        InterviewResultResponse response = new InterviewResultResponse(interview.getId(), interview.getJobRole(), interview.getInterviewType(), interview.getDifficulty(), interview.getStatus(), questions.size(), answers.size(), interview.getCompletedAt(), average(evaluations, AnswerEvaluation::getOverallScore), average(evaluations, AnswerEvaluation::getTechnicalScore), average(evaluations, AnswerEvaluation::getRelevanceScore), average(evaluations, AnswerEvaluation::getClarityScore), average(evaluations, AnswerEvaluation::getCompletenessScore), results);
        response.setSessionMetrics(interview.getDurationSeconds(), correct, incorrect, unanswered);
        for (int index = 0; index < questions.size(); index++) {
            InterviewQuestion question = questions.get(index);
            if (question.getQuestionType() == InterviewQuestionType.MCQ || question.getQuestionType() == InterviewQuestionType.TRUE_FALSE) {
                results.get(index).setCorrectAnswer(question.getCorrectAnswer());
            } else if (question.getQuestionType() == InterviewQuestionType.MULTIPLE_SELECT) {
                results.get(index).setCorrectAnswer(String.join(", ", readList(question.getCorrectAnswersJson())));
            }
        }
        return response;
    }

    @Transactional(readOnly = true)
    public InterviewAnalyticsResponse getAnalytics(String email) {
        List<Interview> interviews = interviewRepository.findByUserEmailOrderByCreatedAtDesc(normalizeEmail(email));
        List<InterviewResultResponse> completed = interviews.stream()
                .filter(item -> item.getStatus() == InterviewStatus.COMPLETED)
                .map(item -> getResults(email, item.getId())).toList();
        List<Double> scores = completed.stream().map(InterviewResultResponse::getOverallScore)
                .filter(java.util.Objects::nonNull).toList();
        Long averageDuration = completed.stream().map(InterviewResultResponse::getDurationSeconds)
                .filter(java.util.Objects::nonNull).mapToLong(Long::longValue).average().isPresent()
                ? Math.round(completed.stream().map(InterviewResultResponse::getDurationSeconds).filter(java.util.Objects::nonNull).mapToLong(Long::longValue).average().orElse(0)) : null;
        List<InterviewAnalyticsResponse.AnalyticsPoint> trend = completed.reversed().stream()
                .map(item -> new InterviewAnalyticsResponse.AnalyticsPoint(item.getJobRole(), item.getOverallScore(), item.getDurationSeconds())).toList();
        List<String> recommendations = new java.util.ArrayList<>();
        if (completed.stream().anyMatch(item -> item.getTechnicalScore() != null && item.getTechnicalScore() < 6)) recommendations.add("Review core technical concepts and explain them with practical examples.");
        if (completed.stream().anyMatch(item -> item.getClarityScore() != null && item.getClarityScore() < 6)) recommendations.add("Practice concise, structured explanations before adding detail.");
        if (recommendations.isEmpty() && !completed.isEmpty()) recommendations.add("Keep practicing mixed technical and scenario questions to maintain your progress.");
        return new InterviewAnalyticsResponse(interviews.size(), completed.size(), interviews.size() - completed.size(), scores.isEmpty() ? null : scores.stream().mapToDouble(Double::doubleValue).average().orElse(0), scores.isEmpty() ? null : scores.stream().mapToDouble(Double::doubleValue).max().orElse(0), averageDuration, completed.stream().mapToInt(InterviewResultResponse::getTotalQuestions).sum(), completed.stream().mapToInt(InterviewResultResponse::getAnsweredQuestions).sum(), completed.stream().mapToInt(InterviewResultResponse::getCorrectObjectiveAnswers).sum(), completed.stream().mapToInt(InterviewResultResponse::getIncorrectObjectiveAnswers).sum(), trend, List.of("Use your highest-scoring question types as a model for future answers."), recommendations);
    }

        @Transactional(readOnly = true)
        public EvaluationResponse getEvaluation(String email, Long interviewId, Long answerId) {
        findOwnedInterview(email, interviewId);
        InterviewAnswer answer = answerRepository.findById(answerId)
            .filter(item -> item.getQuestion().getInterview().getId().equals(interviewId))
            .orElseThrow(() -> new InterviewNotFoundException("Answer not found"));
        AnswerEvaluation evaluation = evaluationRepository.findByAnswerId(answer.getId())
            .orElseThrow(() -> new InterviewNotFoundException("Evaluation not found"));
        return toEvaluationResponse(evaluation);
        }

    private List<InterviewQuestion> generateInitialQuestions(Interview interview) {
        int count = properties.getDefaultQuestionCount();
        List<AIService.GeneratedQuestion> generated;
        try {
            generated = aiService == null ? List.of() : aiService.generateQuestions(interview.getJobRole(), interview.getInterviewType(), interview.getDifficulty().name(), count);
        } catch (AIServiceException exception) {
            generated = List.of();
        }
        List<AIService.GeneratedQuestion> fallback = questionBankService.getFallbackQuestions(interview.getJobRole(), interview.getInterviewType(), interview.getDifficulty().name(), count);
        List<AIService.GeneratedQuestion> selected = new java.util.ArrayList<>();
        java.util.Set<String> seen = new java.util.HashSet<>();
        for (AIService.GeneratedQuestion item : generated) if (seen.add(item.questionText().trim().toLowerCase(Locale.ROOT)) && selected.size() < count) selected.add(item);
        for (AIService.GeneratedQuestion item : fallback) if (seen.add(item.questionText().trim().toLowerCase(Locale.ROOT)) && selected.size() < count) selected.add(item);
        List<InterviewQuestion> questions = selected.stream().map(item -> {
                    InterviewQuestion question = new InterviewQuestion();
                    question.setInterview(interview);
                    question.setQuestionText(item.questionText());
                    question.setQuestionType(item.questionType());
                    question.setOptionsJson(writeList(item.options()));
                    question.setCorrectAnswer(item.correctAnswer());
                    question.setCorrectAnswersJson(writeList(item.correctAnswers()));
                    return question;
                }).toList();
        for (int index = 0; index < questions.size(); index++) {
            questions.get(index).setQuestionOrder(index + 1);
        }
        return questionRepository.saveAll(questions);
    }

    private InterviewSessionResponse buildSession(List<InterviewQuestion> questions) {
        InterviewQuestion current = questions.stream()
                .filter(question -> !answerRepository.existsByQuestionId(question.getId()))
                .findFirst().orElse(null);
        int answered = (int) questions.stream()
                .filter(question -> answerRepository.existsByQuestionId(question.getId())).count();
        return new InterviewSessionResponse(current == null ? null : toQuestionResponse(current),
                current == null, answered, questions.size());
    }

    private AnswerResponse toAnswerResponse(Long interviewId) {
        List<InterviewQuestion> questions = questionRepository.findByInterviewIdOrderByQuestionOrder(interviewId);
        InterviewQuestion next = questions.stream()
                .filter(question -> !answerRepository.existsByQuestionId(question.getId()))
                .findFirst().orElse(null);
        int answered = (int) questions.stream()
                .filter(question -> answerRepository.existsByQuestionId(question.getId())).count();
        return new AnswerResponse(next == null ? null : toQuestionResponse(next),
                next == null, answered, questions.size());
    }

    private void ensureStarted(Interview interview) {
        if (interview.getStatus() == InterviewStatus.CREATED) {
            throw new IllegalArgumentException("Start the interview before loading questions");
        }
        if (interview.getStatus() == InterviewStatus.CANCELLED) {
            throw new IllegalArgumentException("Cancelled interviews have no active questions");
        }
    }

    private void ensureInProgress(Interview interview) {
        if (interview.getStatus() != InterviewStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Interview must be in progress");
        }
    }

    private Interview findOwnedInterview(String email, Long interviewId) {
        return interviewRepository.findByIdAndUserEmail(interviewId, normalizeEmail(email))
                .orElseThrow(() -> new InterviewNotFoundException("Interview not found"));
    }

    private QuestionResponse toQuestionResponse(InterviewQuestion question) {
        return new QuestionResponse(question.getId(), question.getQuestionText(),
                question.getQuestionOrder(), question.getQuestionType(), readList(question.getOptionsJson()));
    }

    private String answerValue(InterviewQuestion question, AnswerRequest request) {
        if (question.getQuestionType() == InterviewQuestionType.MCQ || question.getQuestionType() == InterviewQuestionType.TRUE_FALSE) {
            if (request.getSelectedOption() == null || request.getSelectedOption().isBlank()) throw new IllegalArgumentException("Select an answer");
            return request.getSelectedOption().trim();
        }
        if (question.getQuestionType() == InterviewQuestionType.MULTIPLE_SELECT) {
            if (request.getSelectedOptions() == null || request.getSelectedOptions().isEmpty()) throw new IllegalArgumentException("Select at least one answer");
            return writeList(request.getSelectedOptions());
        }
        if (request.getAnswerText() == null || request.getAnswerText().isBlank()) throw new IllegalArgumentException("Answer is required");
        return request.getAnswerText().trim();
    }

    private void saveEvaluation(Interview interview, InterviewQuestion question, InterviewAnswer answer) {
        if (evaluationRepository == null) return;
        AnswerEvaluation evaluation = new AnswerEvaluation(); evaluation.setAnswer(answer); evaluation.setCreatedAt(LocalDateTime.now()); evaluation.setAvailable(false);
        if (question.getQuestionType() == InterviewQuestionType.MCQ || question.getQuestionType() == InterviewQuestionType.TRUE_FALSE || question.getQuestionType() == InterviewQuestionType.MULTIPLE_SELECT) {
            boolean correct = question.getQuestionType() == InterviewQuestionType.MULTIPLE_SELECT
                    ? new java.util.HashSet<>(readList(question.getCorrectAnswersJson())).equals(new java.util.HashSet<>(readList(answer.getAnswerText())))
                    : answer.getAnswerText().equals(question.getCorrectAnswer());
            evaluation.setCorrect(correct); evaluation.setOverallScore(correct ? 10d : 0d); evaluation.setAvailable(true);
            evaluation.setFeedback(correct ? "Correct answer." : "Review the concepts behind this question.");
        } else if (aiService != null) {
            try { AIService.TextEvaluation result = aiService.evaluateTextAnswer(interview.getJobRole(), interview.getInterviewType(), interview.getDifficulty().name(), question.getQuestionText(), answer.getAnswerText());
                evaluation.setOverallScore(score(result.overallScore())); evaluation.setTechnicalScore(score(result.technicalScore())); evaluation.setRelevanceScore(score(result.relevanceScore())); evaluation.setClarityScore(score(result.clarityScore())); evaluation.setCompletenessScore(score(result.completenessScore())); evaluation.setFeedback(result.feedback()); evaluation.setStrengths(writeList(result.strengths())); evaluation.setImprovements(writeList(result.improvements())); evaluation.setWeaknesses(writeList(result.weaknesses())); evaluation.setImprovementSuggestion(result.improvementSuggestion()); evaluation.setIdealAnswerGuidance(result.idealAnswerGuidance()); evaluation.setAvailable(true);
            } catch (RuntimeException exception) { evaluation.setFeedback("AI evaluation unavailable."); }
        }
        evaluationRepository.save(evaluation);
    }

    private EvaluationResponse toEvaluationResponse(AnswerEvaluation evaluation) { if (evaluation == null) return null; EvaluationResponse response = new EvaluationResponse(evaluation.isAvailable(), evaluation.getCorrect(), evaluation.getOverallScore(), evaluation.getTechnicalScore(), evaluation.getRelevanceScore(), evaluation.getClarityScore(), evaluation.getCompletenessScore(), evaluation.getFeedback(), readList(evaluation.getStrengths()), readList(evaluation.getImprovements())); response.setStructuredFeedback(readList(evaluation.getWeaknesses()), evaluation.getImprovementSuggestion(), evaluation.getIdealAnswerGuidance()); return response; }
    private double score(double value) { return Double.isFinite(value) ? Math.max(0, Math.min(10, value)) : 0; }
    private Double average(List<AnswerEvaluation> evaluations, java.util.function.Function<AnswerEvaluation, Double> metric) { List<Double> values = evaluations.stream().map(metric).filter(java.util.Objects::nonNull).toList(); return values.isEmpty() ? null : values.stream().mapToDouble(Double::doubleValue).average().orElse(0); }
    private String writeList(List<String> values) { try { return objectMapper.writeValueAsString(values == null ? List.of() : values); } catch (JsonProcessingException exception) { throw new IllegalArgumentException("Could not store answer data", exception); } }
    private List<String> readList(String value) { try { return value == null || value.isBlank() ? List.of() : objectMapper.readValue(value, new TypeReference<>() { }); } catch (JsonProcessingException exception) { return List.of(); } }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
