package com.dictatorsclub.controller;

import com.dictatorsclub.model.Dictator;
import com.dictatorsclub.service.DictatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dictators")
@CrossOrigin(origins = "*")
public class DictatorController {

    private final DictatorService dictatorService;

    public DictatorController(DictatorService dictatorService) {
        this.dictatorService = dictatorService;
    }

    @GetMapping
    public ResponseEntity<List<Dictator>> getAllDictators() {
        List<Dictator> dictators = dictatorService.findAll();
        return ResponseEntity.ok(dictators);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dictator> getDictatorById(@PathVariable Long id) {
        Dictator dictator = dictatorService.findById(id);
        if (dictator == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dictator);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Dictator> getDictatorByUsername(@PathVariable String username) {
        Dictator dictator = dictatorService.findByUsername(username);
        if (dictator == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dictator);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDictator(@PathVariable Long id, @RequestBody Dictator updatedDictator, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        String currentUsername = getUsernameFromAuth(authentication);
        if (!dictatorService.isOwner(currentUsername, id)) {
            return ResponseEntity.status(403).body("You can only edit your own profile");
        }

        Dictator existingDictator = dictatorService.findById(id);
        if (existingDictator == null) {
            return ResponseEntity.notFound().build();
        }

        // Update fields but keep username and timestamps
        existingDictator.setName(updatedDictator.getName());
        existingDictator.setCountry(updatedDictator.getCountry());
        existingDictator.setDescription(updatedDictator.getDescription());
        existingDictator.setYearsInPower(updatedDictator.getYearsInPower());

        Dictator savedDictator = dictatorService.save(existingDictator);
        return ResponseEntity.ok(savedDictator);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDictator(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Authentication required");
        }

        String currentUsername = getUsernameFromAuth(authentication);
        if (!dictatorService.isOwner(currentUsername, id)) {
            return ResponseEntity.status(403).body("You can only delete your own profile");
        }

        Dictator dictator = dictatorService.findById(id);
        if (dictator == null) {
            return ResponseEntity.notFound().build();
        }

        dictatorService.deleteById(id);
        return ResponseEntity.ok().body("Dictator profile deleted successfully");
    }

    private String getUsernameFromAuth(Authentication authentication) {
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            // Extract username from JWT token (adjust claim name as needed)
            return jwt.getClaimAsString("preferred_username"); // or "sub" or whatever your OIDC provider uses
        }
        return authentication.getName();
    }
}
