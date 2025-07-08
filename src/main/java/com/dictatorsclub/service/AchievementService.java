package com.dictatorsclub.service;

import com.dictatorsclub.model.Achievement;
import com.dictatorsclub.model.Dictator;
import com.dictatorsclub.repository.AchievementRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final DictatorService dictatorService;

    public AchievementService(AchievementRepository achievementRepository, DictatorService dictatorService) {
        this.achievementRepository = achievementRepository;
        this.dictatorService = dictatorService;
    }

    public List<Achievement> findAll() {
        return achievementRepository.findAll();
    }

    public Achievement findById(Long id) {
        return achievementRepository.findById(id).orElse(null);
    }

    public List<Achievement> findByDictatorId(Long dictatorId) {
        return achievementRepository.findByDictatorId(dictatorId);
    }

    public List<Achievement> findByDictatorUsername(String username) {
        return achievementRepository.findByDictatorUsername(username);
    }

    public Achievement save(Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    public void deleteById(Long id) {
        achievementRepository.deleteById(id);
    }

    public boolean isOwner(String username, Long achievementId) {
        Achievement achievement = findById(achievementId);
        return achievement != null && achievement.getDictator().getUsername().equals(username);
    }
}
