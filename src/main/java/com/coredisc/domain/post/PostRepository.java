package com.coredisc.domain.post;

import com.coredisc.domain.common.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

public interface PostRepository {
    //기본 CRUD
    Post save(Post post);
    Optional<Post> findById(Long id);
    void delete(Post post);
    void deleteById(Long id);

}
