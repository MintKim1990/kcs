package com.project.kcs.repository.userterms;

import com.project.kcs.entity.QUser;
import com.project.kcs.entity.User;
import com.project.kcs.response.terms.SearchTermsResponse;
import com.project.kcs.response.userterms.SearchUserTermsResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.project.kcs.entity.QTerms.terms;
import static com.project.kcs.entity.QUser.user;
import static com.project.kcs.entity.QUserTerms.userTerms;

@Repository
@RequiredArgsConstructor
public class UserTermsRepositoryImpl implements UserTermsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SearchUserTermsResponse> findSearchUserTermsResponseListByUser(User finduser) {
        return queryFactory.select(
                        Projections.constructor(
                                SearchUserTermsResponse.class,
                                userTerms.key,
                                Projections.constructor(
                                        SearchTermsResponse.class,
                                        terms.key,
                                        terms.type,
                                        terms.title,
                                        terms.content,
                                        terms.status
                                ),
                                userTerms.availableTime,
                                userTerms.termsAgreeType
                        )
                )
                .from(userTerms)
                .join(userTerms.user, QUser.user)
                .join(userTerms.terms, terms)
                .where(user.eq(finduser))
                .fetch();
    }

}
