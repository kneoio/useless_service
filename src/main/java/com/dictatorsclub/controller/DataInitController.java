package com.dictatorsclub.controller;

import com.dictatorsclub.model.Achievement;
import com.dictatorsclub.model.Dictator;
import com.dictatorsclub.service.AchievementService;
import com.dictatorsclub.service.DictatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/init")
@CrossOrigin(origins = "*")
public class DataInitController {

    private final DictatorService dictatorService;
    private final AchievementService achievementService;

    public DataInitController(DictatorService dictatorService, AchievementService achievementService) {
        this.dictatorService = dictatorService;
        this.achievementService = achievementService;
    }

    @PostMapping("/dictator")
    public ResponseEntity<?> createDictator(@RequestBody Dictator dictator) {
        try {
            if (dictatorService.existsByUsername(dictator.getUsername())) {
                return ResponseEntity.badRequest().body("Dictator with username '" + dictator.getUsername() + "' already exists");
            }

            Dictator savedDictator = dictatorService.save(dictator);
            return ResponseEntity.ok(savedDictator);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create dictator: " + e.getMessage());
        }
    }

    @PostMapping("/dictator/{dictatorId}/achievement")
    public ResponseEntity<?> createAchievement(@PathVariable Long dictatorId, @RequestBody Achievement achievement) {
        try {
            Dictator dictator = dictatorService.findById(dictatorId);
            if (dictator == null) {
                return ResponseEntity.notFound().build();
            }

            achievement.setDictator(dictator);
            Achievement savedAchievement = achievementService.save(achievement);
            return ResponseEntity.ok(savedAchievement);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create achievement: " + e.getMessage());
        }
    }

    @PostMapping("/sample-data")
    public ResponseEntity<?> initializeSampleData() {
        try {
            Map<String, Object> result = new HashMap<>();
            int dictatorsCreated = 0;
            int achievementsCreated = 0;

            // Create Napoleon
            if (!dictatorService.existsByUsername("napoleon")) {
                Dictator napoleon = new Dictator("napoleon", "Napoleon Bonaparte", "France", 
                    "Emperor of the French, military genius, and conqueror of Europe", "1799-1815");
                napoleon = dictatorService.save(napoleon);
                dictatorsCreated++;

                // Napoleon's achievements
                Achievement napoleonAch1 = new Achievement("Conquered most of Europe", 
                    "Successfully conquered and controlled most of continental Europe through military campaigns", 1807, napoleon);
                Achievement napoleonAch2 = new Achievement("Napoleonic Code", 
                    "Created the Napoleonic Code, a civil code that influenced legal systems worldwide", 1804, napoleon);
                achievementService.save(napoleonAch1);
                achievementService.save(napoleonAch2);
                achievementsCreated += 2;
            }

            // Create Caesar
            if (!dictatorService.existsByUsername("caesar")) {
                Dictator caesar = new Dictator("caesar", "Julius Caesar", "Roman Empire", 
                    "Roman general and statesman who played a critical role in the events that led to the demise of the Roman Republic", "49-44 BC");
                caesar = dictatorService.save(caesar);
                dictatorsCreated++;

                // Caesar's achievements
                Achievement caesarAch1 = new Achievement("Crossed the Rubicon", 
                    "Made the famous decision to cross the Rubicon river, starting a civil war that led to his rise to power", 49, caesar);
                Achievement caesarAch2 = new Achievement("Conquered Gaul", 
                    "Successfully conquered all of Gaul (modern-day France) in the Gallic Wars", 50, caesar);
                achievementService.save(caesarAch1);
                achievementService.save(caesarAch2);
                achievementsCreated += 2;
            }

            // Create Genghis Khan
            if (!dictatorService.existsByUsername("genghis")) {
                Dictator genghis = new Dictator("genghis", "Genghis Khan", "Mongol Empire", 
                    "Founder and first Great Khan of the Mongol Empire, which became the largest contiguous empire in history", "1206-1227");
                genghis = dictatorService.save(genghis);
                dictatorsCreated++;

                // Genghis Khan's achievements
                Achievement genghisAch1 = new Achievement("Created the largest contiguous empire", 
                    "Built the Mongol Empire, the largest contiguous land empire in history", 1220, genghis);
                Achievement genghisAch2 = new Achievement("United the Mongol tribes", 
                    "Successfully united the warring Mongol tribes under his leadership", 1206, genghis);
                achievementService.save(genghisAch1);
                achievementService.save(genghisAch2);
                achievementsCreated += 2;
            }

            result.put("message", "Sample data initialization completed");
            result.put("dictatorsCreated", dictatorsCreated);
            result.put("achievementsCreated", achievementsCreated);
            result.put("totalDictators", dictatorService.findAll().size());
            result.put("totalAchievements", achievementService.findAll().size());

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to initialize sample data: " + e.getMessage());
        }
    }
}
