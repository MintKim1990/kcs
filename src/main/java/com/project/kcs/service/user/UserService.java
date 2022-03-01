package com.project.kcs.service.user;

import com.project.kcs.dto.user.LoginUserInfo;
import com.project.kcs.request.user.LoginUserRequest;
import com.project.kcs.request.user.ModifyUserRequest;
import com.project.kcs.response.user.JoinUserResponse;
import com.project.kcs.entity.User;
import com.project.kcs.entity.constant.UserStatus;
import com.project.kcs.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final String USER_SESSION = "UserInfo";

    private final UserRepository userRepository;

    /**
     * 회원가입
     * @param user
     * @return
     */
    @Transactional
    public JoinUserResponse join(User user) {

        // 중복회원 체크
        validateExistUser(user);

        userRepository.save(user);

        return JoinUserResponse.createUserResponse(user);
    }

    /**
     * 사용자 이름 수정
     * @param key
     * @param user
     * @param modifyUserRequest
     */
    @Transactional
    public User modifyUserInfo(Long key, User user, ModifyUserRequest modifyUserRequest) {

        userKeyValidation(key, user);

        User findUser = userRepository.findById(key)
                .orElseThrow(() -> {
                    throw new IllegalStateException("해당 회원은 존재하지 않습니다.");
                });

        findUser.modifyUserInfo(modifyUserRequest);

        return findUser;
    }

    /**
     * 로그인
     * @param loginUserRequest
     * @return
     */
    public LoginUserInfo login(LoginUserRequest loginUserRequest, HttpSession session) {

        Optional<LoginUserInfo> loginUser = userRepository.findUserByLoginInfo(
                loginUserRequest.getCertificationType(),
                loginUserRequest.getIdentificationInfo()
        );

        // 조회된 사용자가 없을경우 FAIL
        if (!loginUser.isPresent()) {
            throw new IllegalStateException("해당 정보로 가입된 회원이 존재하지 않습니다.");
        }

        // 사용자정보 세션 세팅
        session.setAttribute(USER_SESSION, loginUser.get());

        return loginUser.get();
    }

    /**
     * 회원탈퇴
     * @param key
     * @return
     */
    @Transactional
    public void delete(Long key, User user) {

        userKeyValidation(key, user);

        User findUser = userRepository.findById(key)
                .orElseThrow(() -> {
                    throw new IllegalStateException("해당 회원은 존재하지 않습니다.");
                });

        // 회원유효상태 체크
        if (!findUser.getUserStatus().equals(UserStatus.USE)) {
            throw new IllegalStateException("해당 회원은 상태가 유효하지 않아 삭제가 불가능합니다.");
        }

        // 회원탈퇴
        findUser.withDrawal();
    }

    /**
     * 요청한 user Key가 로그인한 본인의 Key인지 확인
     * @param key
     * @param user
     */
    private void userKeyValidation(Long key, User user) {
        if (!user.getKey().equals(key))
            throw new IllegalStateException("다른 사용자를 변경할 수 없습니다.");
    }

    /**
     * 중복회원가입 체크
     * @param user
     */
    private void validateExistUser(User user) {

        Optional<User> existUser = userRepository.findExistUser(
                user.getUserId(),
                user.getIdentificationInfo()
        );

        if (existUser.isPresent())
            throw new IllegalStateException("이미 존재하는 회원입니다.");
    }

    public User getUserBySession(HttpSession session) {

        LoginUserInfo userInfo = (LoginUserInfo) session.getAttribute(USER_SESSION);

        User user = userRepository.findById(userInfo.getKey())
                .orElseThrow(() -> {
                    throw new IllegalStateException("사용자 정보가 존재하지 않습니다.");
                });

        return user;
    }

}
