package com.vilaka.mediasocial.rest;

import com.vilaka.mediasocial.domain.User;
import com.vilaka.mediasocial.domain.repositories.UserRepository;
import com.vilaka.mediasocial.rest.dto.UserRequest;
import com.vilaka.mediasocial.rest.dto.errors.ResponseError;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserRepository repository;
    private Validator validator;

    @Inject
    public UserResource(UserRepository repository, Validator validator){
        this.repository = repository;
        this.validator = validator;
    }
    @POST
    @Transactional
    public Response createUser(UserRequest dto){

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(dto);

        if(!violations.isEmpty()){
            return ResponseError.createErrorFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
        }

        User user = new User();
        user.setAge(dto.getAge());
        user.setName(dto.getName());
        repository.persist(user);

        return Response
                .status(Response.Status.CREATED.getStatusCode())
                .entity(user)
                .build();
    }

    @GET
    public Response listUsers(){
        PanacheQuery<User> query = repository.findAll();
        return Response.ok(query.list()).build();
    }

    @GET
    @Path("{id}")
    public Response userById(@PathParam("id") Long id){

        User user = repository.findById(id);

        if(user != null){
            return Response.ok().entity(user).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, UserRequest dto){

        User user = repository.findById(id);

        if (user != null) {
            user.setAge(dto.getAge());
            user.setName(dto.getName());

            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id){

        User user = repository.findById(id);

        if (user != null){
            repository.delete(user);
            return Response.noContent().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }



}
