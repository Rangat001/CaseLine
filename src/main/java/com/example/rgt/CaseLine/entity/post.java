package com.example.rgt.CaseLine.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "posts")
public class post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer post_id;

    @Column(nullable = false)
    private Integer case_id;

    @Column(nullable = false)
    private Integer posted_by;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(length = 300)
    private String source_url;

    @Column(length = 300)
    private String media_url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    // Enum for status field
    public enum Status {
        draft,
        published,
        pending
    }
}
