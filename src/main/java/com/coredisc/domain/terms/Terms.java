package com.coredisc.domain.terms;

import com.coredisc.domain.common.BaseEntity;
import com.coredisc.domain.common.enums.TermsType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "terms")
@Check(constraints = "version >= 100")
public class Terms extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(100)")
    private TermsType type;

    @Lob
    @Column(nullable = false, length = 256)
    private String content;

    @Column(nullable = false) // 100 == 1, 201 == 2.1
    @Min(100)
    private Integer version;

    @ColumnDefault("1")
    @Column(nullable = false)
    private Boolean isRequired;
}
