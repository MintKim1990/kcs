package com.project.kcs.repository.company;

import com.project.kcs.entity.User;
import com.project.kcs.response.company.SearchCompanyResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.project.kcs.entity.QCompany.company;

@Repository
@RequiredArgsConstructor
public class CompanyRepositoryImpl implements CompanyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<SearchCompanyResponse> findCompanyByCompanyKeyAndUser(Long key, User user) {
        return Optional.ofNullable(
                queryFactory.select(
                                Projections.constructor(
                                        SearchCompanyResponse.class,
                                        company.key,
                                        company.businessNumber,
                                        company.ownerName,
                                        company.foundationDate,
                                        company.companyName,
                                        company.corporationNumber,
                                        company.companyTelephone
                                )
                        )
                        .from(company)
                        .where(company.key.eq(key).and(company.user.eq(user)))
                        .fetchOne()
        );
    }

    @Override
    public List<SearchCompanyResponse> findCompanyListByUser(User user) {
        return queryFactory.select(
                        Projections.constructor(
                                SearchCompanyResponse.class,
                                company.key,
                                company.businessNumber,
                                company.ownerName,
                                company.foundationDate,
                                company.companyName,
                                company.corporationNumber,
                                company.companyTelephone
                        )
                )
                .from(company)
                .where(company.user.eq(user))
                .fetch();
    }

}
