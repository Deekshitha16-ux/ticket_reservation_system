package com.example.ticketreservation.config;

import com.example.ticketreservation.model.Singer;
import com.example.ticketreservation.repository.SingerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1) // Run first to ensure singers are created before events
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SingerRepository singerRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if singers already exist to avoid duplicates
        if (singerRepository.count() == 0) {
            // Add 10 popular singers
            singerRepository.save(new Singer("Arijit Singh", "Bollywood/Indie"));
            singerRepository.save(new Singer("Neha Kakkar", "Bollywood/Pop"));
            singerRepository.save(new Singer("Ar Rahman", "Classical/Bollywood"));
            singerRepository.save(new Singer("Shreya Ghoshal", "Bollywood/Classical"));
            singerRepository.save(new Singer("Sunidhi Chauhan", "Bollywood"));
            singerRepository.save(new Singer("Sonu Nigam", "Bollywood/Classical"));
            singerRepository.save(new Singer("Udit Narayan", "Bollywood"));
            singerRepository.save(new Singer("Alka Yagnik", "Bollywood"));
            singerRepository.save(new Singer("Asha Bhosle", "Bollywood/Classical"));
            singerRepository.save(new Singer("Lata Mangeshkar", "Bollywood/Classical"));
            
            System.out.println("✅ 10 singers have been added to the database");
        }
    }
}
