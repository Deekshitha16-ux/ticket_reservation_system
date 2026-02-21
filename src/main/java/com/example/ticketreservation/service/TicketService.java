package com.example.ticketreservation.service;

import com.example.ticketreservation.model.Event;
import com.example.ticketreservation.model.Singer;
import com.example.ticketreservation.model.Ticket;
import com.example.ticketreservation.repository.EventRepository;
import com.example.ticketreservation.repository.SingerRepository;
import com.example.ticketreservation.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private SingerRepository singerRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Singer> getAllSingers() {
        return singerRepository.findAll();
    }

    public List<Singer> getSingersByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        return event.getSingers();
    }

    public List<Integer> getBookedSeats(Long eventId) {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
            .filter(ticket -> ticket.getEvent().getId().equals(eventId))
            .map(Ticket::getSeatNumber)
            .collect(Collectors.toList());
    }

    public List<Integer> getBookedSeatsByEventAndSinger(Long eventId, Long singerId) {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
            .filter(ticket -> ticket.getEvent().getId().equals(eventId) 
                && ticket.getSinger().getId().equals(singerId))
            .map(Ticket::getSeatNumber)
            .collect(Collectors.toList());
    }

    public List<Integer> getAvailableSeats(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        List<Integer> bookedSeats = getBookedSeats(eventId);
        List<Integer> availableSeats = new ArrayList<>();
        
        for (int i = 1; i <= event.getTotalSeats(); i++) {
            if (!bookedSeats.contains(i)) {
                availableSeats.add(i);
            }
        }
        
        return availableSeats;
    }

    public List<Integer> getAvailableSeatsByEventAndSinger(Long eventId, Long singerId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        List<Integer> bookedSeats = getBookedSeatsByEventAndSinger(eventId, singerId);
        List<Integer> availableSeats = new ArrayList<>();
        
        for (int i = 1; i <= event.getTotalSeats(); i++) {
            if (!bookedSeats.contains(i)) {
                availableSeats.add(i);
            }
        }
        
        return availableSeats;
    }

    public Ticket getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId).orElseThrow(() ->
            new RuntimeException("Ticket not found"));
    }

    public Ticket findTicketBySeatNumber(int seatNumber, Long eventId) {
        List<Ticket> tickets = ticketRepository.findAll();
        for (Ticket ticket : tickets) {
            if (ticket.getSeatNumber() == seatNumber && ticket.getEvent().getId().equals(eventId)) {
                return ticket;
            }
        }
        throw new RuntimeException("No ticket found for seat number " + seatNumber + " in this event");
    }

    public Ticket findTicketBySeatNumberAndSinger(int seatNumber, Long eventId, Long singerId) {
        List<Ticket> tickets = ticketRepository.findAll();
        for (Ticket ticket : tickets) {
            if (ticket.getSeatNumber() == seatNumber 
                && ticket.getEvent().getId().equals(eventId)
                && ticket.getSinger().getId().equals(singerId)) {
                return ticket;
            }
        }
        throw new RuntimeException("No ticket found for seat number " + seatNumber + " with this singer in this event");
    }

    @Transactional
    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        // Force eager loading of relationships
        for (Ticket ticket : tickets) {
            if (ticket.getEvent() != null) {
                ticket.getEvent().getEventName(); // Force load event
            }
            if (ticket.getSinger() != null) {
                ticket.getSinger().getSingerName(); // Force load singer
            }
        }
        return tickets;
    }

    public void cancelTicketByAdmin(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
            new RuntimeException("Ticket not found"));
        
        Event event = ticket.getEvent();
        event.setAvailableSeats(event.getAvailableSeats() + 1);
        eventRepository.save(event);
        
        ticketRepository.deleteById(ticketId);
    }

    public Ticket bookTicket(Long eventId, Long singerId, String customerName, String email, String phoneNo, int seatNumber) {

        Event event = eventRepository.findById(eventId).orElseThrow(() ->
            new RuntimeException("Event not found"));

        Singer singer = singerRepository.findById(singerId).orElseThrow(() ->
            new RuntimeException("Singer not found"));

        if (event.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available");
        }

        // Check if seat number is valid
        if (seatNumber < 1 || seatNumber > event.getTotalSeats()) {
            throw new RuntimeException("Invalid seat number");
        }

        // Check if seat is already booked for this specific singer
        List<Integer> bookedSeats = getBookedSeatsByEventAndSinger(eventId, singerId);
        if (bookedSeats.contains(seatNumber)) {
            throw new RuntimeException("Seat " + seatNumber + " is already booked for this singer. Please select another seat.");
        }

        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);

        Ticket ticket = new Ticket(customerName, email, phoneNo, seatNumber, event, singer);
        return ticketRepository.save(ticket);
    }

    public Event createEvent(Event event) {
        event.setAvailableSeats(event.getTotalSeats());
        return eventRepository.save(event);
    }

    public void cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
            new RuntimeException("Ticket not found"));
        
        Event event = ticket.getEvent();
        event.setAvailableSeats(event.getAvailableSeats() + 1);
        eventRepository.save(event);
        
        ticketRepository.deleteById(ticketId);
    }
}
