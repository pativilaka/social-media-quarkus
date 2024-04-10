package com.vilaka.mediasocial.rest;


import com.vilaka.mediasocial.domain.entities.Post;
import com.vilaka.mediasocial.domain.entities.User;
import com.vilaka.mediasocial.domain.repositories.FollowerRepository;
import com.vilaka.mediasocial.domain.repositories.PostRepository;
import com.vilaka.mediasocial.domain.repositories.UserRepository;
import com.vilaka.mediasocial.rest.dto.PostRequest;
import com.vilaka.mediasocial.rest.dto.PostResponse;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResources {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private FollowerRepository followerRepository;

    @Inject
    public PostResources(UserRepository userRepository, PostRepository postRepository,
                         FollowerRepository followerRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followerRepository = followerRepository;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, PostRequest dto){

        User user = userRepository.findById(userId);

        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Post post = new Post();
        post.setText(dto.getText());
        post.setUser(user);
        postRepository.persist(post);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId,
                              @HeaderParam("followerId") Long followerId){

        User user = userRepository.findById(userId);
        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (followerId == null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("FollowId is requerid in header.")
                    .build();
        }

        User follower = userRepository.findById(followerId);
        if (follower == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        boolean follows = followerRepository.follows(follower, user);
        if(!follows){
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("You must to follow this user to see his post!")
                    .build();
        }

        PanacheQuery<Post> query = postRepository
                .find("user", Sort.by("dateTimePost", Sort.Direction.Descending), user);
        List<Post> listPost = query.list();

        List<PostResponse> listPostResponse = listPost.stream()
                //.map(lp -> PostResponse.responseFromEntity(lp))
                .map(PostResponse::responseFromEntity)
                .collect(Collectors.toList());

        return Response.ok(listPostResponse).build();
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public Response deletePost(@PathParam("id") Long id){
        Post post = postRepository.findById(id);

        if(post == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        postRepository.delete(post);

        return Response.noContent().build();}
}
