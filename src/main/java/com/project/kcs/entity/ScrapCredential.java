package com.project.kcs.entity;

import com.project.kcs.entity.constant.CredentialType;
import com.project.kcs.request.scrapcredential.InsertScrapCredentialRequest;
import com.project.kcs.utils.PasswordUtil;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "SCRAPCREDENTAIL", uniqueConstraints = {
        @UniqueConstraint(name = "SCRAPCREDENTAIL_SCRAP_CREDENTAIL_TYPE_UNIQUE", columnNames = "SCRAP_CREDENTAIL_TYPE")
})
public class ScrapCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCRAP_CREDENTAIL_KEY")
    private Long key;

    @Enumerated(EnumType.STRING)
    @Column(name = "SCRAP_CREDENTAIL_TYPE", nullable = false)
    private CredentialType type;

    @Column(name = "SCRAP_CREDENTAIL_ID", nullable = false)
    private String id;

    @Column(name = "SCRAP_CREDENTAIL_PASSWORD", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_KEY")
    private User user;

    @Builder
    public ScrapCredential(CredentialType type, String id, String password, User user) {
        this.type = type;
        this.id = id;
        this.password = password;
        this.user = user;
    }

    public static ScrapCredential setScrapCredential(InsertScrapCredentialRequest insertScrapCredentialRequest, User user) {
        return ScrapCredential.builder()
                .type(insertScrapCredentialRequest.getType())
                .id(insertScrapCredentialRequest.getId())
                .password(PasswordUtil.convertEncoding(insertScrapCredentialRequest.getPassword()))
                .user(user)
                .build();
    }

    public void modifyId(String id) {
        setId(id);
    }

    public void modifyPassword(String password) {
        setPassword(password);
    }
}
