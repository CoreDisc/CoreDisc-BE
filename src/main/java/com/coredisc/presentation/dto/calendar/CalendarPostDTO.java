package com.coredisc.presentation.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CalendarPostDTO {
    private Long postId;
    private LocalDateTime createdAt;
}
