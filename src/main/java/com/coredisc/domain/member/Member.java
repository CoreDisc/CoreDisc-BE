package com.coredisc.domain.member;

import com.coredisc.domain.Comment;
import com.coredisc.domain.PostLike;
import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.common.enums.OauthType;
import com.coredisc.domain.common.enums.Role;
import com.coredisc.domain.follow.Follow;
import com.coredisc.domain.mapping.MemberTerms;
import com.coredisc.domain.monthlyReport.MonthlyReport;
import com.coredisc.domain.post.Post;
import com.coredisc.domain.profileImg.ProfileImg;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Setter
    @Column(length = 16, nullable = false, unique = true)
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Setter
    @Column(length = 16, nullable = false, unique = true)
    private String nickname;

    @Column(length = 16, nullable = false)
    private String name;

    @Setter
    @ColumnDefault("1")
    @Column(nullable = false)
    private Boolean status;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private Role role;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Boolean isSocialLogin;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private OauthType oauthType;

    private String oauthKey;


    // 연관관계 매핑
    @Setter
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileImg profileImg;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<PostLike> likes = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<MemberTerms> memberTermsList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonthlyReport> monthlyReportList = new ArrayList<>();

    // 이 사용자가 팔로우하는 다른 사용자들과의 관계 목록
    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    private List<Follow> followSentList = new ArrayList<>();

    // 이 사용자를 팔로우하는 다른 사용자들과의 관계 목록
    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    private List<Follow> followReceivedList = new ArrayList<>();


    // 메서드
    public void encodePassword(String password) {
        this.password = password;
    }
}
