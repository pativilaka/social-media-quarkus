package com.vilaka.mediasocial.domain.repositories;

import com.vilaka.mediasocial.domain.User;
import com.vilaka.mediasocial.domain.entities.Follower;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepository<Follower> {
    public boolean follows(User follower, User user){

//        Map<String, Object>  params = new HashMap<>();
//        params.put("user", user);
//        params.put("follower", follower);

        Map<String, Object> params = Parameters
                .with("follower", follower)
                .and("user", user)
                .map();
        PanacheQuery<Follower> query = find("follower = :follower and user = :user ", params);
        Optional<Follower> result = query.firstResultOptional();

        return result.isPresent();

    }

    public List<Follower> findByUser(Long userId){
        PanacheQuery<Follower> query = find("user.id", userId);
        return query.list();
    }

    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        Map<String, Object> param = Parameters
                .with("followerId", followerId)
                .and("userId", userId)
                .map();

        delete("follower.id = :followerId and user.id = :userId", param);
    }

}
