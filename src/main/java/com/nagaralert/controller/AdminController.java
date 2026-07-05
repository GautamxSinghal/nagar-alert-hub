package com.nagaralert.controller;

import com.nagaralert.model.AlertStatus;
import com.nagaralert.service.AlertService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagaralert.model.Alert;

@Controller
public class AdminController {

    private final AlertService alertService;

    public AdminController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Spring Security will handle the POST /login and POST /logout requests

    @GetMapping("/admin")
    public String adminDashboard(Model model, @RequestParam(required = false, defaultValue = "ALL") String dept) {
        model.addAttribute("alerts", alertService.getAlertsByDepartment(dept));
        model.addAttribute("deptName", dept);
        return "admin";
    }

    @GetMapping("/admin/dashboard")
    public String analyticsDashboard(Model model, @RequestParam(required = false, defaultValue = "ALL") String dept) {
        List<Alert> allAlerts = alertService.getAlertsByDepartment(dept);
        long totalAlerts = allAlerts.size();
        
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        long alertsToday = allAlerts.stream()
            .filter(a -> a.getTimestamp() != null && a.getTimestamp().isAfter(startOfDay))
            .count();
            
        long resolvedAlerts = allAlerts.stream()
            .filter(a -> a.getStatus() == AlertStatus.RESOLVED)
            .count();
        double resolutionRate = totalAlerts > 0 ? (double) resolvedAlerts / totalAlerts * 100 : 0.0;
        
        Map<String, Long> byDept = allAlerts.stream()
            .filter(a -> a.getDepartment() != null)
            .collect(Collectors.groupingBy(Alert::getDepartment, Collectors.counting()));
            
        Map<String, Long> bySeverity = allAlerts.stream()
            .filter(a -> a.getSeverity() != null)
            .collect(Collectors.groupingBy(a -> a.getSeverity().name(), Collectors.counting()));
            
        model.addAttribute("totalAlerts", totalAlerts);
        model.addAttribute("alertsToday", alertsToday);
        model.addAttribute("resolutionRate", Math.round(resolutionRate));
        model.addAttribute("deptName", dept);
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            model.addAttribute("deptDataJson", mapper.writeValueAsString(byDept));
            model.addAttribute("severityDataJson", mapper.writeValueAsString(bySeverity));
        } catch (Exception e) {
            model.addAttribute("deptDataJson", "{}");
            model.addAttribute("severityDataJson", "{}");
        }
        
        return "dashboard";
    }

    @PostMapping("/admin/verify/{id}")
    public String verifyAlert(@PathVariable String id, @RequestParam(required = false) String dept) {
        alertService.updateStatus(id, AlertStatus.IN_PROGRESS);
        return "redirect:/admin" + (dept != null ? "?dept=" + dept : "");
    }

    @PostMapping("/admin/resolve/{id}")
    public String resolveAlert(@PathVariable String id, @RequestParam(required = false) String dept) {
        alertService.updateStatus(id, AlertStatus.RESOLVED);
        return "redirect:/admin" + (dept != null ? "?dept=" + dept : "");
    }

}
