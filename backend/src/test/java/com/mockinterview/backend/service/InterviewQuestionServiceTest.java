package com.mockinterview.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mockinterview.backend.dto.AnswerRequest;
import com.mockinterview.backend.dto.AnswerResponse;
import com.mockinterview.backend.dto.InterviewCompletionResponse;
import com.mockinterview.backend.dto.InterviewSessionResponse;
import com.mockinterview.backend.entity.Interview;
import com.mockinterview.backend.entity.InterviewAnswer;
import com.mockinterview.backend.entity.InterviewDifficulty;
import com.mockinterview.backend.entity.InterviewQuestion;
import com.mockinterview.backend.entity.InterviewQuestionType;
import com.mockinterview.backend.entity.InterviewStatus;
import com.mockinterview.backend.entity.User;
import com.mockinterview.backend.exception.InterviewNotFoundException;
import com.mockinterview.backend.repository.InterviewAnswerRepository;
import com.mockinterview.backend.repository.InterviewQuestionRepository;
import com.mockinterview.backend.repository.InterviewRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InterviewQuestionServiceTest {

    @Mock private InterviewRepository interviewRepository;
    @Mock private InterviewQuestionRepository questionRepository;
    @Mock private InterviewAnswerRepository answerRepository;
    @Mock private QuestionBankService questionBankService;

    private InterviewQuestionService service;

    @BeforeEach
    void setUp() {
        service = new InterviewQuestionService(
                interviewRepository, questionRepository, answerRepository, questionBankService);
    }

    @Test
    void startInterview_shouldGenerateQuestionsAndMoveToInProgress() {
        Interview interview = interview(1L, "student@example.com", InterviewStatus.CREATED);
        InterviewQuestion first = question(11L, interview, 1, "First question");
        when(interviewRepository.findByIdAndUserEmail(1L, "student@example.com"))
                .thenReturn(Optional.of(interview));
        when(questionRepository.findByInterviewIdOrderByQuestionOrder(1L))
                .thenReturn(List.of(), List.of(first));
        when(questionRepository.saveAll(any())).thenReturn(List.of(first));
        when(answerRepository.existsByQuestionId(11L)).thenReturn(false);

        InterviewSessionResponse response = service.startInterview("student@example.com", 1L);

        assertEquals(InterviewStatus.IN_PROGRESS, interview.getStatus());
        assertEquals("First question", response.getCurrentQuestion().getQuestionText());
        assertEquals(1, response.getTotalQuestions());
        verify(interviewRepository).save(interview);
    }

    @Test
    void startInterview_twice_shouldReuseExistingQuestions() {
        Interview interview = interview(1L, "student@example.com", InterviewStatus.IN_PROGRESS);
        InterviewQuestion first = question(11L, interview, 1, "Existing question");
        when(interviewRepository.findByIdAndUserEmail(1L, "student@example.com"))
                .thenReturn(Optional.of(interview));
        when(questionRepository.findByInterviewIdOrderByQuestionOrder(1L))
                .thenReturn(List.of(first));
        when(answerRepository.existsByQuestionId(11L)).thenReturn(false);

        InterviewSessionResponse response = service.startInterview("student@example.com", 1L);

        assertEquals("Existing question", response.getCurrentQuestion().getQuestionText());
        verify(questionRepository, never()).saveAll(any());
    }

    @Test
    void submitAnswer_shouldPersistAnswerAndReturnNextQuestion() {
        Interview interview = interview(1L, "student@example.com", InterviewStatus.IN_PROGRESS);
        InterviewQuestion first = question(11L, interview, 1, "First");
        InterviewQuestion second = question(12L, interview, 2, "Second");
        when(interviewRepository.findByIdAndUserEmail(1L, "student@example.com"))
                .thenReturn(Optional.of(interview));
        when(questionRepository.findByIdAndInterviewId(11L, 1L)).thenReturn(Optional.of(first));
        when(answerRepository.existsByQuestionId(11L)).thenReturn(false, true);
        when(questionRepository.findByInterviewIdOrderByQuestionOrder(1L))
                .thenReturn(List.of(first, second));
        when(answerRepository.existsByQuestionId(12L)).thenReturn(false);

        AnswerRequest request = new AnswerRequest();
        request.setAnswerText("My answer");
        AnswerResponse response = service.submitAnswer("student@example.com", 1L, 11L, request);

        assertEquals("Second", response.getNextQuestion().getQuestionText());
        assertEquals(1, response.getAnsweredQuestions());
        verify(answerRepository).save(any(InterviewAnswer.class));
    }

    @Test
    void submitAnswer_shouldRejectQuestionOwnedByAnotherInterview() {
        Interview interview = interview(1L, "student@example.com", InterviewStatus.IN_PROGRESS);
        when(interviewRepository.findByIdAndUserEmail(1L, "student@example.com"))
                .thenReturn(Optional.of(interview));
        when(questionRepository.findByIdAndInterviewId(99L, 1L)).thenReturn(Optional.empty());

        AnswerRequest request = new AnswerRequest();
        request.setAnswerText("My answer");

        assertThrows(InterviewNotFoundException.class,
                () -> service.submitAnswer("student@example.com", 1L, 99L, request));
    }

    @Test
    void finishInterview_shouldCompleteWhenAllQuestionsAnswered() {
        Interview interview = interview(1L, "student@example.com", InterviewStatus.IN_PROGRESS);
        InterviewQuestion first = question(11L, interview, 1, "First");
        when(interviewRepository.findByIdAndUserEmail(1L, "student@example.com"))
                .thenReturn(Optional.of(interview));
        when(questionRepository.findByInterviewIdOrderByQuestionOrder(1L))
                .thenReturn(List.of(first));
        when(answerRepository.countByQuestionInterviewId(1L)).thenReturn(1L);

        InterviewCompletionResponse response = service.finishInterview("student@example.com", 1L);

        assertEquals(InterviewStatus.COMPLETED, response.getStatus());
        assertNotNull(response.getCompletedAt());
        verify(interviewRepository).save(interview);
    }

    @Test
    void finishInterview_shouldRejectIncompleteSession() {
        Interview interview = interview(1L, "student@example.com", InterviewStatus.IN_PROGRESS);
        when(interviewRepository.findByIdAndUserEmail(1L, "student@example.com"))
                .thenReturn(Optional.of(interview));
        when(questionRepository.findByInterviewIdOrderByQuestionOrder(1L))
                .thenReturn(List.of(question(11L, interview, 1, "First")));
        when(answerRepository.countByQuestionInterviewId(1L)).thenReturn(0L);

        assertThrows(IllegalArgumentException.class,
                () -> service.finishInterview("student@example.com", 1L));
    }

    @Test
    void completedInterview_cannotStartAgain() {
        Interview interview = interview(1L, "student@example.com", InterviewStatus.COMPLETED);
        when(interviewRepository.findByIdAndUserEmail(1L, "student@example.com"))
                .thenReturn(Optional.of(interview));

        assertThrows(IllegalArgumentException.class,
                () -> service.startInterview("student@example.com", 1L));
    }

    private Interview interview(Long id, String email, InterviewStatus status) {
        User user = new User();
        user.setEmail(email);
        user.setId(10L);
        Interview interview = new Interview();
        interview.setId(id);
        interview.setUser(user);
        interview.setJobRole("Java Developer");
        interview.setInterviewType("Technical");
        interview.setDifficulty(InterviewDifficulty.MEDIUM);
        interview.setStatus(status);
        return interview;
    }

    private InterviewQuestion question(Long id, Interview interview, int order, String text) {
        InterviewQuestion question = new InterviewQuestion();
        question.setId(id);
        question.setInterview(interview);
        question.setQuestionOrder(order);
        question.setQuestionText(text);
        question.setQuestionType(InterviewQuestionType.TECHNICAL);
        return question;
    }
}
