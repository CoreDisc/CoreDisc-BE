package com.coredisc.domain.common.enums;

public enum NotificationType {
    DAILY_REMINDER, //질문 답변 해줘 리마인더
    UNANSWERED_QUESTION, //질문 답변 마무리해줘 리마인더
    MONTHLY_REPORT, //한 달 리포트
    FOLLOW, //팔로우 알림 (누가 날 팔로우 했어요)
    SHARED_SAVED, //공유 질문 저장 알림 (내가 작성한 공유 질문 누가 저장했어요)
    COMMENT, // 댓글 알림
    COMMENT_REPLY, // 대댓글 알림
    LIKE, // 좋아요 알림
    TEMP_POSTS // 임시저장 알림
}
