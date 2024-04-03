package com.vilaka.mediasocial.domain.repositories;

import com.vilaka.mediasocial.domain.entities.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheRepository<Post> {
}
