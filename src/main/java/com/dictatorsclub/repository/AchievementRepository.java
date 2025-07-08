package com.dictatorsclub.repository;

import com.dictatorsclub.model.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByDictatorId(Long dictatorId);
    List<Achievement> findByDictatorUsername(String username);
}
