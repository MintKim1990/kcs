package com.project.kcs.repository.company;

import com.project.kcs.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyRepositoryCustom {

    Optional<Company> findCompanyBybusinessNumber(String businessNumber);

}
