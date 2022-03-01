package com.project.kcs.service.user;

import com.project.kcs.dto.user.LoginUserInfo;
import com.project.kcs.entity.User;
import com.project.kcs.entity.constant.CertificationType;
import com.project.kcs.entity.constant.UserStatus;
import com.project.kcs.request.user.LoginUserRequest;
import com.project.kcs.response.user.JoinUserResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserServiceTest {

    protected MockHttpSession session;

    @Autowired
    UserService userService;

    @BeforeEach
    public void setUp() {
        session = new MockHttpSession();
    }

    @AfterEach
    public void clear(){
        session.clearAttributes();
        session = null;
    }

    @Test
    public void 회원가입() {

        // given
        User user = createUser();

        // when
        JoinUserResponse response = userService.join(user);

        // then
        assertThat(response.getUserId()).isEqualTo(user.getUserId());
        assertThat(response.getUserName()).isEqualTo(user.getUserName());
    }

    @DisplayName("로그인 성공시 세션에 유저정보가 담겨있어야 하며 응답코드가 성공이여야한다.")
    @Test
    public void 로그인성공() {

        // given
        User user = createUser();

        userService.join(user);

        // when
        LoginUserInfo loginUserInfo = userService.login(
                new LoginUserRequest(CertificationType.KAKAO, "testci"), session
        );

        // then
        assertThat(session.getAttribute("UserInfo")).isNotNull();
        assertThat(loginUserInfo).isNotNull();
    }

    @DisplayName("로그인 실패시 에러가 발생해야한다.")
    @Test
    public void 로그인실패() {

        // given
        User user = createUser();

        userService.join(user);

        // when then
        assertThrows(IllegalStateException.class, () -> {
            userService.login(
                    new LoginUserRequest(CertificationType.KAKAO, "testCIFAIL"), session
            );
        });
    }

    @DisplayName("회원탈퇴 후 로그인 시도시 로그인이 되지 않아야 한다.")
    @Test
    public void 회원탈퇴() {

        // given
        User user = createUser();

        userService.join(user);

        // when
        userService.delete(user.getKey(), user);

        assertThrows(IllegalStateException.class, () -> {
            userService.login(
                    new LoginUserRequest(CertificationType.KAKAO, "testCIFAIL"), session
            );
        });
    }

    private User createUser() {
        return User.builder()
                .userId("userA")
                .identificationInfo("testci")
                .userName("유저이름")
                .birthDate("19900703")
                .gender("1")
                .name("김민태")
                .hptel("01071656293")
                .userStatus(UserStatus.USE)
                .certificationType(CertificationType.KAKAO)
                .build();
    }

}