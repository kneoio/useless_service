package com.dictatorsclub.service;

import com.dictatorsclub.model.Dictator;
import com.dictatorsclub.repository.DictatorRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DictatorService {

    private final DictatorRepository dictatorRepository;

    public DictatorService(DictatorRepository dictatorRepository) {
        this.dictatorRepository = dictatorRepository;
    }

    public List<Dictator> findAll() {
        return dictatorRepository.findAll();
    }

    public Dictator findById(Long id) {
        return dictatorRepository.findById(id).orElse(null);
    }

    public Dictator findByUsername(String username) {
        return dictatorRepository.findByUsername(username).orElse(null);
    }

    public boolean existsByUsername(String username) {
        return dictatorRepository.existsByUsername(username);
    }

    public Dictator save(Dictator dictator) {
        return dictatorRepository.save(dictator);
    }

    public void deleteById(Long id) {
        dictatorRepository.deleteById(id);
    }

    public boolean isOwner(String username, Long dictatorId) {
        Dictator dictator = findById(dictatorId);
        return dictator != null && dictator.getUsername().equals(username);
    }
}
