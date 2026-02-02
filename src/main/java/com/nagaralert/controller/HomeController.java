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
    public String reportAlert(@ModelAttribute Alert alert) {
        alert.setAlertId(UUID.randomUUID().toString());
        alert.setTimestamp(LocalDateTime.now());
        alert.setSeverity(SeverityDetector.determineSeverity(alert.getDescription()));
        alert.setVerified(false);

        alertService.reportAlert(alert);

        return "redirect:/";
    }
}
