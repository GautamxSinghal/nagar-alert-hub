package com.nagaralert.repository;

import com.nagaralert.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, String> {
    List<Alert> findByLocation(String location);
    List<Alert> findByDepartment(String department);
    List<Alert> findByTimestampBefore(LocalDateTime timestamp);
    List<Alert> findByPhoneNumber(String phoneNumber);
    boolean existsByDescription(String description);
}
