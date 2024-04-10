package com.vilaka.mediasocial.rest;


import com.vilaka.mediasocial.domain.entities.Follower;
import com.vilaka.mediasocial.domain.entities.User;
import com.vilaka.mediasocial.domain.repositories.FollowerRepository;
import com.vilaka.mediasocial.domain.repositories.UserRepository;
import com.vilaka.mediasocial.rest.dto.FollowerRequest;
import com.vilaka.mediasocial.rest.dto.FollowerResponse;
import com.vilaka.mediasocial.rest.dto.FollowersPerUserResponse;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/followers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FollowerResources {
    private FollowerRepository followerRepository;
    private UserRepository userRepository;

    @Inject
    public FollowerResources(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    @PUT
    @Transactional
    public Response followUser(@PathParam("userId") Long userId, FollowerRequest followerId){

        if (userId.equals(followerId.getFollowId())){
            return Response.status(Response.Status.CONFLICT)
                    .entity("You can't follow yourself!")
                    .build();
        }

        User user = userRepository.findById(userId);

        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User follower = userRepository.findById(followerId.getFollowId());

        if(!followerRepository.follows(follower, user)){
            Follower entityFollower = new Follower();
            entityFollower.setUser(user);
            entityFollower.setFollower(follower);
            followerRepository.persist(entityFollower);
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    public Response listFollowers(@PathParam("userId") Long userId){

        User user = userRepository.findById(userId);
        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Follower> list = followerRepository.findByUser(userId);
        FollowersPerUserResponse resultPerUser = new FollowersPerUserResponse();
        resultPerUser.setFollowersCount(list.size());

        List<FollowerResponse> resultMap = list.stream()
                .map(FollowerResponse::new)
                .collect(Collectors.toList());

        resultPerUser.setContent(resultMap);

        return Response.ok(resultPerUser).build();
    }

    @DELETE
    @Transactional
    public Response unfollUser(@PathParam("userId") Long userId,
                               @QueryParam("followerId") Long followerId){

        if (userId == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        followerRepository.deleteByFollowerAndUser(followerId, userId);

        return Response.status(Response.Status.CREATED).build();

    }

}
