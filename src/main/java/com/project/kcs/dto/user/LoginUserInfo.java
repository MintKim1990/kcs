package com.project.kcs.dto.user;

import com.project.kcs.entity.constant.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;

@Data
@AllArgsConstructor
public class LoginUserInfo {

    private Long key;
    private String userName;
    private String birthDate;
    private String gender;
    private String name;
    private String hptel;
    private String identificationInfo;
    private UserStatus userStatus;

}
