package com.nagaralert.service;

import com.nagaralert.model.Alert;
import com.nagaralert.threads.AutoCleanup;
import com.nagaralert.util.DataPersister;
import com.nagaralert.util.DepartmentClassifier;
import com.nagaralert.util.InputValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

/**
 * Implementation of AlertService using various collections for efficient
 * management.
 */
@Service
public class AlertManager implements AlertService {

    // LinkedList to store all alerts maintaining insertion order
    private final LinkedList<Alert> allAlerts;

    // HashMap for fast searching by location
    private final Map<String, List<Alert>> alertsByLocation;

    // HashSet to prevent duplicate alert descriptions
    private final Set<String> uniqueDescriptions;

    private final DataPersister dataPersister;
    private final InputValidator inputValidator;
    private final DepartmentClassifier departmentClassifier; // AI Classifier
    private AutoCleanup autoCleanupThread;

    /**
     * Constructor initializing the data structures.
     */
    public AlertManager(DepartmentClassifier departmentClassifier) {
        this.allAlerts = new LinkedList<>();
        this.alertsByLocation = new HashMap<>();
        this.uniqueDescriptions = new HashSet<>();
        this.dataPersister = new DataPersister();
        this.inputValidator = new InputValidator();
        this.departmentClassifier = departmentClassifier;
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        // Load data from file
        List<Alert> loadedAlerts = dataPersister.loadData();
        if (loadedAlerts != null) {
            for (Alert alert : loadedAlerts) {
                // Determine severity again in case logic changed, or just trust loaded data
                // For now, we trust the loaded data but re-index it
                // Validate and fix data integrity issues
                if (alert.getSeverity() == null) {
                    // Try to redetect or default to LOW
                    if (alert.getDescription() != null) {
                        alert.setSeverity(
                                com.nagaralert.util.SeverityDetector.determineSeverity(alert.getDescription()));
                    } else {
                        alert.setSeverity(com.nagaralert.model.Severity.LOW);
                    }
                }

                if (alert.getTimestamp() == null) {
                    alert.setTimestamp(java.time.LocalDateTime.now());
                }

                this.allAlerts.add(alert);
                this.uniqueDescriptions.add(alert.getDescription());
                this.alertsByLocation
                        .computeIfAbsent(alert.getLocation(), k -> new ArrayList<>())
                        .add(alert);
            }
        }

        // Start background cleanup thread
        this.autoCleanupThread = new AutoCleanup(this.allAlerts);
        this.autoCleanupThread.start();
        System.out.println("AlertManager initialized: Data loaded and Cleanup thread started.");
    }

    @jakarta.annotation.PreDestroy
    public void destroy() {
        // Save data to file
        dataPersister.saveData(new ArrayList<>(allAlerts));

        // Stop the thread if possible (though daemon threads or interrupt handling is
        // better)
        if (autoCleanupThread != null && autoCleanupThread.isAlive()) {
            autoCleanupThread.interrupt();
        }
        System.out.println("AlertManager shutting down: Data saved.");
    }

    /**
     * Reports a new alert.
     * Checks for duplicate descriptions before adding.
     * Thread-safe.
     *
     * @param alert The alert to report
     * @return The reported alert, or null if it's a duplicate or invalid
     */
    @Override
    public synchronized Alert reportAlert(Alert alert) {
        if (!inputValidator.isValidAlert(alert)) {
            System.out.println("Invalid alert reported.");
            return null;
        }

        if (uniqueDescriptions.contains(alert.getDescription())) {
            System.out.println("Duplicate alert detected: " + alert.getDescription());
            return null;
        }

        // AI Logic: Detect Department
        String predictedDept = departmentClassifier.predict(alert.getDescription());
        alert.setDepartment(predictedDept);

        // Add to collections
        allAlerts.add(alert);
        uniqueDescriptions.add(alert.getDescription());

        alertsByLocation
                .computeIfAbsent(alert.getLocation(), k -> new ArrayList<>())
                .add(alert);

        System.out.println("Alert reported: " + alert.getAlertId() + " [Dept: " + predictedDept + "]");

        // Save state immediately (optional, or rely on shutdown)
        // dataPersister.saveData(new ArrayList<>(allAlerts));

        return alert;
    }

    /**
     * Verifies an alert by its ID.
     * Thread-safe.
     *
     * @param alertId The ID of the alert to verify
     * @return true if found and verified, false otherwise
     */
    @Override
    public synchronized boolean verifyAlert(String alertId) {
        if (alertId == null) {
            return false;
        }

        // O(n) search in LinkedList - reasonable for this scope
        for (Alert alert : allAlerts) {
            if (alertId.equals(alert.getAlertId())) {
                alert.setVerified(true);
                System.out.println("Alert verified: " + alertId);
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for alerts by location.
     * Thread-safe.
     *
     * @param location The location to search for
     * @return List of alerts in that location, or empty list if none found
     */
    @Override
    public synchronized List<Alert> searchByLocation(String location) {
        if (location == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(alertsByLocation.getOrDefault(location, new ArrayList<>()));
    }

    /**
     * Retrieves all alerts.
     * Thread-safe.
     *
     * @return A copy of the list of all alerts
     */
    @Override
    public synchronized List<Alert> getAllAlerts() {
        return new ArrayList<>(allAlerts);
    }

    /**
     * Retrieves alerts filtered by department.
     *
     * @param department The department to filter by
     * @return List of alerts matching the department
     */
    @Override
    public synchronized List<Alert> getAlertsByDepartment(String department) {
        if (department == null || department.equals("ALL")) {
            return getAllAlerts();
        }
        // Filter streaming
        return allAlerts.stream()
                .filter(a -> department.equalsIgnoreCase(a.getDepartment()))
                .toList(); // Java 16+
    }

    /**
     * Deletes an alert by its ID.
     * Removes the alert from all internal collections and persists the changes.
     * Thread-safe.
     *
     * @param alertId The ID of the alert to delete.
     */
    @Override
    public synchronized void deleteAlert(String alertId) {
        if (alertId == null) {
            return;
        }

        Alert alertToRemove = null;
        for (Alert alert : allAlerts) {
            if (alertId.equals(alert.getAlertId())) {
                alertToRemove = alert;
                break;
            }
        }

        if (alertToRemove != null) {
            // Remove from allAlerts
            allAlerts.remove(alertToRemove);

            // Remove from uniqueDescriptions
            uniqueDescriptions.remove(alertToRemove.getDescription());

            // Remove from alertsByLocation
            List<Alert> alertsInLocation = alertsByLocation.get(alertToRemove.getLocation());
            if (alertsInLocation != null) {
                alertsInLocation.remove(alertToRemove);
                if (alertsInLocation.isEmpty()) {
                    alertsByLocation.remove(alertToRemove.getLocation());
                }
            }

            // Persist changes immediately
            dataPersister.saveData(new ArrayList<>(allAlerts));
            System.out.println("Alert deleted and changes persisted: " + alertId);
        } else {
            System.out.println("Alert with ID " + alertId + " not found for deletion.");
        }
    }
}
