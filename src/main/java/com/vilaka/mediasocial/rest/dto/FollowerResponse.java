package com.vilaka.mediasocial.rest.dto;

import com.vilaka.mediasocial.domain.entities.Follower;
import lombok.Data;

@Data
public class FollowerResponse {
    private Long id;
    private String name;

    public FollowerResponse() {
    }

    public FollowerResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public FollowerResponse(Follower entity){
        id = entity.getId();
        name = entity.getFollower().getName();
    }

}
