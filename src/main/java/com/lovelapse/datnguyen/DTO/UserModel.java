package com.lovelapse.datnguyen.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usermod")
public class UserModel {
    @Id
    @Column(name = "number")
    private String number;

    @Column(name = "gmail", nullable = false, unique = true)
    private String gmail;

    @Column(name = "password", nullable = false)
    private String password; // hashed password, not actual password

    @Column(name = "dob")
    private String dob; // Consider using LocalDate instead for better handling

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "gender")
    private String gender;

    @Column(name = "star")
    private String star;

    @Column(name = "image")
    private String image;

    @Column(name = "age")
    private Integer age;

    @Column(name = "introduction", columnDefinition = "text")
    private String introduction;
}
