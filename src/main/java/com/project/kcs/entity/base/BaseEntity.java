package com.project.kcs.entity.base;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @CreatedBy
    @Column(name = "CREATED_BY",updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "MODIFIED_BY")
    private String lastModifiedBy;

    @CreatedDate
    @Column(name = "CREATED_DATE", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "MODIFIED_DATE")
    private LocalDateTime ModifiedDate;

}
