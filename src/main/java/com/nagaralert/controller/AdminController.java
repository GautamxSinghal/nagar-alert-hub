package com.nagaralert.controller;

import com.nagaralert.service.AlertService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, @RequestParam String department,
            HttpSession session) {
        // Simple authentication logic
        // In a real app, this would check against a database or secure config
        if ("admin".equals(username) && "admin123".equals(password)) {
            session.setAttribute("loggedIn", true);
            session.setAttribute("department", department);
            return "redirect:/admin";
        }
        // Redirect back to login with error and preserve the selected department so we
        // can show the form again if needed
        // For simplicity, just general error
        return "redirect:/login?error";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login"; // Changed from ?logout to clean URL
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model, HttpSession session) {
        // Security check
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }

        String department = (String) session.getAttribute("department");
        model.addAttribute("alerts", alertService.getAlertsByDepartment(department));
        model.addAttribute("deptName", department);
        return "admin";
    }

    @PostMapping("/admin/verify/{id}")
    public String verifyAlert(@PathVariable String id, @RequestParam String dept) {
        alertService.verifyAlert(id);
        return "redirect:/admin";
    }

    @PostMapping("/admin/resolve/{id}")
    public String resolveAlert(@PathVariable String id, @RequestParam String dept) {
        alertService.deleteAlert(id);
        return "redirect:/admin";
    }

}
