package com.nagaralert.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents an alert in the Nagar Alert Hub system.
 * Implements Serializable for object serialization.
 */
public class Alert implements Serializable {
    private static final long serialVersionUID = 1L;

    private String alertId;
    private String description;
    private String location;
    private Severity severity;
    private String department;
    private LocalDateTime timestamp;
    private boolean isVerified;

    /**
     * Default constructor.
     */
    public Alert() {
    }

    /**
     * Parameterized constructor.
     *
     * @param alertId     Unique identifier for the alert
     * @param description Brief description of the alert
     * @param location    Location where the alert occurred
     * @param severity    Severity level of the alert
     * @param department  Department handling the alert
     * @param timestamp   Time when the alert occurred
     * @param isVerified  Verification status of the alert
     */
    public Alert(String alertId, String description, String location, Severity severity, String department,
            LocalDateTime timestamp, boolean isVerified) {
        this.alertId = alertId;
        this.description = description;
        this.location = location;
        this.severity = severity;
        this.department = department;
        this.timestamp = timestamp;
        this.isVerified = isVerified;
    }

    /**
     * Gets the alert ID.
     *
     * @return the alert ID
     */
    public String getAlertId() {
        return alertId;
    }

    /**
     * Sets the alert ID.
     *
     * @param alertId the alert ID to set
     */
    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the severity.
     *
     * @return the severity
     */
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Sets the severity.
     *
     * @param severity the severity to set
     */
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    /**
     * Gets the department.
     *
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department.
     *
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * Gets the timestamp.
     *
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp.
     *
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Checks if the alert is verified.
     *
     * @return true if verified, false otherwise
     */
    public boolean isVerified() {
        return isVerified;
    }

    /**
     * Sets the verification status.
     *
     * @param verified the verification status to set
     */
    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "alertId='" + alertId + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", severity=" + severity +
                ", department='" + department + '\'' +
                ", timestamp=" + timestamp +
                ", isVerified=" + isVerified +
                '}';
    }
}
