package com.vilaka.mediasocial.domain.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text_post")
    private String text;

    @Column(name = "datetimepost")
    private LocalDateTime dateTimePost;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    public void createDateTimePost(){
        setDateTimePost(LocalDateTime.now());
    }
}
