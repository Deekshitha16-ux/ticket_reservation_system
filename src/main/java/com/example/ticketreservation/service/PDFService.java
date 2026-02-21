package com.example.ticketreservation.service;

import com.example.ticketreservation.model.Ticket;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PDFService {

    public byte[] generateTicketPDF(Ticket ticket) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);
        
        // Set margins
        document.setMargins(20, 20, 20, 20);
        
        // Title
        PdfFont titleFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont headerFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        
        // Header
        Paragraph header = new Paragraph("🎫 TICKET CONFIRMATION")
                .setFont(titleFont)
                .setFontSize(24)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(header);
        
        // Subheader
        Paragraph subheader = new Paragraph("Ticket Reservation System")
                .setFont(normalFont)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(subheader);
        
        // Ticket ID Section
        Paragraph ticketId = new Paragraph("Ticket ID: " + ticket.getId())
                .setFont(headerFont)
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(ticketId);
        
        // Passenger Information Section
        Paragraph passengerHeader = new Paragraph("PASSENGER INFORMATION")
                .setFont(headerFont)
                .setFontSize(12)
                .setMarginBottom(10);
        document.add(passengerHeader);
        
        Table passengerTable = new Table(2);
        passengerTable.setWidth(com.itextpdf.kernel.geom.PageSize.A4.getWidth() - 40);
        
        addTableRow(passengerTable, "Full Name:", ticket.getCustomerName(), normalFont);
        addTableRow(passengerTable, "Email:", ticket.getEmail(), normalFont);
        addTableRow(passengerTable, "Phone Number:", ticket.getPhoneNo(), normalFont);
        
        document.add(passengerTable);
        document.add(new Paragraph("\n"));
        
        // Event Information Section
        Paragraph eventHeader = new Paragraph("EVENT INFORMATION")
                .setFont(headerFont)
                .setFontSize(12)
                .setMarginBottom(10);
        document.add(eventHeader);
        
        Table eventTable = new Table(2);
        eventTable.setWidth(com.itextpdf.kernel.geom.PageSize.A4.getWidth() - 40);
        
        addTableRow(eventTable, "Event Name:", ticket.getEvent().getEventName(), normalFont);
        addTableRow(eventTable, "Location:", ticket.getEvent().getLocation(), normalFont);
        addTableRow(eventTable, "Total Seats:", String.valueOf(ticket.getEvent().getTotalSeats()), normalFont);
        
        document.add(eventTable);
        document.add(new Paragraph("\n"));
        
        // Singer Information Section
        Paragraph singerHeader = new Paragraph("PERFORMER INFORMATION")
                .setFont(headerFont)
                .setFontSize(12)
                .setMarginBottom(10);
        document.add(singerHeader);
        
        Table singerTable = new Table(2);
        singerTable.setWidth(com.itextpdf.kernel.geom.PageSize.A4.getWidth() - 40);
        
        addTableRow(singerTable, "Singer Name:", ticket.getSinger().getSingerName(), normalFont);
        addTableRow(singerTable, "Genre:", ticket.getSinger().getGenre(), normalFont);
        
        document.add(singerTable);
        document.add(new Paragraph("\n"));
        
        // Seat Information Section
        Paragraph seatHeader = new Paragraph("SEAT INFORMATION")
                .setFont(headerFont)
                .setFontSize(12)
                .setMarginBottom(10);
        document.add(seatHeader);
        
        Table seatTable = new Table(2);
        seatTable.setWidth(com.itextpdf.kernel.geom.PageSize.A4.getWidth() - 40);
        
        addTableRow(seatTable, "Seat Number:", String.valueOf(ticket.getSeatNumber()), normalFont);
        
        document.add(seatTable);
        document.add(new Paragraph("\n\n"));
        
        // Footer
        Paragraph footer = new Paragraph("Thank you for your booking! Please carry this ticket to the event.")
                .setFont(normalFont)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20);
        document.add(footer);
        
        Paragraph footer2 = new Paragraph("Generated on: " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .setFont(normalFont)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(footer2);
        
        document.close();
        return baos.toByteArray();
    }
    
    private void addTableRow(Table table, String label, String value, PdfFont font) {
        Cell labelCell = new Cell().add(new Paragraph(label).setFont(font).setFontSize(11)).setVerticalAlignment(VerticalAlignment.MIDDLE);
        Cell valueCell = new Cell().add(new Paragraph(value).setFont(font).setFontSize(11)).setVerticalAlignment(VerticalAlignment.MIDDLE);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}
