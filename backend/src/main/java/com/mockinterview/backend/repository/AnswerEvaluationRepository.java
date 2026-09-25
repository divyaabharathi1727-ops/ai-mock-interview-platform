package com.mockinterview.backend.repository;

import com.mockinterview.backend.entity.AnswerEvaluation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerEvaluationRepository extends JpaRepository<AnswerEvaluation, Long> {

    Optional<AnswerEvaluation> findByAnswerId(Long answerId);

    List<AnswerEvaluation> findByAnswerQuestionInterviewId(Long interviewId);
}
