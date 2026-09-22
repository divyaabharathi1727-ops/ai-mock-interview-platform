package com.mockinterview.backend.service;

import com.mockinterview.backend.dto.AnswerRequest;
import com.mockinterview.backend.dto.AnswerResponse;
import com.mockinterview.backend.dto.InterviewCompletionResponse;
import com.mockinterview.backend.dto.InterviewSessionResponse;
import com.mockinterview.backend.dto.InterviewSessionResultsResponse;
import com.mockinterview.backend.dto.QuestionResponse;
import com.mockinterview.backend.entity.Interview;
import com.mockinterview.backend.entity.InterviewAnswer;
import com.mockinterview.backend.entity.InterviewQuestion;
import com.mockinterview.backend.entity.InterviewStatus;
import com.mockinterview.backend.exception.InterviewNotFoundException;
import com.mockinterview.backend.repository.InterviewAnswerRepository;
import com.mockinterview.backend.repository.InterviewQuestionRepository;
import com.mockinterview.backend.repository.InterviewRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterviewQuestionService {

    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository questionRepository;
    private final InterviewAnswerRepository answerRepository;
    private final QuestionBankService questionBankService;

    public InterviewQuestionService(InterviewRepository interviewRepository,
                                    InterviewQuestionRepository questionRepository,
                                    InterviewAnswerRepository answerRepository,
                                    QuestionBankService questionBankService) {
        this.interviewRepository = interviewRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.questionBankService = questionBankService;
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
        InterviewAnswer answer = new InterviewAnswer();
        answer.setQuestion(question);
        answer.setAnswerText(request.getAnswerText().trim());
        answer.setAnsweredAt(LocalDateTime.now());
        answerRepository.save(answer);
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
        if (answeredQuestions < totalQuestions) {
            throw new IllegalArgumentException("Answer all questions before finishing the interview");
        }
        interview.setStatus(InterviewStatus.COMPLETED);
        interview.setCompletedAt(LocalDateTime.now());
        interviewRepository.save(interview);
        return new InterviewCompletionResponse(
                interview.getStatus(), totalQuestions, answeredQuestions, interview.getCompletedAt());
    }

    @Transactional(readOnly = true)
    public InterviewSessionResultsResponse getResults(String email, Long interviewId) {
        Interview interview = findOwnedInterview(email, interviewId);
        int totalQuestions = questionRepository.findByInterviewIdOrderByQuestionOrder(interviewId).size();
        int answeredQuestions = Math.toIntExact(answerRepository.countByQuestionInterviewId(interviewId));
        return new InterviewSessionResultsResponse(
                interview.getId(), interview.getJobRole(), interview.getInterviewType(),
                interview.getDifficulty(), interview.getStatus(), totalQuestions,
                answeredQuestions, interview.getCompletedAt());
    }

    private List<InterviewQuestion> generateInitialQuestions(Interview interview) {
        List<InterviewQuestion> questions = questionBankService
                .getDevelopmentQuestions(interview.getJobRole(), interview.getInterviewType())
                .stream().map(seed -> {
                    InterviewQuestion question = new InterviewQuestion();
                    question.setInterview(interview);
                    question.setQuestionText(seed.text());
                    question.setQuestionType(seed.type());
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
                question.getQuestionOrder(), question.getQuestionType());
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
