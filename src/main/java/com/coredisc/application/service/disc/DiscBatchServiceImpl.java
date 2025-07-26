package com.coredisc.application.service.disc;

import com.coredisc.common.util.DiscCreationHelper;
import com.coredisc.domain.member.Member;
import com.coredisc.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
@Slf4j
public class DiscBatchServiceImpl implements DiscBatchService {

    private final MemberRepository memberRepository;
    private final DiscCreationHelper discCreationHelper;

    @Override
    public void generateDiscsForMonth(LocalDate targetMonth) {
        int page = 0;
        int size = 100; // 한 번에 처리할 건수 조절

        int success = 0;
        int skipped = 0;
        int failed = 0;

        Page<Member> memberPage;

        do {
            memberPage = memberRepository.findAllForDiscCreation(PageRequest.of(page, size));
            for (Member member : memberPage.getContent()) {
                try {
                    boolean created = discCreationHelper.ensureCurrentMonthDisc(member);
                    if (created) {
                        success++;
                    } else {
                        skipped++;
                    }
                } catch (Exception e) {
                    log.warn("⚠️ 디스크 생성 실패 - memberId: {}, 사유: {}", member.getId(), e.getMessage());
                    failed++;
                }
            }
            page++;
        } while (memberPage.hasNext());

        log.info("📊 디스크 생성 요약 - 성공: {}, 건너뜀: {}, 실패: {}", success, skipped, failed);
    }
}
