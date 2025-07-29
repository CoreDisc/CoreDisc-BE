package com.coredisc.common.util;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.ReportStatHandler;
import com.coredisc.domain.common.enums.DiaryWhat;
import com.coredisc.domain.common.enums.DiaryWhere;
import com.coredisc.domain.common.enums.DiaryWho;

import java.util.Map;

public class DailyEnumMappingHelper {

    // label 매핑
    private static final Map<DiaryWho, String> diaryWhoLabelMap = Map.of(
            DiaryWho.ALONE, "혼자",
            DiaryWho.FRIEND, "친구",
            DiaryWho.FAMILY, "가족",
            DiaryWho.COLLEAGUE, "동료",
            DiaryWho.LOVER, "연인",
            DiaryWho.PET, "반려동물"
    );

    private static final Map<DiaryWhere, String> diaryWhereLabelMap = Map.of(
            DiaryWhere.HOME, "집",
            DiaryWhere.COMPANY, "회사",
            DiaryWhere.SCHOOL, "학교",
            DiaryWhere.CAFE, "카페",
            DiaryWhere.OUTDOOR, "야외",
            DiaryWhere.ON_THE_MOVE, "이동 중"
    );

    private static final Map<DiaryWhat, String> diaryWhatLabelMap = Map.of(
            DiaryWhat.WORK, "일",
            DiaryWhat.STUDY, "공부",
            DiaryWhat.EXERCISE, "운동",
            DiaryWhat.REST, "휴식",
            DiaryWhat.SLEEP, "수면",
            DiaryWhat.HOBBY, "취미"
    );

    // enum → selectedOption (1~6)
    public static int toSelectedOption(Enum<?> enumValue) {
        return enumValue.ordinal() + 1;
    }

    // selectedOption → enum
    public static Enum<?> fromSelectedOption(int dailyType, int selectedOption) {
        if (selectedOption < 1 || selectedOption > 6) {
            throw new ReportStatHandler(ErrorStatus.INVALID_SELECTED_OPTION);
        }

        return switch (dailyType) {
            case 1 -> DiaryWho.values()[selectedOption - 1];
            case 2 -> DiaryWhere.values()[selectedOption - 1];
            case 3 -> DiaryWhat.values()[selectedOption - 1];
            default -> throw new ReportStatHandler(ErrorStatus.INVALID_DAILY_TYPE);
        };
    }

    // enum → label
    public static String toLabel(Enum<?> enumValue) {
        if (enumValue instanceof DiaryWho who) {
            return diaryWhoLabelMap.get(who);
        } else if (enumValue instanceof DiaryWhere where) {
            return diaryWhereLabelMap.get(where);
        } else if (enumValue instanceof DiaryWhat what) {
            return diaryWhatLabelMap.get(what);
        } else {
            throw new ReportStatHandler(ErrorStatus.INVALID_LABEL_MAPPING);
        }
    }

    // label → enum
    public static Enum<?> fromLabel(int dailyType, String label) {
        return switch (dailyType) {
            case 1 -> findEnumByLabel(diaryWhoLabelMap, label);
            case 2 -> findEnumByLabel(diaryWhereLabelMap, label);
            case 3 -> findEnumByLabel(diaryWhatLabelMap, label);
            default -> throw new ReportStatHandler(ErrorStatus.INVALID_DAILY_TYPE);
        };
    }

    // selectedOption → label
    public static String toLabelFromSelectedOption(int dailyType, int selectedOption) {
        Enum<?> enumValue = fromSelectedOption(dailyType, selectedOption);
        return toLabel(enumValue);
    }

    // label → selectedOption
    public static int toSelectedOptionFromLabel(int dailyType, String label) {
        Enum<?> enumValue = fromLabel(dailyType, label);
        return toSelectedOption(enumValue);
    }

    // 질문 이름 가져오기
    public static String toDailyTypeName(int dailyType) {
        return switch (dailyType) {
            case 1 -> "누구와 가장 많이 있었을까요?";
            case 2 -> "어디에 가장 많이 있었을까요?";
            case 3 -> "무엇을 가장 많이 했을까요?";
            default -> throw new ReportStatHandler(ErrorStatus.INVALID_DAILY_TYPE);
        };
    }

    private static <E extends Enum<E>> E findEnumByLabel(Map<E, String> map, String label) {
        return map.entrySet().stream()
                .filter(entry -> entry.getValue().equals(label))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new ReportStatHandler(ErrorStatus.INVALID_LABEL_MAPPING));
    }
}