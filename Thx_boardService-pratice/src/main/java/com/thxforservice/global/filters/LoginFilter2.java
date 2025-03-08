//package com.thxforservice.global.filters;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.thxforservice.global.Utils;
//import com.thxforservice.global.rests.JSONData;
//import com.thxforservice.member.MemberInfo;
//import com.thxforservice.member.constants.Authority;
//import com.thxforservice.member.entities.Member;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.filter.GenericFilterBean;
//
//import java.io.IOException;
//import java.util.List;
//
///**
// * JWT 기반 로그인 필터
// *
// * 이 필터는 클라이언트의 요청에서 JWT 토큰을 추출하여 사용자 인증을 수행하는 역할을 합니다.
// *
// * 1. 요청에서 JWT 토큰을 추출
// * 2. 토큰을 기반으로 사용자 정보를 조회
// * 3. 조회된 정보를 바탕으로 Spring Security에 사용자 인증 정보 설정
// */
//@Component
//@RequiredArgsConstructor
//public class LoginFilter extends GenericFilterBean {
//
//    private final DiscoveryClient discoveryClient;  // 마이크로서비스의 서비스 검색 기능을 제공하는 DiscoveryClient
//    private final RestTemplate restTemplate;        // 외부 API 호출을 위한 RestTemplate
//    private final ObjectMapper om;                  // JSON 데이터 변환을 위한 ObjectMapper
//    private final Utils utils;                      // 유틸리티 클래스 (API URL 조합 등을 수행)
//
//    /**
//     * 요청을 필터링하여 JWT 기반 로그인 처리 수행
//     *
//     * @param request  클라이언트의 요청
//     * @param response 서버의 응답
//     * @param chain    필터 체인
//     */
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        // 요청에서 JWT 토큰을 가져옴
//        String token = getToken(request);
//
//        // 토큰이 존재하면 로그인 프로세스 수행
//        if (StringUtils.hasText(token)) {
//            loginProcess(token);
//        }
//
//        // 다음 필터로 요청을 전달
//        chain.doFilter(request, response);
//    }
//
//    /**
//     * JWT 토큰을 이용하여 회원 정보를 조회하고 로그인 처리
//     *
//     * @param token 클라이언트가 제공한 JWT 토큰
//     */
//    private void loginProcess(String token) {
//        try {
//            // API 서버 주소 설정 (회원 서비스의 /account 엔드포인트 호출)
//            String apiUrl = utils.url("/account", "memberservice");
//
//            // HTTP 요청 헤더에 Authorization: Bearer {token} 추가
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(token);
//            HttpEntity<Void> entity = new HttpEntity<>(headers);
//
//            // 회원 정보 조회 요청 (JWT 토큰 검증 및 사용자 정보 가져오기)
//            ResponseEntity<JSONData> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, JSONData.class);
//
//            // 응답이 성공적일 경우 로그인 처리
//            if (response.getStatusCode().is2xxSuccessful()) {
//                JSONData data = response.getBody();
//                if (data != null && data.isSuccess()) {
//
//                    // JSON 데이터를 Member 객체로 변환
//                    String json = om.writeValueAsString(data.getData());
//                    Member member = om.readValue(json, Member.class);
//
//                    // 회원의 권한 정보 설정
//                    Authority authority = member.getAuthority();
//                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(authority.name()));
//
//                    // 특정 권한(예: 상담사)일 경우 추가 권한 부여
//                    if (authority == Authority.COUNSELOR) {
//                        authorities.add(new SimpleGrantedAuthority(Authority.COUNSELOR.name()));
//                    }
//
//                    // MemberInfo 객체 생성 (사용자 인증 정보 포함)
//                    MemberInfo memberInfo = MemberInfo.builder()
//                            .email(member.getEmail())         // 사용자 이메일
//                            .password(member.getPassword())   // 사용자 패스워드
//                            .member(member)                   // 회원 정보
//                            .authorities(authorities)         // 부여된 권한 목록
//                            .build();
//
//                    // Spring Security 인증 객체 생성 (사용자 정보, 토큰, 권한 정보 포함)
//                    Authentication authentication = new UsernamePasswordAuthenticationToken(
//                            memberInfo, token, memberInfo.getAuthorities());
//
//                    // SecurityContextHolder에 인증 정보 설정 (로그인 처리 완료)
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            }
//
//        } catch (JsonProcessingException e) {
//            e.printStackTrace(); // JSON 변환 오류 발생 시 예외 처리
//        }
//    }
//
//    /**
//     * 요청에서 JWT 토큰을 추출하는 메서드
//     *
//     * 1. HTTP 요청 헤더의 "Authorization" 값을 확인
//     * 2. "Bearer " 접두사가 포함되어 있으면 해당 부분을 제거하고 토큰 반환
//     * 3. 요청 파라미터에서 "token" 값을 확인 (필요할 경우 지원)
//     *
//     * @param request 클라이언트의 요청
//     * @return 추출된 JWT 토큰 (없으면 null 반환)
//     */
//    private String getToken(ServletRequest request) {
//        HttpServletRequest req = (HttpServletRequest) request;
//
//        // Authorization 헤더에서 토큰 추출
//        String bearerToken = req.getHeader("Authorization");
//        if (StringUtils.hasText(bearerToken) && bearerToken.toUpperCase().startsWith("BEARER ")) {
//            return bearerToken.substring(7).trim();  // "Bearer " 이후의 토큰 값만 반환
//        }
//
//        // 요청 파라미터에서 token 값 추출
//        String token = req.getParameter("token");
//        if (StringUtils.hasText(token)) {
//            return token;
//        }
//
//        // 토큰이 없으면 null 반환
//        return null;
//    }
//}
