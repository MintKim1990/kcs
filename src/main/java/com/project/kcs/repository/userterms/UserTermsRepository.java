package com.project.kcs.repository.userterms;

import com.project.kcs.entity.User;
import com.project.kcs.entity.UserTerms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.List;

public interface UserTermsRepository extends JpaRepository<UserTerms, Long>, RevisionRepository<UserTerms, Long, Integer>, UserTermsRepositoryCustom {

    List<UserTerms> findUserTermsListByUser(User user);

}
