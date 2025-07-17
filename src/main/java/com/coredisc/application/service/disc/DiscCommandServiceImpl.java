package com.coredisc.application.service.disc;

import com.coredisc.common.apiPayload.status.ErrorStatus;
import com.coredisc.common.exception.handler.DiscHandler;
import com.coredisc.domain.common.enums.DiscCoverColor;
import com.coredisc.domain.disc.Disc;
import com.coredisc.domain.disc.DiscRepository;
import com.coredisc.domain.member.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DiscCommandServiceImpl implements DiscCommandService {

    private final DiscRepository discRepository;

    @Override
    public Disc updateDiscCoverImage(Long discId, String coverImageUrl, Member member) {
        Disc disc = discRepository.findByIdAndMember(discId, member)
                .orElseThrow(() -> new DiscHandler(ErrorStatus.DISC_NOT_FOUND));

        disc.setCoverImgUrl(coverImageUrl);
        return discRepository.save(disc);
    }

    @Override
    public Disc updateDiscCoverColor(Long discId, DiscCoverColor coverColor, Member member) {
        Disc disc = discRepository.findByIdAndMember(discId, member)
                .orElseThrow(() -> new DiscHandler(ErrorStatus.DISC_NOT_FOUND));

        disc.setCoverColor(coverColor);
        return discRepository.save(disc);
    }
}
