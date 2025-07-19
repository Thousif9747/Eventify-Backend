package com.example.eventify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Booking> addBooking(@RequestBody Booking booking) {
        Booking saved = bookingRepository.save(booking);
        if (mailSender != null && booking.getEmail() != null && !booking.getEmail().isEmpty()) {
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
                String htmlMsg = "<h2>Booking Confirmation</h2>"
                    + "<p>Dear <b>" + booking.getName() + "</b>,</p>"
                    + "<p>Thank you for booking <b>" + booking.getEventName() + "</b>!</p>"
                    + "<ul>"
                    + "<li><b>Date:</b> " + booking.getEventDate() + "</li>"
                    + "<li><b>Venue:</b> " + booking.getEventVenue() + "</li>"
                    + "<li><b>Tickets:</b> " + booking.getTickets() + "</li>"
                    + "</ul>"
                    + "<p>We look forward to seeing you at the event!</p>"
                    + "<br><p>Best regards,<br>Eventify Team</p>";
                helper.setText(htmlMsg, true);
                helper.setTo(booking.getEmail());
                helper.setSubject("Booking Confirmation - " + booking.getEventName());
                mailSender.send(mimeMessage);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        if (booking.getPhone() != null && !booking.getPhone().isEmpty()) {
            String waMsg = "Dear " + booking.getName() + ", your booking for '" + booking.getEventName() + "' on " + booking.getEventDate() + " at " + booking.getEventVenue() + " is confirmed! Tickets: " + booking.getTickets() + ". - Eventify";
            try {
                WhatsAppSender.sendWhatsAppMessageToNumber(booking.getPhone(), waMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.ok(saved);
    }
}