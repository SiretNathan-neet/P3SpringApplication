package com.p3springboot.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "RENTALS")
@Data
@NoArgsConstructor
public class RentalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private Integer surface;
    private Integer price;
    private String picture;

    @Column(length = 2000)
    private String description;

    private Integer ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
