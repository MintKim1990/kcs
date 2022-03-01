package com.project.kcs.service.scrapcredential;

import com.project.kcs.entity.ScrapCredential;
import com.project.kcs.entity.User;
import com.project.kcs.repository.scrapcredential.ScrapCredentialRepository;
import com.project.kcs.request.scrapcredential.InsertScrapCredentialRequest;
import com.project.kcs.response.scrapcredential.InsertScrapCredentialResponse;
import com.project.kcs.response.scrapcredential.SearchScrapCredentialResponse;
import com.project.kcs.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScrapCredentialService {

    private final ScrapCredentialRepository scrapCredentialRepository;

    public List<SearchScrapCredentialResponse> findAll(User user) {
        return scrapCredentialRepository.findScrapCredentialListByUser(user)
                .stream()
                .map(scrapCredential -> new SearchScrapCredentialResponse(
                        scrapCredential.getKey(),
                        scrapCredential.getType(),
                        scrapCredential.getId(),
                        PasswordUtil.convertDecoding(scrapCredential.getPassword())
                ))
                .collect(Collectors.toList());
    }

    /**
     * 인증기관정보 등록
     * @param insertScrapCredentialRequest
     * @param user
     * @return
     */
    @Transactional
    public InsertScrapCredentialResponse registration(InsertScrapCredentialRequest insertScrapCredentialRequest, User user) {

        ScrapCredential scrapCredential = ScrapCredential.setScrapCredential(insertScrapCredentialRequest, user);

        // 약관등록 조건체크
        validateExistSrapCredential(scrapCredential, user);

        scrapCredentialRepository.save(scrapCredential);

        return InsertScrapCredentialResponse.createRegistrationScrapCredentialResponse(scrapCredential);
    }

    private void validateExistSrapCredential(ScrapCredential scrapCredential, User user) {

        Optional<ScrapCredential> findScrapCredential =
                scrapCredentialRepository.findScrapCredentialByTypeAndUser(scrapCredential.getType(), user);

        if (findScrapCredential.isPresent())
            throw new IllegalStateException("이미 등록된 정보입니다.");

    }

    /**
     * 인증기관정보 수정
     * @param key
     * @param id
     * @param password
     */
    @Transactional
    public void modifyCredentialInfo(Long key, String id, String password, User user) {

        ScrapCredential scrapCredential = scrapCredentialRepository.findById(key)
                .orElseThrow(() -> {
                    throw new IllegalStateException("인증기관 정보가 존재하지 않습니다.");
                });

        // 인증기관정보 등록자만 수정가능
        if (scrapCredential.getUser().getKey().equals(user.getKey())) {
            // 인증기관정보변경
            scrapCredential.modifyId(id);
            scrapCredential.modifyPassword(PasswordUtil.convertEncoding(password));
        } else {
            throw new IllegalStateException("인증정보 등록자가 아니므로 수정할 수 없습니다.");
        }

    }

    /**
     * 인증기관정보 삭제
     * @param key
     */
    @Transactional
    public void delete(Long key, User user) {

        ScrapCredential scrapCredential = scrapCredentialRepository.findById(key)
                .orElseThrow(() -> {
                    throw new IllegalStateException("인증기관 정보가 존재하지 않습니다.");
                });

        // 인증기관정보 등록자만 삭제가능
        if (scrapCredential.getUser().getKey().equals(user.getKey())) {
            // 인증기관정보 삭제
            scrapCredentialRepository.deleteById(key);
        } else {
            throw new IllegalStateException("인증정보 등록자가 아니므로 삭제할 수 없습니다.");
        }


    }

}
