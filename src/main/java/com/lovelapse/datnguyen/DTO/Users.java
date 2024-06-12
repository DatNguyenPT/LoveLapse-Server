package com.lovelapse.datnguyen.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private String username;
    @Column(name = "")
    private List<Files>files;
}
