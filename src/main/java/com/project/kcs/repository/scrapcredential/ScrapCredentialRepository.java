package com.project.kcs.repository.scrapcredential;

import com.project.kcs.entity.ScrapCredential;
import com.project.kcs.entity.User;
import com.project.kcs.entity.constant.CredentialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapCredentialRepository extends JpaRepository<ScrapCredential, Long> {

    Optional<ScrapCredential> findScrapCredentialByTypeAndUser(CredentialType type, User user);

    List<ScrapCredential> findScrapCredentialListByUser(User user);

}
