package com.project.kcs.repository.user;

import com.project.kcs.dto.user.LoginUserInfo;
import com.project.kcs.entity.User;
import com.project.kcs.entity.constant.CertificationType;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> findExistUser(String userId, String identificationInfo);

    Optional<LoginUserInfo> findUserByLoginInfo(CertificationType certificationType, String identificationInfo);

}
