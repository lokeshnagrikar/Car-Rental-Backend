package com.carrental.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;

    @Value("${spring.mail.username:noreply@carrental.com}")
    private String fromEmail;

    @Async
    public void sendPasswordResetEmail(String to, String token) {
        if (!emailEnabled) {
            log.info("Email sending is disabled. Would have sent password reset email to: {}", to);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Password Reset Request");

            String content = getString(token);

            helper.setText(content, true);

            mailSender.send(message);
            log.info("Password reset email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to: {}", to, e);
            // Don't throw the exception to prevent disrupting the application flow
        }
    }

    private static String getString(String token) {
        String resetUrl = "http://localhost:3000/reset-password?token=" + token;
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + resetUrl + "\">Reset Password</a></p>"
                + "<p>If you did not request a password reset, please ignore this email.</p>"
                + "<p>Regards,<br>Car Rental Team</p>";
        return content;
    }

    @Async
    public void sendBookingConfirmationEmail(String to, String bookingDetails) {
        if (!emailEnabled) {
            log.info("Email sending is disabled. Would have sent booking confirmation email to: {}", to);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Booking Confirmation");

            String content = "<p>Hello,</p>"
                    + "<p>Your booking has been confirmed.</p>"
                    + "<p>Booking Details:</p>"
                    + "<p>" + bookingDetails + "</p>"
                    + "<p>Thank you for choosing our service.</p>"
                    + "<p>Regards,<br>Car Rental Team</p>";

            helper.setText(content, true);

            mailSender.send(message);
            log.info("Booking confirmation email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send booking confirmation email to: {}", to, e);
            // Don't throw the exception to prevent disrupting the application flow
        }
    }

    @Async
    public void sendPaymentConfirmationEmail(String to, String paymentDetails) {
        if (!emailEnabled) {
            log.info("Email sending is disabled. Would have sent payment confirmation email to: {}", to);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Payment Confirmation");

            String content = "<p>Hello,</p>"
                    + "<p>Your payment has been processed successfully.</p>"
                    + "<p>Payment Details:</p>"
                    + "<p>" + paymentDetails + "</p>"
                    + "<p>Thank you for your payment.</p>"
                    + "<p>Regards,<br>Car Rental Team</p>";

            helper.setText(content, true);

            mailSender.send(message);
            log.info("Payment confirmation email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send payment confirmation email to: {}", to, e);
            // Don't throw the exception to prevent disrupting the application flow
        }
    }

    @Async
    public void sendWelcomeEmail(String to, String name) {
        if (!emailEnabled) {
            log.info("Email sending is disabled. Would have sent welcome email to: {}", to);
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("Welcome to Car Rental Service");

            String content = "<p>Hello " + name + ",</p>"
                    + "<p>Welcome to our Car Rental Service!</p>"
                    + "<p>Thank you for registering with us. We're excited to have you as a member.</p>"
                    + "<p>You can now browse our selection of cars and make bookings through our platform.</p>"
                    + "<p>If you have any questions or need assistance, please don't hesitate to contact our support team.</p>"
                    + "<p>Regards,<br>Car Rental Team</p>";

            helper.setText(content, true);

            mailSender.send(message);
            log.info("Welcome email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send welcome email to: {}", to, e);
            // Don't throw the exception to prevent disrupting the application flow
        }
    }
}
