package com.project.kcs.service.company;

import com.project.kcs.constant.ResponseCode;
import com.project.kcs.entity.Company;
import com.project.kcs.entity.User;
import com.project.kcs.repository.company.CompanyRepository;
import com.project.kcs.request.company.BusinessSearchRequest;
import com.project.kcs.request.company.ModifyCompanyRequest;
import com.project.kcs.request.company.InsertCompanyRequest;
import com.project.kcs.response.company.InsertCompanyResponse;
import com.project.kcs.response.company.SearchCompanyResponse;
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
public class CompanyService {

    private final AllcreditClient allcreditClient;
    private final CrefiaClient crefiaClient;
    private final HometaxClient hometaxClient;
    private final CompanyRepository companyRepository;

    /**
     * 사업장 조회
     * @param key
     * @return
     */
    public SearchCompanyResponse findOne(Long key, User user) {
        return companyRepository.findCompanyByCompanyKeyAndUser(key, user).orElse(null);
    }

    /**
     * 사용자 사업장 조회
     * @param user
     * @return
     */
    public List<SearchCompanyResponse> findAll(User user) {
        return companyRepository.findCompanyListByUser(user);
    }

    /**
     * 사업장 등록
     * @param insertCompanyRequest
     * @param user
     * @return
     */
    @Transactional
    public InsertCompanyResponse registration(InsertCompanyRequest insertCompanyRequest, User user) {

        // 조건체크 (등록여부, 사업장진위확인)
        boolean validation = registrationValidation(
                BusinessSearchRequest.setBusinessSearchRequestForSearch(insertCompanyRequest, user)
        );

        if (validation) {

            Company company = companyRepository.save(
                    Company.setCompany(insertCompanyRequest, user)
            );

            return InsertCompanyResponse.createRegistrationCompanyResponse(company);
        }

        throw new IllegalStateException("등록실패");
    }

    /**
     * 사업장정보 수정
     * @param modifyCompanyRequest
     * @return
     */
    @Transactional
    public void modify(ModifyCompanyRequest modifyCompanyRequest, Long key, User user) {

        Company company = companyRepository.findById(key)
                .orElseThrow(() -> {
                    throw new IllegalStateException("등록되지 않은 사업장입니다.");
                });

        // 사업장 등록자만 수정가능
        if (company.getUser().getKey().equals(user.getKey())) {
            // 사업장정보변경
            company.ModifyCompanyInfo(modifyCompanyRequest);
        } else {
            throw new IllegalStateException("사업장 등록자가 아니므로 수정할 수 없습니다.");
        }
    }

    /**
     * 사업장정보 삭제
     * @param key
     * @param user
     */
    @Transactional
    public void delete(Long key, User user) {

        Company company = companyRepository.findById(key)
                .orElseThrow(() -> {
                    throw new IllegalStateException("등록되지 않은 사업장입니다.");
                });

        // 사업장 등록자만 삭제가능
        if (company.getUser().getKey().equals(user.getKey())) {
            // 사업장 삭제
            companyRepository.deleteById(key);
        } else {
            throw new IllegalStateException("사업장 등록자가 아니므로 삭제할 수 없습니다.");
        }

    }



    /**
     * 사업장 등록 조건체크
     * @param businessSearchRequest
     * @return
     */
    private boolean registrationValidation(BusinessSearchRequest businessSearchRequest) {

        Optional<Company> companyBybusinessNumber = companyRepository.findCompanyBybusinessNumber(businessSearchRequest.getBusinessNumber());

        if (companyBybusinessNumber.isPresent()) {
            throw new IllegalStateException("이미 등록된 사업장입니다.");
        }

        return registrationValidationByApi(businessSearchRequest);
    }

    /**
     * 사업장 진위확인
     * @param businessSearchRequest
     * @return
     */
    private boolean registrationValidationByApi(BusinessSearchRequest businessSearchRequest) {

        // KCB SOHO 진위확인
        if (allcreditClient.search(businessSearchRequest).getCode().equals(ResponseCode.SUCCESS)) {
            return true;
        }

        // 여신금융협회 진위확인
        if (crefiaClient.search(businessSearchRequest).getCode().equals(ResponseCode.SUCCESS)) {
            return true;
        }

        // 국세청 사업장 진위확인
        if (hometaxClient.search(businessSearchRequest).getCode().equals(ResponseCode.SUCCESS)) {
            return true;
        }

        throw new IllegalStateException("입력하신 사업장 정보가 유효하지 않습니다.");
    }

}
