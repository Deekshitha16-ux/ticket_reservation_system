package com.example.ticketreservation.controller;

import com.example.ticketreservation.model.Event;
import com.example.ticketreservation.model.Singer;
import com.example.ticketreservation.model.Ticket;
import com.example.ticketreservation.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    // Create Event
    @PostMapping("/events")
    public Event createEvent(@RequestBody Event event) {
        return ticketService.createEvent(event);
    }

    // Get All Events
    @GetMapping("/events")
    public List<Event> getAllEvents() {
        return ticketService.getAllEvents();
    }

    // Get All Singers
    @GetMapping("/singers")
    public List<Singer> getAllSingers() {
        return ticketService.getAllSingers();
    }

    // Get Singers for a specific event
    @GetMapping("/events/{eventId}/singers")
    public List<Singer> getSingersByEvent(@PathVariable Long eventId) {
        return ticketService.getSingersByEvent(eventId);
    }

    // Book Ticket
    @PostMapping("/book")
    public Ticket bookTicket(@RequestParam Long eventId,
                             @RequestParam Long singerId,
                             @RequestParam String customerName,
                             @RequestParam String email,
                             @RequestParam String phoneNo,
                             @RequestParam int seatNumber) {
        return ticketService.bookTicket(eventId, singerId, customerName, email, phoneNo, seatNumber);
    }

    // Cancel Ticket
    @DeleteMapping("/cancel/{ticketId}")
    public String cancelTicket(@PathVariable Long ticketId) {
        ticketService.cancelTicket(ticketId);
        return "Ticket cancelled successfully";
    }

    // Get booked seats for an event
    @GetMapping("/events/{eventId}/booked-seats")
    public List<Integer> getBookedSeats(@PathVariable Long eventId) {
        return ticketService.getBookedSeats(eventId);
    }

    // Get booked seats for an event and singer
    @GetMapping("/events/{eventId}/singers/{singerId}/booked-seats")
    public List<Integer> getBookedSeatsByEventAndSinger(@PathVariable Long eventId, @PathVariable Long singerId) {
        return ticketService.getBookedSeatsByEventAndSinger(eventId, singerId);
    }

    // Get available seats for an event and singer
    @GetMapping("/events/{eventId}/singers/{singerId}/available-seats")
    public List<Integer> getAvailableSeatsByEventAndSinger(@PathVariable Long eventId, @PathVariable Long singerId) {
        return ticketService.getAvailableSeatsByEventAndSinger(eventId, singerId);
    }
}
