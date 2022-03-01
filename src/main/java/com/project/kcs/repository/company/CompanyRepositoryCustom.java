package com.project.kcs.repository.company;

import com.project.kcs.entity.User;
import com.project.kcs.response.company.SearchCompanyResponse;

import java.util.List;
import java.util.Optional;

public interface CompanyRepositoryCustom {

    Optional<SearchCompanyResponse> findCompanyByCompanyKeyAndUser(Long key, User user);

    List<SearchCompanyResponse> findCompanyListByUser(User user);

}
