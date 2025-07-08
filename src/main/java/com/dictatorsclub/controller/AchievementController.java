package com.dictatorsclub.controller;

import com.dictatorsclub.model.Achievement;
import com.dictatorsclub.model.Dictator;
import com.dictatorsclub.service.AchievementService;
import com.dictatorsclub.service.DictatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AchievementController {

    private final AchievementService achievementService;
    private final DictatorService dictatorService;

    public AchievementController(AchievementService achievementService, DictatorService dictatorService) {
        this.achievementService = achievementService;
        this.dictatorService = dictatorService;
    }

    @GetMapping("/dictators/{dictatorId}/achievements")
    public ResponseEntity<List<Achievement>> getAchievementsByDictator(@PathVariable Long dictatorId) {
        List<Achievement> achievements = achievementService.findByDictatorId(dictatorId);
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/achievements")
    public ResponseEntity<List<Achievement>> getAllAchievements() {
        List<Achievement> achievements = achievementService.findAll();
        return ResponseEntity.ok(achievements);
    }

    @GetMapping("/achievements/{id}")
    public ResponseEntity<Achievement> getAchievementById(@PathVariable Long id) {
        Achievement achievement = achievementService.findById(id);
        if (achievement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(achievement);
    }

    @PostMapping("/dictators/{dictatorId}/achievements")
    public ResponseEntity<?> createAchievement(@PathVariable Long dictatorId, @RequestBody Achievement achievement, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        String currentUsername = getUsernameFromAuth(authentication);
        if (!dictatorService.isOwner(currentUsername, dictatorId)) {
            return ResponseEntity.status(403).body("You can only add achievements to your own profile");
        }

        Dictator dictator = dictatorService.findById(dictatorId);
        if (dictator == null) {
            return ResponseEntity.notFound().build();
        }

        achievement.setDictator(dictator);
        Achievement savedAchievement = achievementService.save(achievement);
        return ResponseEntity.ok(savedAchievement);
    }

    @PutMapping("/achievements/{id}")
    public ResponseEntity<?> updateAchievement(@PathVariable Long id, @RequestBody Achievement updatedAchievement, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        String currentUsername = getUsernameFromAuth(authentication);
        if (!achievementService.isOwner(currentUsername, id)) {
            return ResponseEntity.status(403).body("You can only edit your own achievements");
        }

        Achievement existingAchievement = achievementService.findById(id);
        if (existingAchievement == null) {
            return ResponseEntity.notFound().build();
        }

        existingAchievement.setTitle(updatedAchievement.getTitle());
        existingAchievement.setDescription(updatedAchievement.getDescription());
        existingAchievement.setYear(updatedAchievement.getYear());

        Achievement savedAchievement = achievementService.save(existingAchievement);
        return ResponseEntity.ok(savedAchievement);
    }

    @DeleteMapping("/achievements/{id}")
    public ResponseEntity<?> deleteAchievement(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        String currentUsername = getUsernameFromAuth(authentication);
        if (!achievementService.isOwner(currentUsername, id)) {
            return ResponseEntity.status(403).body("You can only delete your own achievements");
        }

        Achievement achievement = achievementService.findById(id);
        if (achievement == null) {
            return ResponseEntity.notFound().build();
        }

        achievementService.deleteById(id);
        return ResponseEntity.ok().body("Achievement deleted successfully");
    }

    private String getUsernameFromAuth(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            // Extract username from JWT token (adjust claim name as needed)
            return jwt.getClaimAsString("preferred_username"); // or "sub" or whatever your OIDC provider uses
        }
        return authentication.getName();
    }
}
