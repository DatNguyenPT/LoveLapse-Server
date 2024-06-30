package com.lovelapse.datnguyen.Repository;

import com.lovelapse.datnguyen.DTO.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotiRepo extends JpaRepository<NotificationModel, Long> {
}
