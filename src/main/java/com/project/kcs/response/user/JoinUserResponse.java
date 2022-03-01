package com.project.kcs.response.user;

import com.project.kcs.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 사용자 등록 응답 VO
 */
@Data
@AllArgsConstructor
public class JoinUserResponse {

    private Long key;
    private String userId;
    private String userName;
    private String birthDate;
    private String gender;
    private String name;
    private String hptel;

    public static JoinUserResponse createUserResponse(User user) {
        return new JoinUserResponse(
                user.getKey(),
                user.getUserId(),
                user.getUserName(),
                user.getBirthDate(),
                user.getGender(),
                user.getName(),
                user.getHptel()
        );
    }
}
