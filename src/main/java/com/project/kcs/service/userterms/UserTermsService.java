package com.project.kcs.service.userterms;

import com.project.kcs.entity.Terms;
import com.project.kcs.entity.User;
import com.project.kcs.entity.UserTerms;
import com.project.kcs.repository.terms.TermsRepository;
import com.project.kcs.repository.userterms.UserTermsRepository;
import com.project.kcs.request.userterms.InsertUserTermsRequest;
import com.project.kcs.response.terms.SearchTermsResponse;
import com.project.kcs.response.userterms.InsertUserTermsResponse;
import com.project.kcs.response.userterms.SearchUserTermsResponse;
import com.project.kcs.response.userterms.SearchUserTermsHistoryResponse;
import com.project.kcs.response.userterms.SearchUserTermsWithHistoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserTermsService {

    private final UserTermsRepository userTermsRepository;
    private final TermsRepository termsRepository;

    /**
     * 사용자별 약관 및 약관이력 전체조회
     *
     * 주의사항
     * Spring data Envers 방식에 로깅을 사용하고있는데 해당방식은 Spring data Envers에서 제공하는 Repository 방식으로 조회하다보니
     * N+1방식을 해결할 방법이 없음 (batchsize도 안먹힘..)
     *
     * 만약 데이터가 많아져서 튜닝이 필요할경우 이력관리 하려는 엔티티에 @PostPersist, @PostUpdate 어노테이션으로 직접 로깅처리하는방향으로
     * 변경할 예정
     *
     * @param user
     * @return
     */
    public List<SearchUserTermsWithHistoryResponse> findAll(User user) {
        return userTermsRepository.findSearchUserTermsResponseListByUser(user)
                .stream()
                .map(searchUserTermsResponse -> {
                    return new SearchUserTermsWithHistoryResponse(
                            searchUserTermsResponse,
                            findAllHistory(searchUserTermsResponse.getKey())
                    );
                })
                .collect(Collectors.toList());
    }

    /**
     * 사용자별 약관 조회
     * @param user
     * @return
     */
    public List<SearchUserTermsResponse> findTermsAll(User user) {
        return userTermsRepository.findSearchUserTermsResponseListByUser(user);
    }

    /**
     * 약관 이력 조회
     * @param key
     * @return
     */
    public List<SearchUserTermsHistoryResponse> findAllHistory(Long key) {
        return userTermsRepository.findRevisions(key)
                .stream()
                .map(revision -> {
                    return new SearchUserTermsHistoryResponse(
                            revision.getEntity().getKey(),
                            new SearchTermsResponse(
                                    revision.getEntity().getTerms().getKey(),
                                    revision.getEntity().getTerms().getType(),
                                    revision.getEntity().getTerms().getTitle(),
                                    revision.getEntity().getTerms().getContent(),
                                    revision.getEntity().getTerms().getStatus()
                            ),
                            revision.getEntity().getAvailableTime(),
                            revision.getEntity().getTermsAgreeType(),
                            revision.getMetadata().getRequiredRevisionNumber(),
                            revision.getMetadata().getRequiredRevisionInstant(),
                            revision.getMetadata().getRevisionType()
                    );
                }).collect(Collectors.toList());
    }

    /**
     * 약관동의처리
     * @param insertUserTermsRequest
     * @param user
     * @return
     */
    @Transactional
    public InsertUserTermsResponse registration(InsertUserTermsRequest insertUserTermsRequest, User user) {

        registrationValidation(insertUserTermsRequest, user);

        // 이미 등록된 약관인지 확인
        Optional<UserTerms> existTerms = userTermsRepository.findUserTermsListByUser(user)
                .stream()
                .filter(userTerms -> userTerms.getTerms()
                        .getType()
                        .equals(insertUserTermsRequest.getTermsType()))
                .findAny();

        // 등록된 약관일경우 수정
        if (existTerms.isPresent()) {

            UserTerms userTerms = existTerms.get();

            // 약관 수정
            userTerms.modifyTermsAgreeType(insertUserTermsRequest.getTermsAgreeType());
            userTerms.modifyAvailableTime(LocalDateTime.now().plusYears(1L));

            return InsertUserTermsResponse.createRegistrationUserTermsResponse(userTerms);
        }

        // 약관 등록
        UserTerms userTerms = UserTerms.setUserTerms(
                insertUserTermsRequest,
                user,
                termsRepository.findTermsByType(insertUserTermsRequest.getTermsType()).get());

        userTermsRepository.save(userTerms);

        return InsertUserTermsResponse.createRegistrationUserTermsResponse(userTerms);
    }

    private boolean registrationValidation(InsertUserTermsRequest insertUserTermsRequest, User user) {

        // 약관이 존재하는지 확인
        Terms terms = termsRepository.findTermsByType(insertUserTermsRequest.getTermsType())
                .orElseThrow(() -> {
                    throw new IllegalStateException("약관이 유효하지 않습니다.");
                });

        // 사용중인 약관인지 확인
        if (!terms.available())
            throw new IllegalStateException("약관이 유효하지 않습니다.");

        return true;
    }

}
