//package com.thxforservice.board.controllers;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.thxforservice.file.entities.FileInfo;
//import jakarta.validation.constraints.*;
//import lombok.Data;
//
//import java.util.List;
//import java.util.UUID;
//
//@Data
//@JsonIgnoreProperties(ignoreUnknown = true) // JSON에서 정의되지 않은 필드는 무시하고 매핑
//public class RequestBoard {
//
//    private Long seq; // 글 번호 (수정 시 필요)
//
//    private String mode = "write"; // "write": 작성, "update": 수정
//
//    @NotBlank(message = "게시판 ID는 필수 입력값입니다.")
//    private String bid; // 게시판 ID (필수)

//    @JsonIgnore	//필드	특정 필드 하나만 무시
//    private String gid = UUID.randomUUID().toString(); // 그룹 ID (자동 생성)
//
//    private boolean notice; // 공지글 여부
//
//    private String category; // 게시판 카테고리 (선택)
//
//    @NotBlank(message = "작성자는 필수 입력값입니다.")
//    private String poster; // 작성자 (필수)
//
//    private String guestPw; // 비회원 비밀번호 (선택, 수정/삭제 시 필요)
//
//    @NotBlank(message = "제목은 필수 입력값입니다.")
//    @Size(min = 2, max = 100, message = "제목은 2~100자 이내로 입력해주세요.")
//    private String subject; // 게시글 제목 (필수, 길이 제한)
//
//    @NotBlank(message = "내용은 필수 입력값입니다.")
//    @Size(min = 10, message = "내용은 최소 10자 이상 입력해주세요.")
//    private String content; // 게시글 내용 (필수, 최소 10자)
//
//    @PositiveOrZero(message = "num1은 0 이상이어야 합니다.")
//    private Long num1; // 숫자값1 (0 이상 허용)
//
//    @PositiveOrZero(message = "num2은 0 이상이어야 합니다.")
//    private Long num2; // 숫자값2 (0 이상 허용)
//
//    @PositiveOrZero(message = "num3은 0 이상이어야 합니다.")
//    private Long num3; // 숫자값3 (0 이상 허용)
//
//    private String text1;
//    private String text2;
//    private String text3;
//
//    @Size(max = 500, message = "긴 글은 최대 500자까지 입력할 수 있습니다.")
//    private String longText1; // 긴 텍스트1 (최대 500자)
//
//    @Size(max = 500, message = "긴 글은 최대 500자까지 입력할 수 있습니다.")
//    private String longText2; // 긴 텍스트2 (최대 500자)
//
//    private List<FileInfo> editorImages; // 에디터 내 이미지 파일 리스트
//    private List<FileInfo> attachFiles; // 첨부 파일 리스트
//}
