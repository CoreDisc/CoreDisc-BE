package com.coredisc.infrastructure.repository.post;


import com.coredisc.domain.member.QMember;
import com.coredisc.domain.post.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface JpaPostRepository extends JpaRepository<Post, Long> {

}




