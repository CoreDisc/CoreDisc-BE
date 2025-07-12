package com.coredisc.common.util;

import java.util.Random;

public class RandomNicknameGenerator {

    private RandomNicknameGenerator() {
        // 인스턴스화 방지
        throw new UnsupportedOperationException("Utility class");
    }

    // 형용사
    private static final String[] ADJECTIVES = {
            "엉뚱한", "날렵한", "비범한", "고요한", "수줍은", "우아한",
            "엉킨", "반짝이는", "용의주도한", "느긋한", "뒤죽박죽한",
            "심술맞은", "쌩쌩한", "반항적인", "몽환적인", "엉성한",
            "폭신폭신한", "통통 튀는", "끈질긴", "허둥지둥한", "행복한",
            "똑똑한", "즐거운", "강한", "빠른", "재치있는", "충성스러운",
            "멋진", "훌륭한", "즐거운", "아름다운", "기쁜", "사랑스러운",
            "감동적인", "뛰어난", "성실한", "창의적인", "자랑스러운", "유쾌한"
    };

    // 명사
    private static final String[] NOUNS = {
            "두더지", "나무늘보", "문어", "치와와", "돌고래", "다람쥐", "오소리",
            "딱따구리", "고릴라", "피카츄", "스핑크스", "치타냥", "사자", "호랑이",
            "독수리", "상어", "판다", "여우", "늑대", "용", "곰", "매", "강아지",
            "고양이", "토끼", "햄스터", "앵무새", "거북이", "고슴도치",
            "물고기", "말", "펭귄", "코알라", "기린", "수달", "코끼리"
    };

    private static final Random RANDOM = new Random();

    public static String generateRandomNickname() {

        // 랜덤 형용사
        String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];

        // 랜덤 명사
        String noun = NOUNS[RANDOM.nextInt(NOUNS.length)];

        // 랜덤 숫자
        String randomNum = String.valueOf(RANDOM.nextInt(999));

        return adjective + noun + randomNum;
    }
}
