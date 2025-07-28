package com.coredisc.presentation.dto.auth;

import lombok.Getter;

@Getter
public class NaverUserInfo {

    private Response response;

    @Getter
    public static class Response {

        private String name;
        private String email;
    }
}
