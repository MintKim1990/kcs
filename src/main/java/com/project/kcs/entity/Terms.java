package com.project.kcs.entity;

import com.project.kcs.entity.base.BaseEntity;
import com.project.kcs.entity.constant.TermsStatus;
import com.project.kcs.entity.constant.TermsType;
import com.project.kcs.request.terms.RegistrationTermsRequest;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "TERMS", uniqueConstraints = {
        @UniqueConstraint(name = "TERMS_TYPE_UNIQUE", columnNames = "TERMS_TYPE")
})
public class Terms extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TERMS_KEY")
    private Long key;

    @Enumerated(EnumType.STRING)
    @Column(name = "TERMS_TYPE")
    private TermsType type;

    @Column(name = "TERMS_TITLE", nullable = false)
    private String title;

    @Column(name = "TERMS_CONTENT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "TEMRS_STATUS")
    private TermsStatus status;

    @Builder
    public Terms(TermsType type, String title, String content, TermsStatus status) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    public boolean available() {
        return this.status.equals(TermsStatus.USE);
    }

    public static Terms setTerms(RegistrationTermsRequest registrationTermsRequest) {
        return Terms.builder()
                .type(registrationTermsRequest.getType())
                .title(registrationTermsRequest.getTitle())
                .content(registrationTermsRequest.getContent())
                .status(TermsStatus.USE)
                .build();
    }

    public void modifyTitle(String title) {
        setTitle(title);
    }

    public void modifyContent(String content) {
        setContent(content);
    }

    public void delete() {
        setStatus(TermsStatus.DELETE);
    }


}
