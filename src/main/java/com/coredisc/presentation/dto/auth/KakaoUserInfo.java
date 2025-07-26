package com.coredisc.presentation.dto.auth;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class KakaoUserInfo {

    @SerializedName("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    public static class KakaoAccount {

        private Profile profile;
        private String email;
    }

    @Getter
    public static class Profile {

        private String nickname;
    }
}
