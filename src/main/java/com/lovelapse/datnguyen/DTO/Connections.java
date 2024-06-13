package com.lovelapse.datnguyen.DTO;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "connections")
public class Connections {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "fromUser")
    private String fromUser;

    @Column(name = "toUser")
    private String toUser;

    @Column(name = "replied")
    private boolean replied;

    @Column(name = "daysendorreceive")
    private String daySendOrReceive;
}
