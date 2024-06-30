package com.lovelapse.datnguyen.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "Notifications")
public class NotificationModel {
    @Id
    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;
}
