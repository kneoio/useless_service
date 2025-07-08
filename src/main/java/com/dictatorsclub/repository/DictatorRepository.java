package com.dictatorsclub.repository;

import com.dictatorsclub.model.Dictator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DictatorRepository extends JpaRepository<Dictator, Long> {
    Optional<Dictator> findByUsername(String username);
    boolean existsByUsername(String username);
}
