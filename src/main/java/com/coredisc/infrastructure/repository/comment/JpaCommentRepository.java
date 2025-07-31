package com.coredisc.infrastructure.repository.comment;

import com.coredisc.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCommentRepository extends JpaRepository<Comment,Long> {

    // 게시글의 전체 댓글 수
    Long countByPostId(Long postId);

    // 특정 댓글의 대댓글 수
    Long countByParentId(Long parentId);

}
