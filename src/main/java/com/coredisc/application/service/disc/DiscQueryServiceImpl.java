package com.coredisc.application.service.disc;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.DiscHandler;
import com.coredisc.common.util.DiscCreationHelper;
import com.coredisc.domain.disc.Disc;
import com.coredisc.domain.disc.DiscRepository;
import com.coredisc.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscQueryServiceImpl implements DiscQueryService {

    private final DiscRepository discRepository;
    private final DiscCreationHelper discCreationHelper;

    @Override
    public List<Disc> getMyDiscList(Member member) {
        List<Disc> discList = discRepository.findAllByMemberOrderByYearDescMonthDesc(member);

        // 디스크가 하나도 없을 때 (새로 회원가입한 경우 매월 1일에 자동생성되는 디스크가 없음)
        if (discList.isEmpty()) {
            // 이번 달 디스크 생성
            discCreationHelper.ensureCurrentMonthDisc(member);

            // 다시 조회
            discList = discRepository.findAllByMemberOrderByYearDescMonthDesc(member);

            // 그래도 없으면 예외 처리
            if (discList.isEmpty()) {
                throw new DiscHandler(ErrorStatus.DISC_NOT_FOUND);
            }
        }

        return discList;
    }

    @Override
    public Disc getDisc(Long discId, Member member) {
        return discRepository.findByIdAndMember(discId, member).orElseThrow(() -> new DiscHandler(ErrorStatus.DISC_NOT_FOUND));
    }
}