package com.coredisc.common.util;

import com.coredisc.application.service.disc.DiscCommandService;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DiscCreationHelper {
    private final DiscCommandService discCommandService;

    public boolean ensureCurrentMonthDisc(Member member) {
        LocalDate now = LocalDate.now();
        return discCommandService.createDiscIfNotExists(member, now.getYear(), now.getMonthValue());
    }
}
