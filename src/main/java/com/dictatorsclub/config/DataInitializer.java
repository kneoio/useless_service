package com.dictatorsclub.config;

import com.dictatorsclub.model.Achievement;
import com.dictatorsclub.model.Dictator;
import com.dictatorsclub.repository.AchievementRepository;
import com.dictatorsclub.repository.DictatorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DictatorRepository dictatorRepository;
    private final AchievementRepository achievementRepository;

    public DataInitializer(DictatorRepository dictatorRepository, AchievementRepository achievementRepository) {
        this.dictatorRepository = dictatorRepository;
        this.achievementRepository = achievementRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create sample dictators
        Dictator napoleon = new Dictator(
            "napoleon",
            "Napoleon Bonaparte",
            "France",
            "Emperor of the French, military genius, and conqueror of Europe",
            "1799-1815"
        );

        Dictator caesar = new Dictator(
            "caesar",
            "Julius Caesar",
            "Roman Empire",
            "Roman general and statesman who played a critical role in the events that led to the demise of the Roman Republic",
            "49-44 BC"
        );

        Dictator genghis = new Dictator(
            "genghis",
            "Genghis Khan",
            "Mongol Empire",
            "Founder and first Great Khan of the Mongol Empire, which became the largest contiguous empire in history",
            "1206-1227"
        );

        dictatorRepository.save(napoleon);
        dictatorRepository.save(caesar);
        dictatorRepository.save(genghis);

        // Create sample achievements
        Achievement napoleonAchievement1 = new Achievement(
            "Conquered most of Europe",
            "Successfully conquered and controlled most of continental Europe through military campaigns",
            1807,
            napoleon
        );

        Achievement napoleonAchievement2 = new Achievement(
            "Napoleonic Code",
            "Created the Napoleonic Code, a civil code that influenced legal systems worldwide",
            1804,
            napoleon
        );

        Achievement caesarAchievement1 = new Achievement(
            "Crossed the Rubicon",
            "Made the famous decision to cross the Rubicon river, starting a civil war that led to his rise to power",
            49,
            caesar
        );

        Achievement caesarAchievement2 = new Achievement(
            "Conquered Gaul",
            "Successfully conquered all of Gaul (modern-day France) in the Gallic Wars",
            50,
            caesar
        );

        Achievement genghisAchievement1 = new Achievement(
            "Created the largest contiguous empire",
            "Built the Mongol Empire, the largest contiguous land empire in history",
            1220,
            genghis
        );

        Achievement genghisAchievement2 = new Achievement(
            "United the Mongol tribes",
            "Successfully united the warring Mongol tribes under his leadership",
            1206,
            genghis
        );

        achievementRepository.save(napoleonAchievement1);
        achievementRepository.save(napoleonAchievement2);
        achievementRepository.save(caesarAchievement1);
        achievementRepository.save(caesarAchievement2);
        achievementRepository.save(genghisAchievement1);
        achievementRepository.save(genghisAchievement2);

        System.out.println("Sample data initialized successfully!");
    }
}
