package com.project.kcs.service.terms;

import com.project.kcs.entity.constant.TermsType;
import com.project.kcs.request.terms.RegistrationTermsRequest;
import com.project.kcs.response.terms.InsertTermsResponse;
import com.project.kcs.response.terms.SearchTermsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class TermsServiceTest {

    @Autowired
    TermsService termsService;

    @Test
    public void 약관등록() {

        InsertTermsResponse insertTermsResponse = termsService.registration(new RegistrationTermsRequest(
                TermsType.SCRAP,
                "스크래핑약관동의",
                "스크래핑약관에 동의하시나요?"
        ));

        assertThat(insertTermsResponse.getType()).isEqualTo(TermsType.SCRAP);
        assertThat(insertTermsResponse.getTitle()).isEqualTo("스크래핑약관동의");
        assertThat(insertTermsResponse.getContent()).isEqualTo("스크래핑약관에 동의하시나요?");
    }

    @DisplayName("약관타입이 동일한 약관을 중복등록시 에러가 발생해야 한다.")
    @Test
    public void 약관중복등록() {

        // given
        InsertTermsResponse insertTermsResponse = termsService.registration(new RegistrationTermsRequest(
                TermsType.SCRAP,
                "스크래핑약관동의",
                "스크래핑약관에 동의하시나요?"
        ));

        // when then
        assertThrows(IllegalStateException.class, () -> {
            termsService.registration(new RegistrationTermsRequest(
                    TermsType.SCRAP,
                    "스크래핑약관동의",
                    "스크래핑약관 중복등록"
            ));
        });
    }

    @Test
    public void 약관타이틀수정() {

        // given
        InsertTermsResponse insertTermsResponse = termsService.registration(new RegistrationTermsRequest(
                TermsType.SCRAP,
                "스크래핑약관동의",
                "스크래핑약관에 동의하시나요?"
        ));

        // when
        termsService.modifyTitle(insertTermsResponse.getKey(), "타이틀을 변경했어요.");

        SearchTermsResponse searchTermsResponse = termsService.findOne(insertTermsResponse.getKey());

        // then
        assertThat(searchTermsResponse.getTitle()).isEqualTo("타이틀을 변경했어요.");
    }

    @Test
    public void 약관내용수정() {

        // given
        InsertTermsResponse insertTermsResponse = termsService.registration(new RegistrationTermsRequest(
                TermsType.SCRAP,
                "스크래핑약관동의",
                "스크래핑약관에 동의하시나요?"
        ));

        // when
        termsService.modifyContent(insertTermsResponse.getKey(), "내용을 변경했어요");

        SearchTermsResponse searchTermsResponse = termsService.findOne(insertTermsResponse.getKey());

        // then
        assertThat(searchTermsResponse.getContent()).isEqualTo("내용을 변경했어요");
    }

    @Test
    public void 약관삭제() {

        // given
        InsertTermsResponse insertTermsResponse = termsService.registration(new RegistrationTermsRequest(
                TermsType.SCRAP,
                "스크래핑약관동의",
                "스크래핑약관에 동의하시나요?"
        ));

        // when
        termsService.delete(insertTermsResponse.getKey());

        SearchTermsResponse searchTermsResponse = termsService.findOne(insertTermsResponse.getKey());

        // then
        assertThat(searchTermsResponse).isEqualTo(null);
    }


}