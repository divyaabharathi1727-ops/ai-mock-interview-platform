package com.mockinterview.backend.repository;

import com.mockinterview.backend.entity.InterviewAnswer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewAnswerRepository extends JpaRepository<InterviewAnswer, Long> {

    boolean existsByQuestionId(Long questionId);

    long countByQuestionInterviewId(Long interviewId);

    List<InterviewAnswer> findByQuestionInterviewIdOrderByAnsweredAtAsc(Long interviewId);
}
