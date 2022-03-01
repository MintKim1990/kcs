package com.project.kcs.repository.terms;

import com.project.kcs.dto.user.LoginUserInfo;
import com.project.kcs.entity.Terms;
import com.project.kcs.entity.constant.TermsStatus;
import com.project.kcs.response.terms.SearchTermsResponse;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.project.kcs.entity.QTerms.terms;


@Repository
@RequiredArgsConstructor
public class TermsRepositoryImpl implements TermsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<SearchTermsResponse> findSearchTermsResponseById(Long key) {
        return Optional.ofNullable(
                queryFactory.select(
                                Projections.constructor(
                                        SearchTermsResponse.class,
                                        terms.key,
                                        terms.type,
                                        terms.title,
                                        terms.content,
                                        terms.status
                                )
                        )
                        .from(terms)
                        .where(terms.key.eq(key).and(terms.status.eq(TermsStatus.USE)))
                        .fetchOne()
        );
    }

    @Override
    public List<SearchTermsResponse> findSearchTermsResponseList() {
        return queryFactory.select(
                        Projections.constructor(
                                SearchTermsResponse.class,
                                terms.key,
                                terms.type,
                                terms.title,
                                terms.content,
                                terms.status
                        )
                )
                .from(terms)
                .fetch();
    }

}
