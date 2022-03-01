package com.project.kcs.service.terms;

import com.project.kcs.entity.Terms;
import com.project.kcs.repository.terms.TermsRepository;
import com.project.kcs.request.terms.RegistrationTermsRequest;
import com.project.kcs.response.terms.InsertTermsResponse;
import com.project.kcs.response.terms.SearchTermsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TermsService {

    private final TermsRepository termsRepository;

    public SearchTermsResponse findOne(Long key) {
        return termsRepository.findSearchTermsResponseById(key).orElse(null);
    }

    /**
     * 약관전체조회
     * @return
     */
    public List<SearchTermsResponse> findAll() {
        return termsRepository.findSearchTermsResponseList();
    }

    /**
     * 약관등록
     * @param registrationTermsRequest
     * @return
     */
    @Transactional
    public InsertTermsResponse registration(RegistrationTermsRequest registrationTermsRequest) {

        Terms terms = Terms.setTerms(registrationTermsRequest);

        // 약관등록 조건체크
        validateExistTerms(terms);

        termsRepository.save(terms);

        return InsertTermsResponse.createRegistrationTermsResponse(terms);
    }

    /**
     * 약관 타이틀 수정
     * @param key
     * @param title
     */
    @Transactional
    public void modifyTitle(Long key, String title) {

        Terms terms = termsRepository.findById(key)
                .orElseThrow(() -> {
                    throw new IllegalStateException("약관이 존재하지 않습니다.");
                });

        terms.modifyTitle(title);
    }

    /**
     * 약관 내용 수정
     * @param key
     * @param content
     */
    @Transactional
    public void modifyContent(Long key, String content) {

        Terms terms = termsRepository.findById(key)
                .orElseThrow(() -> {
                    throw new IllegalStateException("약관이 존재하지 않습니다.");
                });

        terms.modifyContent(content);
    }

    /**
     * 약관 삭제
     * @param key
     */
    @Transactional
    public void delete(Long key) {

        Terms terms = termsRepository.findById(key)
                .orElseThrow(() -> {
                    throw new IllegalStateException("약관이 존재하지 않습니다.");
                });

        terms.delete();
    }

    /**
     * 중복약관 등록 체크
     * @param terms
     */
    private void validateExistTerms(Terms terms) {

        Optional<Terms> findTerms = termsRepository.findTermsByType(terms.getType());

        if (findTerms.isPresent())
            throw new IllegalStateException("이미 존재하는 약관입니다.");

    }

}
