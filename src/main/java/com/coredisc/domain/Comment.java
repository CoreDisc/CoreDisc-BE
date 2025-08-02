package com.coredisc.domain;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.post.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    // 0 : 댓글 ,1: 대댓글
    @Column(nullable = false)
    @Builder.Default
    private Integer depth =0;

    // 연관관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Comment> replies = new ArrayList<>();


    // 댓글사용자 본인 확인 용
    public boolean isOwner(Long memberId) {
        return this.member.getId().equals(memberId);
    }

    public void addReply(Comment reply) {
        replies.add(reply);
        reply.parent = this;
        reply.depth = this.depth + 1;
    }

    public boolean hasChild() {
        return !this.replies.isEmpty();
    }

}

