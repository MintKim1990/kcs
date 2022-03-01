package com.project.kcs.repository.user;

import com.project.kcs.dto.user.LoginUserInfo;
import com.project.kcs.entity.User;
import com.project.kcs.entity.constant.CertificationType;
import com.project.kcs.entity.constant.UserStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.project.kcs.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findExistUser(String userId, String identificationInfo) {
        return Optional.ofNullable(
                queryFactory
                        .select(user)
                        .from(user)
                        .where(
                                user.userId.eq(userId).or(
                                        user.userStatus.eq(UserStatus.USE).and(user.identificationInfo.eq(identificationInfo))
                                )
                        )
                        .fetchOne()
        );
    }

    /**
     * SNS 간편로그인 조회 쿼리
     * @param certificationType
     * @param identificationInfo
     * @return
     */
    @Override
    public Optional<LoginUserInfo> findUserByLoginInfo(CertificationType certificationType, String identificationInfo) {
        return Optional.ofNullable(
                queryFactory.select(
                                Projections.constructor(
                                        LoginUserInfo.class,
                                        user.key,
                                        user.userName,
                                        user.birthDate,
                                        user.gender,
                                        user.name,
                                        user.hptel,
                                        user.identificationInfo,
                                        user.userStatus
                                )
                        )
                        .from(user)
                        .where(
                                user.certificationType.eq(certificationType)
                                .and(user.identificationInfo.eq(identificationInfo))
                                .and(user.userStatus.eq(UserStatus.USE))
                        )
                        .fetchOne()
        );
    }
}
