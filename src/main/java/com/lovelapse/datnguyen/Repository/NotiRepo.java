package com.lovelapse.datnguyen.Repository;

import com.lovelapse.datnguyen.DTO.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotiRepo extends JpaRepository<NotificationModel, Long> {
}
