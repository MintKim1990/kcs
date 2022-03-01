package com.project.kcs.repository.user;

import com.project.kcs.entity.constant.CertificationType;
import com.project.kcs.entity.User;
import com.project.kcs.entity.constant.UserStatus;
import com.project.kcs.request.user.ModifyUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @DisplayName("유저 생성")
    @Test
    public void 유저생성() {

        // given
        User user = createUser();

        // when
        userRepository.save(user);

        User findUser = userRepository.findUserByuserId(user.getUserId())
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("해당 사용자는 존재하지 않습니다.");
                });

        // then
        System.out.println("user = " + user);
        System.out.println("findUser = " + findUser);

        assertThat(user.getUserId()).isEqualTo(findUser.getUserId());
        assertThat(user.getUserName()).isEqualTo(findUser.getUserName());
        assertThat(user.getPassword()).isEqualTo(findUser.getPassword());
    }

    @DisplayName("유저 이름변경")
    @Test
    public void 유저이름변경() {

        // given
        User user = createUser();

        userRepository.save(user);

        // when
        user.modifyUserInfo(new ModifyUserRequest("이름변경", "", "", "", ""));

        entityManager.flush();
        entityManager.clear();

        User findUser = userRepository.findById(user.getKey())
                .orElseThrow(() -> {
                    throw new IllegalStateException("회원이 존재하지 않습니다.");
                });

        assertThat("이름변경").isEqualTo(findUser.getUserName());
    }

    @DisplayName("유저 탈퇴")
    @Test
    public void 유저탈퇴() {

        // given
        User user = createUser();

        userRepository.save(user);

        // when
        user.withDrawal();

        // then
        User findUser = userRepository.findById(user.getKey())
                .orElseThrow(() -> {
                    throw new IllegalStateException("회원이 존재하지 않습니다.");
                });

        assertThat(UserStatus.DELETE).isEqualTo(findUser.getUserStatus());

    }

    @DisplayName("유저 회원가입시 UserId가 동일한 사용자가 존재할경우 유니크 제약조건에 의해 에러가 발생해야한다.")
    @Test
    public void 유저중복회원가입아이디체크() {

        // given
        User user = createUserByIdentificationInfo("testCi1");
        User user2 = createUserByIdentificationInfo("testCi2");

        // when
        userRepository.save(user);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user2);
        });
    }

    @DisplayName("유저 회원가입시 인증정보가 동일한 사용자가 존재할경우 유니크 제약조건에 의해 에러가 발생해야한다.")
    @Test
    public void 유저중복회원가입인증정보체크() {

        // given
        User user = createUserByUserId("userIdA");
        User user2 = createUserByUserId("userIdB");

        // when
        userRepository.save(user);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user2);
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

    private User createUserByUserId(String userId) {
        return User.builder()
                .userId(userId)
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

    private User createUserByIdentificationInfo(String identificationInfo) {
        return User.builder()
                .userId("userA")
                .identificationInfo(identificationInfo)
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