package com.coredisc.presentation.dto.comment;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequestDTO {

    private String content;

}
