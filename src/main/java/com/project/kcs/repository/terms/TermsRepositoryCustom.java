package com.project.kcs.repository.terms;

import com.project.kcs.entity.Terms;
import com.project.kcs.entity.constant.TermsType;
import com.project.kcs.response.terms.SearchTermsResponse;

import java.util.List;
import java.util.Optional;

public interface TermsRepositoryCustom {

    Optional<SearchTermsResponse> findSearchTermsResponseById(Long key);

    List<SearchTermsResponse> findSearchTermsResponseList();

}
