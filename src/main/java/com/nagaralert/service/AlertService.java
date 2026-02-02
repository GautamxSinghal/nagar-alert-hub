package com.nagaralert.service;

import com.nagaralert.model.Alert;
import java.util.List;

/**
 * Interface for alert management services.
 */
public interface AlertService {
    /**
     * Reports a new alert.
     *
     * @param alert The alert to report
     * @return The reported Alert object, or null if duplicate/invalid
     */
    Alert reportAlert(Alert alert);

    /**
     * Verifies an alert.
     *
     * @param alertId The ID of the alert to verify
     * @return true if verification was successful, false otherwise
     */
    boolean verifyAlert(String alertId);

    /**
     * Searches for alerts by location.
     *
     * @param location The location to search for
     * @return A list of alerts at the specified location
     */
    List<Alert> searchByLocation(String location);

    /**
     * Retrieves all alerts.
     *
     * @return A list of all alerts
     */
    List<Alert> getAllAlerts();

    /**
     * Retrieves alerts by department.
     *
     * @param department The department name
     * @return List of alerts for the department
     */

    List<Alert> getAlertsByDepartment(String department);

    void deleteAlert(String alertId);
}
