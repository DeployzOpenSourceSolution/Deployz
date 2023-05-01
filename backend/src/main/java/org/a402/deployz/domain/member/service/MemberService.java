package org.a402.deployz.domain.member.service;

import static org.a402.deployz.global.security.jwt.JwtAuthenticationFilter.*;

import org.a402.deployz.domain.member.entity.Member;
import org.a402.deployz.domain.member.repository.MemberRepository;
import org.a402.deployz.domain.member.request.ReCreateTokenRequest;
import org.a402.deployz.domain.member.response.MemberInformationResponse;
import org.a402.deployz.domain.member.exception.MemberNotFoundException;

import org.a402.deployz.global.security.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Transactional(readOnly = true)
	public MemberInformationResponse findMemberInformation(final Long idx) {
		final Member member = memberRepository.findById(idx)
			.orElseThrow(MemberNotFoundException::new);

		return new MemberInformationResponse(member);
	}

	@Transactional(readOnly = true)
	public String reCreateToken(final ReCreateTokenRequest reCreateTokenRequest) {
		final Member member = memberRepository.findMemberByEmail(reCreateTokenRequest.getEmail())
			.orElseThrow(MemberNotFoundException::new);
		final String refreshToken = jwtTokenProvider.splitToken(reCreateTokenRequest.getRefreshToken());

		return BEARER + jwtTokenProvider.reCreateAccessToken(refreshToken, member);
	}

}
