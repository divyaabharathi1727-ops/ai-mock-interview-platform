package com.mockinterview.backend.repository;

import com.mockinterview.backend.entity.Interview;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {

    List<Interview> findByUserEmailOrderByCreatedAtDesc(String email);

    Optional<Interview> findByIdAndUserEmail(Long id, String email);
}
