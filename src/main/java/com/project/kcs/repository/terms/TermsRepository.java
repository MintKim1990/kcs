package com.project.kcs.repository.terms;

import com.project.kcs.entity.Terms;
import com.project.kcs.entity.constant.TermsType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TermsRepository extends JpaRepository<Terms, Long>, TermsRepositoryCustom {

    Optional<Terms> findTermsByType(TermsType type);

}
