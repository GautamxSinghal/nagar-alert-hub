package com.nagaralert.controller;

import com.nagaralert.model.Alert;
import com.nagaralert.model.Severity;
import com.nagaralert.service.AlertService;
import com.nagaralert.util.SeverityDetector;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
public class HomeController {

    private final AlertService alertService;

    public HomeController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Alert> alerts = alertService.getAllAlerts();
        long criticalCount = alerts.stream()
                .filter(a -> a.getSeverity() == Severity.CRITICAL)
                .count();

        model.addAttribute("alerts", alerts);
        model.addAttribute("criticalCount", criticalCount);
        return "index";
    }

    @PostMapping("/report")
    public String reportAlert(@ModelAttribute Alert alert, @RequestParam(value = "image", required = false) MultipartFile image) {
        alert.setAlertId(UUID.randomUUID().toString());
        alert.setTimestamp(LocalDateTime.now());
        alert.setSeverity(SeverityDetector.determineSeverity(alert.getDescription()));

        if (image != null && !image.isEmpty()) {
            try {
                Path uploadDir = Paths.get("uploads");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                String filename = alert.getAlertId() + "_" + image.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
                Path filePath = uploadDir.resolve(filename);
                Files.copy(image.getInputStream(), filePath);
                alert.setImageUrl("/uploads/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        alertService.reportAlert(alert);

        return "redirect:/";
    }

    @PostMapping("/alert/{id}/upvote")
    public String upvoteAlert(@org.springframework.web.bind.annotation.PathVariable String id) {
        alertService.upvoteAlert(id);
        return "redirect:/";
    }

    @GetMapping("/my-alerts")
    public String myAlerts(@RequestParam(required = false) String phone, Model model) {
        if (phone != null && !phone.isBlank()) {
            List<Alert> myAlerts = alertService.getAlertsByPhoneNumber(phone);
            model.addAttribute("alerts", myAlerts);
            model.addAttribute("phone", phone);
        }
        return "my-alerts";
    }
}
