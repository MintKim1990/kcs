package com.project.kcs.entity;

import com.project.kcs.entity.base.BaseEntity;
import com.project.kcs.entity.constant.TermsAgreeType;
import com.project.kcs.request.userterms.InsertUserTermsRequest;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "USERTERMS", uniqueConstraints = {
        @UniqueConstraint(name = "USERTERMS_UNIQUE", columnNames = {"USER_KEY", "TERMS_KEY"})
})
public class UserTerms extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_TEMRS_KEY")
    private Long key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_KEY")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TERMS_KEY")
    private Terms terms;

    @Column(name = "AVAILABLE_TIME")
    private LocalDateTime availableTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "TERMS_AGREE", nullable = false)
    private TermsAgreeType termsAgreeType;

    @Builder
    public UserTerms(User user, Terms terms, LocalDateTime availableTime, TermsAgreeType termsAgreeType) {
        this.user = user;
        this.terms = terms;
        this.availableTime = availableTime;
        this.termsAgreeType = termsAgreeType;
    }

    public static UserTerms setUserTerms(InsertUserTermsRequest insertUserTermsRequest, User user, Terms terms) {
        return UserTerms.builder()
                .user(user)
                .terms(terms)
                .availableTime(LocalDateTime.now().plusYears(1L))
                .termsAgreeType(insertUserTermsRequest.getTermsAgreeType())
                .build();
    }

    public void modifyAvailableTime(LocalDateTime availableTime) {
        setAvailableTime(availableTime);
    }

    public void modifyTermsAgreeType(TermsAgreeType termsAgreeType) {
        setTermsAgreeType(termsAgreeType);
    }
}
