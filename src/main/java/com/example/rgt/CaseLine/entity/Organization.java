package com.example.rgt.CaseLine.entity;

import com.example.rgt.CaseLine.Enum.OrgType;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer org_id;

    @Column(nullable = false, length = 100)
    private String password;  // 🔐 for login purpose

    @Column(nullable = false, length = 25)
    private String owner_name;

    @Column(nullable = false, length = 25)
    private String org_name;

    @Column(nullable = false, length = 25)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrgType org_type;

    @Column(length = 1500)
    private String description;

    @Column(nullable = true)
    private LocalDateTime created_at;
}
