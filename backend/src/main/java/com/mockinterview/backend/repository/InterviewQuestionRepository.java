package com.mockinterview.backend.repository;

import com.mockinterview.backend.entity.InterviewQuestion;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findByInterviewIdOrderByQuestionOrder(Long interviewId);

    Optional<InterviewQuestion> findByIdAndInterviewId(Long id, Long interviewId);
}
