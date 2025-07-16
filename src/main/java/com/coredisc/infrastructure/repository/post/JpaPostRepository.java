package com.coredisc.infrastructure.repository.post;

import com.coredisc.domain.common.enums.PostStatus;
import com.coredisc.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface JpaPostRepository extends JpaRepository<Post,Long> {


}

