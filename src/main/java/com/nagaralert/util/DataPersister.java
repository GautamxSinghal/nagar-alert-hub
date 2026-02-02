package com.nagaralert.util;

import com.nagaralert.model.Alert;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for persisting data using Serialization.
 */
public class DataPersister {

    private static final String FILE_NAME = "alerts.ser";

    /**
     * Saves the list of alerts to a file.
     *
     * @param alerts The list of alerts to save
     */
    public void saveData(List<Alert> alerts) {
        try (FileOutputStream fos = new FileOutputStream(FILE_NAME);
                ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(alerts);
            System.out.println("Data saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Loads the list of alerts from a file.
     *
     * @return The list of alerts, or an empty list if file not found or error
     *         occurs
     */
    @SuppressWarnings("unchecked")
    public List<Alert> loadData() {
        List<Alert> alerts = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_NAME);
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            alerts = (List<Alert>) ois.readObject();
            System.out.println("Data loaded successfully from " + FILE_NAME);
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found. Starting with empty list.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
        return alerts;
    }
}
