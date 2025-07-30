package com.coredisc.common.converter;

import com.coredisc.domain.Comment;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import com.coredisc.presentation.dto.comment.CommentResponseDTO;

public class CommentConverter {

    public static Comment toComment(String comment , Post post , Member member) {
        return Comment.builder()
                .content(comment)
                .post(post)
                .member(member)
                .depth(0) // 일반 댓글
                .build();
    }

    public static CommentResponseDTO.MemberInfo toMemberInfo(Member member) {
        return CommentResponseDTO.MemberInfo.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .profileImg(member.getProfileImg().getImgUrl())
                .build();
    }

    public static CommentResponseDTO.CommentCreateResponse toCreateResponse(Comment comment) {
        return CommentResponseDTO.CommentCreateResponse.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .depth(comment.getDepth())
                .member(CommentConverter.toMemberInfo(comment.getMember()))
                .build();
    }

}
