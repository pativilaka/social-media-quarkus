package com.vilaka.mediasocial.rest.dto;

import com.vilaka.mediasocial.domain.entities.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {

    private String text;
    private LocalDateTime deteTime;

    public static PostResponse responseFromEntity(Post post){

        PostResponse postResponse = new PostResponse();
        postResponse.setText(post.getText());
        postResponse.setDeteTime(post.getDateTimePost());

        return postResponse;
    }

}
