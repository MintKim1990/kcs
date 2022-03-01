package com.project.kcs.service.userterms;

import com.project.kcs.entity.User;
import com.project.kcs.entity.constant.CertificationType;
import com.project.kcs.entity.constant.TermsAgreeType;
import com.project.kcs.entity.constant.TermsType;
import com.project.kcs.entity.constant.UserStatus;
import com.project.kcs.repository.terms.TermsRepository;
import com.project.kcs.repository.user.UserRepository;
import com.project.kcs.repository.userterms.UserTermsRepository;
import com.project.kcs.request.terms.RegistrationTermsRequest;
import com.project.kcs.request.userterms.InsertUserTermsRequest;
import com.project.kcs.response.terms.InsertTermsResponse;
import com.project.kcs.response.userterms.InsertUserTermsResponse;
import com.project.kcs.response.userterms.SearchUserTermsHistoryResponse;
import com.project.kcs.response.userterms.SearchUserTermsResponse;
import com.project.kcs.response.userterms.SearchUserTermsWithHistoryResponse;
import com.project.kcs.service.terms.TermsService;
import com.project.kcs.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTermsServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    TermsService termsService;

    @Autowired
    UserTermsService userTermsService;

    @Autowired
    TermsRepository termsRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserTermsRepository userTermsRepository;

    @Transactional
    @Test
    public void 약관동의() {

        // given
        User user = createUser();

        userService.join(user);

        InsertTermsResponse insertTermsResponse = termsService.registration(new RegistrationTermsRequest(
                TermsType.SCRAP,
                "스크래핑약관동의",
                "스크래핑약관에 동의하시나요?"
        ));

        // when
        InsertUserTermsResponse insertUserTermsResponse = userTermsService.registration(new InsertUserTermsRequest(
                TermsType.SCRAP,
                TermsAgreeType.AGREE
        ), user);

        // then
        List<SearchUserTermsResponse> findUserTerms = userTermsService.findTermsAll(user);

        SearchUserTermsResponse findsearchUserTermsResponse = findUserTerms.stream()
                .filter(searchUserTermsResponse -> searchUserTermsResponse.getTerms().getType().equals(TermsType.SCRAP))
                .findFirst()
                .orElse(null);

        assertThat(findsearchUserTermsResponse).isNotNull();
        assertThat(findsearchUserTermsResponse.getTerms().getType()).isEqualTo(TermsType.SCRAP);
        assertThat(findsearchUserTermsResponse.getTermsAgreeType()).isEqualTo(TermsAgreeType.AGREE);

        userTermsRepository.deleteById(insertUserTermsResponse.getKey());
        termsRepository.deleteById(insertTermsResponse.getKey());
        userRepository.deleteById(user.getKey());

    }

    @Transactional
    @Test
    public void 약관수정() {

        // given
        User user = createUser();

        userService.join(user);

        InsertTermsResponse insertTermsResponse = termsService.registration(new RegistrationTermsRequest(
                TermsType.SCRAP,
                "스크래핑약관동의",
                "스크래핑약관에 동의하시나요?"
        ));

        InsertUserTermsResponse insertUserTermsResponse = userTermsService.registration(new InsertUserTermsRequest(
                TermsType.SCRAP,
                TermsAgreeType.AGREE
        ), user);

        // when
        userTermsService.registration(new InsertUserTermsRequest(
                TermsType.SCRAP,
                TermsAgreeType.DISAGREE
        ), user);

        // then
        List<SearchUserTermsResponse> findUserTerms = userTermsService.findTermsAll(user);

        SearchUserTermsResponse findsearchUserTermsResponse = findUserTerms.stream()
                .filter(searchUserTermsResponse -> searchUserTermsResponse.getTerms().getType().equals(TermsType.SCRAP))
                .findFirst()
                .orElse(null);

        assertThat(findsearchUserTermsResponse).isNotNull();
        assertThat(findsearchUserTermsResponse.getTerms().getType()).isEqualTo(TermsType.SCRAP);
        assertThat(findsearchUserTermsResponse.getTermsAgreeType()).isEqualTo(TermsAgreeType.DISAGREE);

        userTermsRepository.deleteById(insertUserTermsResponse.getKey());
        termsRepository.deleteById(insertTermsResponse.getKey());
        userRepository.deleteById(user.getKey());

    }

    @Test
    public void 약관및이력전체조회() {

        // given
        User user = createUser();

        userService.join(user);

        InsertTermsResponse insertTermsResponse = termsService.registration(new RegistrationTermsRequest(
                TermsType.SCRAP,
                "스크래핑약관동의",
                "스크래핑약관에 동의하시나요?"
        ));

        InsertTermsResponse insertTermsResponse1 = termsService.registration(new RegistrationTermsRequest(
                TermsType.SERVICE,
                "서비스약관",
                "서비스약관에 동의하시나요?"
        ));

        InsertUserTermsResponse insertUserTermsResponse = userTermsService.registration(new InsertUserTermsRequest(
                TermsType.SCRAP,
                TermsAgreeType.AGREE
        ), user);

        InsertUserTermsResponse insertUserTermsResponse1 = userTermsService.registration(new InsertUserTermsRequest(
                TermsType.SERVICE,
                TermsAgreeType.AGREE
        ), user);


        // when
        userTermsService.registration(new InsertUserTermsRequest(
                TermsType.SCRAP,
                TermsAgreeType.DISAGREE
        ), user);

        // then
        List<SearchUserTermsHistoryResponse> history = userTermsService.findAllHistory(insertUserTermsResponse.getKey());

        assertThat(history).isNotNull();
        assertThat(history.size()).isEqualTo(2);

        List<SearchUserTermsWithHistoryResponse> searchUserTermsWithHistoryResponsesList = userTermsService.findAll(user);

        assertThat(searchUserTermsWithHistoryResponsesList).isNotNull();
        assertThat(searchUserTermsWithHistoryResponsesList.size()).isEqualTo(2);

        userTermsRepository.deleteById(insertUserTermsResponse.getKey());
        userTermsRepository.deleteById(insertUserTermsResponse1.getKey());
        termsRepository.deleteById(insertTermsResponse.getKey());
        termsRepository.deleteById(insertTermsResponse1.getKey());
        userRepository.deleteById(user.getKey());

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