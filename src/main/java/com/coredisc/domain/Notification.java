package com.coredisc.domain;

import com.coredisc.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // TODO: 연관관계 설정, 엔티티 정의하고 todo 제거해주세요.
    // - @ManyToOne Member
    // - @ManyToOne NotificationType
}
