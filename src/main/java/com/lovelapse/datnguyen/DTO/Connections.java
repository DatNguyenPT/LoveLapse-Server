package com.lovelapse.datnguyen.DTO;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name="connections")
public class Connections {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

    @Column(name="fromuser")
    private String from;

    @Column(name="touser")
    private String to;

    @Column(name = "replied")
    private boolean replied;

    @Column(name = "daysendorreceive")
    private String daysendorreceive;
}
