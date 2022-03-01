package com.project.kcs.entity;

import com.project.kcs.entity.base.BaseEntity;
import com.project.kcs.entity.constant.CertificationType;
import com.project.kcs.entity.constant.UserStatus;
import com.project.kcs.request.user.InsertUserRequestForHptel;
import com.project.kcs.request.user.InsertUserRequestForKakao;
import com.project.kcs.request.user.ModifyUserRequest;
import com.project.kcs.utils.GenderUtil;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "User", uniqueConstraints = {
        @UniqueConstraint(name = "USER_ID_UNIQUE", columnNames = "USER_ID"),
        @UniqueConstraint(name = "USER_IDENTIFICATION_INFO_UNIQUE", columnNames = "IDENTIFICATION_INFO")
})
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_KEY")
    private Long key;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_PASSWORD")
    private String password;

    @Column(name = "BIRTHDATE", length = 8)
    private String birthDate;

    @Column(name = "GENDER", length = 1)
    private String gender;

    @Column(name = "NAME")
    private String name;

    @Column(name = "HPTEL", length = 11)
    private String hptel;

    @Column(name = "IDENTIFICATION_INFO")
    private String identificationInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_STATUS")
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "CERTIFICATION_TYPE")
    private CertificationType certificationType;

    @ToString.Exclude
    @NotAudited
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Company> companyList = new ArrayList<>();

    @Builder
    public User(String userId, String userName, String password, String identificationInfo,
                String birthDate, String gender, String name, String hptel,
                UserStatus userStatus, CertificationType certificationType) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.identificationInfo = identificationInfo;
        this.birthDate = birthDate;
        this.gender = gender;
        this.name = name;
        this.hptel = hptel;
        this.userStatus = userStatus;
        this.certificationType = certificationType;
    }

    /**
     * 카카오인증 데이터 엔티티 생성
     * @param insertUserRequestForKakao
     * @return
     */
    public static User setUser(InsertUserRequestForKakao insertUserRequestForKakao) {
        return User.builder()
                .userId(insertUserRequestForKakao.getIdentificationInfo())
                .userName(insertUserRequestForKakao.getNickname())
                .birthDate(insertUserRequestForKakao.getBirthDate())
                .gender(GenderUtil.convertGender(insertUserRequestForKakao.getGender()))
                .name(insertUserRequestForKakao.getName())
                .hptel(insertUserRequestForKakao.getHptel())
                .identificationInfo(insertUserRequestForKakao.getIdentificationInfo())
                .userStatus(UserStatus.USE)
                .certificationType(CertificationType.KAKAO)
                .build();
    }

    /**
     * 휴대폰인증 데이터 엔티티 생성
     * @param insertUserRequestForHptel
     * @return
     */
    public static User setUser(InsertUserRequestForHptel insertUserRequestForHptel) {
        return User.builder()
                .userId(insertUserRequestForHptel.getName() + "_" + insertUserRequestForHptel.getBirthDate())
                .userName(insertUserRequestForHptel.getName())
                .birthDate(insertUserRequestForHptel.getBirthDate())
                .gender(GenderUtil.convertGender(insertUserRequestForHptel.getGender()))
                .name(insertUserRequestForHptel.getName())
                .hptel(insertUserRequestForHptel.getHptel())
                .identificationInfo(insertUserRequestForHptel.getIdentificationInfo())
                .userStatus(UserStatus.USE)
                .certificationType(CertificationType.HPTEL)
                .build();
    }

    /**
     * 사용자 정보 변경 메소드
     * @param modifyUserRequest
     */
    public void modifyUserInfo(ModifyUserRequest modifyUserRequest) {
        if (StringUtils.hasText(modifyUserRequest.getUserName())) setUserName(modifyUserRequest.getUserName());
        if (StringUtils.hasText(modifyUserRequest.getBirthDate())) setBirthDate(modifyUserRequest.getBirthDate());
        if (StringUtils.hasText(modifyUserRequest.getGender())) setGender(modifyUserRequest.getGender());
        if (StringUtils.hasText(modifyUserRequest.getName())) setName(modifyUserRequest.getName());
        if (StringUtils.hasText(modifyUserRequest.getHptel())) setHptel(modifyUserRequest.getHptel());
    }

    /**
     * 회원탈퇴
     * 유니크 제약조건으로 재가입이 불가능하므로 전부 NULL 처리
     * 이력테이블로 히스토리 조회가능
     */
    public void withDrawal() {
        setUserId(null);
        setUserName(null);
        setPassword(null);
        setBirthDate(null);
        setGender(null);
        setName(null);
        setHptel(null);
        setIdentificationInfo(null);
        setUserStatus(UserStatus.DELETE);
        setCertificationType(null);
    }
}
