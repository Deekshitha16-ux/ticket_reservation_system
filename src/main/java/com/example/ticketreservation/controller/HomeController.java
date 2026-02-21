package com.example.ticketreservation.controller;

import com.example.ticketreservation.model.Event;
import com.example.ticketreservation.model.Ticket;
import com.example.ticketreservation.service.PDFService;
import com.example.ticketreservation.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private PDFService pdfService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("events", ticketService.getAllEvents());
        model.addAttribute("singers", ticketService.getAllSingers());
        return "index";
    }

    @PostMapping("/book")
    public String bookTicket(@RequestParam Long eventId,
                             @RequestParam Long singerId,
                             @RequestParam String customerName,
                             @RequestParam String email,
                             @RequestParam String phoneNo,
                             @RequestParam int seatNumber,
                             Model model) {
        try {
            var ticket = ticketService.bookTicket(eventId, singerId, customerName, email, phoneNo, seatNumber);
            model.addAttribute("ticket", ticket);
            return "booking-success";
        } catch (RuntimeException e) {
            model.addAttribute("events", ticketService.getAllEvents());
            model.addAttribute("singers", ticketService.getAllSingers());
            model.addAttribute("error", e.getMessage());
            return "index";
        }
    }

    @GetMapping("/download-ticket/{ticketId}")
    public ResponseEntity<byte[]> downloadTicket(@PathVariable Long ticketId) {
        try {
            Ticket ticket = ticketService.getTicketById(ticketId);
            byte[] pdfContent = pdfService.generateTicketPDF(ticket);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ticket_" + ticketId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/manage-tickets")
    public String manageTickets(Model model) {
        try {
            var events = ticketService.getAllEvents();
            var tickets = ticketService.getAllTickets();
            var singers = ticketService.getAllSingers();
            System.out.println("DEBUG: Found " + events.size() + " events and " + tickets.size() + " tickets");
            model.addAttribute("events", events != null ? events : java.util.Collections.emptyList());
            model.addAttribute("tickets", tickets != null ? tickets : java.util.Collections.emptyList());
            model.addAttribute("singers", singers != null ? singers : java.util.Collections.emptyList());
            return "manage-tickets";
        } catch (Exception e) {
            System.err.println("ERROR in manageTickets: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("events", java.util.Collections.emptyList());
            model.addAttribute("tickets", java.util.Collections.emptyList());
            model.addAttribute("singers", java.util.Collections.emptyList());
            model.addAttribute("error", "Error loading manage-tickets page: " + e.getMessage());
            return "manage-tickets";
        }
    }

    @PostMapping("/search-ticket")
    public String searchTicket(@RequestParam int seatNumber,
                               @RequestParam Long eventId,
                               @RequestParam Long singerId,
                               Model model) {
        try {
            Ticket ticket = ticketService.findTicketBySeatNumberAndSinger(seatNumber, eventId, singerId);
            model.addAttribute("ticket", ticket);
            model.addAttribute("found", true);
            model.addAttribute("events", ticketService.getAllEvents());
            model.addAttribute("singers", ticketService.getAllSingers());
            model.addAttribute("tickets", ticketService.getAllTickets());
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("found", false);
            model.addAttribute("events", ticketService.getAllEvents());
            model.addAttribute("singers", ticketService.getAllSingers());
            model.addAttribute("tickets", ticketService.getAllTickets());
        }
        return "manage-tickets";
    }

    @PostMapping("/cancel-ticket/{ticketId}")
    public String cancelTicket(@PathVariable Long ticketId,
                               Model model) {
        try {
            Ticket ticket = ticketService.getTicketById(ticketId);
            String ticketInfo = "Ticket for " + ticket.getCustomerName() + " (Seat " + ticket.getSeatNumber() + ")";
            ticketService.cancelTicketByAdmin(ticketId);
            model.addAttribute("cancelSuccess", true);
            model.addAttribute("cancelledTicket", ticketInfo);
            model.addAttribute("events", ticketService.getAllEvents());
            model.addAttribute("singers", ticketService.getAllSingers());
            model.addAttribute("tickets", ticketService.getAllTickets());
        } catch (RuntimeException e) {
            model.addAttribute("cancelError", e.getMessage());
            model.addAttribute("events", ticketService.getAllEvents());
            model.addAttribute("singers", ticketService.getAllSingers());
            model.addAttribute("tickets", ticketService.getAllTickets());
        }
        return "manage-tickets";
    }
}
