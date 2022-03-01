package com.project.kcs.repository.userterms;

import com.project.kcs.entity.User;
import com.project.kcs.response.userterms.SearchUserTermsResponse;

import java.util.List;

public interface UserTermsRepositoryCustom {

    List<SearchUserTermsResponse> findSearchUserTermsResponseListByUser(User finduser);

}
