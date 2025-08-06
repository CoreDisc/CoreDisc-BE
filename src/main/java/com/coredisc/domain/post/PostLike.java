package com.coredisc.domain.post;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "post_like",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_post_member",
                columnNames = {"post_id", "member_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public static PostLike create(Post post, Member member) {
        return PostLike.builder()
                .post(post)
                .member(member)
                .build();
    }


}