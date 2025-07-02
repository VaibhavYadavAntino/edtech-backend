package com.edtech.entity;
import com.edtech.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String name;
 
 @Column(unique = true, nullable = false)
 private String email;
 private String phoneNumber;
 private String password;

 @Enumerated(EnumType.STRING)
 private UserRole role;
}
