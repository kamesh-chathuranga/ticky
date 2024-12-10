package com.ticky.backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ticky.backend.dto.response.Response;
import com.ticky.backend.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_user")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Response {
    @Id
    @GeneratedValue
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String contactNumber;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

}
