package com.nagaralert.threads;

import com.nagaralert.model.Alert;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Background thread to automatically clean up alerts older than 24 hours.
 */
public class AutoCleanup extends Thread {

    private final LinkedList<Alert> alerts;

    /**
     * Constructor.
     *
     * @param alerts The list of alerts to monitor and clean
     */
    public AutoCleanup(LinkedList<Alert> alerts) {
        this.alerts = alerts;
    }

    /**
     * continuously checks for old alerts and removes them.
     * Sleeps for 2 minutes between checks.
     */
    @Override
    public void run() {
        while (true) {
            try {
                // Sleep for 2 minutes
                Thread.sleep(2 * 60 * 1000);

                System.out.println("AutoCleanup: Starting cleanup process...");

                synchronized (alerts) {
                    LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24);
                    Iterator<Alert> iterator = alerts.iterator();
                    int removedCount = 0;

                    while (iterator.hasNext()) {
                        Alert alert = iterator.next();
                        if (alert.getTimestamp() != null && alert.getTimestamp().isBefore(cutoffTime)) {
                            iterator.remove();
                            System.out.println("AutoCleanup: Removed old alert with ID: " + alert.getAlertId());
                            removedCount++;
                        }
                    }
                    if (removedCount > 0) {
                        System.out.println("AutoCleanup: Cleanup finished. Total removed: " + removedCount);
                    } else {
                        System.out.println("AutoCleanup: No alerts older than 24 hours found.");
                    }
                }

            } catch (InterruptedException e) {
                System.err.println("AutoCleanup thread interrupted: " + e.getMessage());
                // Restore interrupted status and break the loop to stop the thread gracefully
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("AutoCleanup thread encountered an error: " + e.getMessage());
            }
        }
    }
}
