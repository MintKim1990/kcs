package com.project.kcs.repository.terms;

import com.project.kcs.entity.Terms;
import com.project.kcs.entity.constant.TermsStatus;
import com.project.kcs.entity.constant.TermsType;
import com.project.kcs.response.terms.SearchTermsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class TermsRepositoryTest {

    @Autowired
    private TermsRepository termsRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void 약관등록() {

        // given
        Terms terms = Terms.builder()
                .type(TermsType.SCRAP)
                .title("스크래핑동의약관")
                .content("스크래핑하는것에동의하시나요?")
                .status(TermsStatus.USE)
                .build();

        // when
        termsRepository.save(terms);

        // then
        assertThat(terms.getKey()).isNotNull();
    }

    @DisplayName("약관타입이 동일한 약관을 중복등록시 에러가 발생해야 한다.")
    @Test
    public void 약관중복등록() {

        // given
        Terms terms = Terms.builder()
                .type(TermsType.SCRAP)
                .title("스크래핑동의약관")
                .content("스크래핑하는것에동의하시나요?")
                .status(TermsStatus.USE)
                .build();

        Terms terms2 = Terms.builder()
                .type(TermsType.SCRAP)
                .title("스크래핑동의약관")
                .content("똑같은걸또넣으시게요?")
                .status(TermsStatus.USE)
                .build();

        // when
        termsRepository.save(terms);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> {
            termsRepository.save(terms2);
        });

    }

    @Test
    public void 약관타이틀수정() {

        // given
        Terms terms = Terms.builder()
                .type(TermsType.SCRAP)
                .title("스크래핑동의약관")
                .content("스크래핑하는것에동의하시나요?")
                .status(TermsStatus.USE)
                .build();

        termsRepository.save(terms);

        // when
        terms.modifyTitle("스크래핑동의약관제목변경");

        entityManager.flush();
        entityManager.clear();

        // then
        Terms findTerms = termsRepository.findById(terms.getKey())
                .orElseThrow(() -> {
                    throw new IllegalStateException("약관데이터가 존재하지 않습니다.");
                });

        assertThat(findTerms.getTitle()).isEqualTo("스크래핑동의약관제목변경");
    }

    @Test
    public void 약관내용수정() {

        // given
        Terms terms = Terms.builder()
                .type(TermsType.SCRAP)
                .title("스크래핑동의약관")
                .content("스크래핑하는것에동의하시나요?")
                .status(TermsStatus.USE)
                .build();

        termsRepository.save(terms);

        // when
        terms.modifyContent("스크래핑동의약관내용변경입니다만");

        entityManager.flush();
        entityManager.clear();

        // then
        Terms findTerms = termsRepository.findById(terms.getKey())
                .orElseThrow(() -> {
                    throw new IllegalStateException("약관데이터가 존재하지 않습니다.");
                });

        assertThat(findTerms.getContent()).isEqualTo("스크래핑동의약관내용변경입니다만");
    }

    @Test
    public void 약관삭제() {

        // given
        Terms terms = Terms.builder()
                .type(TermsType.SCRAP)
                .title("스크래핑동의약관")
                .content("스크래핑하는것에동의하시나요?")
                .status(TermsStatus.USE)
                .build();

        termsRepository.save(terms);

        // when
        terms.delete();

        entityManager.flush();
        entityManager.clear();

        // then
        Optional<SearchTermsResponse> findTerms
                = termsRepository.findSearchTermsResponseById(terms.getKey());

        assertThat(findTerms.isPresent()).isEqualTo(false);
    }

}