package com.example.ticketreservation.config;

import com.example.ticketreservation.model.Event;
import com.example.ticketreservation.model.Singer;
import com.example.ticketreservation.repository.EventRepository;
import com.example.ticketreservation.repository.SingerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(2) // Run after DataInitializer (which has order 1 by default)
public class EventInitializer implements CommandLineRunner {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SingerRepository singerRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if events already exist
        if (eventRepository.count() == 0) {
            // Get singers - wait a bit to ensure they're loaded
            List<Singer> singers = singerRepository.findAll();
            
            int retries = 5;
            while (singers.isEmpty() && retries > 0) {
                System.out.println("Waiting for singers to be initialized...");
                Thread.sleep(500); // Wait 500ms
                singers = singerRepository.findAll();
                retries--;
            }
            
            if (!singers.isEmpty()) {
                // Create sample events
                Event event1 = new Event("Bollywood Music Festival", "Delhi Convention Center", 100);
                event1.setSingers(singers.subList(0, Math.min(5, singers.size())));
                eventRepository.save(event1);
                
                Event event2 = new Event("Classical Music Concert", "Mumbai Auditorium", 80);
                event2.setSingers(singers.subList(Math.max(0, singers.size() - 5), singers.size()));
                eventRepository.save(event2);
                
                Event event3 = new Event("Indie Artists Night", "Bangalore Live House", 120);
                event3.setSingers(Arrays.asList(singers.get(0)));
                eventRepository.save(event3);
                
                System.out.println("✅ 3 events have been added to the database");
            } else {
                System.err.println("❌ No singers found - events were not created. Please restart the application.");
            }
        } else {
            System.out.println("Events already exist in database, skipping initialization");
        }
    }
}
