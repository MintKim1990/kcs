package com.project.kcs.response;

import com.project.kcs.constant.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {

    private ResponseCode code;
    private T content;

    public Response(ResponseCode code) {
        this.code = code;
    }
}
