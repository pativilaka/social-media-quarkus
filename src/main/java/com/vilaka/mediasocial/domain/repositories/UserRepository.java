package com.vilaka.mediasocial.domain.repositories;


import com.vilaka.mediasocial.domain.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
}
