package com.thxforservice.board.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestBoardConfig {

    // 게시판 설정 모드 (기본값: "add" - 추가, "edit" - 수정 등)
    private String mode = "add";

    // 게시판 그룹 ID (자동 생성되는 UUID 값)
    private String gid = UUID.randomUUID().toString();

    // 게시판 진열 순서를 결정하는 가중치 (숫자가 높을수록 우선순위 ↑)
    private int listOrder;

    // 게시판 ID (필수 입력 값)
    @NotBlank
    private String bid;

    // 게시판 이름 (필수 입력 값)
    @NotBlank
    private String bname;

    // 게시판 사용 여부 (true: 사용, false: 미사용)
    private boolean active;

    // 한 페이지당 표시할 게시글 수 (기본값: 20)
    private int rowsPerPage = 20;

    // PC 환경에서 페이지네이션 블록 수 (기본값: 10)
    private int pageCountPc = 10;

    // 모바일 환경에서 페이지네이션 블록 수 (기본값: 5)
    private int pageCountMobile = 5;

    // 답글 기능 사용 여부 (true: 사용, false: 미사용)
    private boolean useReply;

    // 댓글 기능 사용 여부 (true: 사용, false: 미사용)
    private boolean useComment;

    // 에디터 사용 여부 (true: 사용, false: 미사용 - 일반 텍스트 입력)
    private boolean useEditor;

    // 이미지 업로드 기능 사용 여부
    private boolean useUploadImage;

    // 파일 업로드 기능 사용 여부
    private boolean useUploadFile;

    // 글 작성 후 이동 위치 설정 (기본값: "list")
    private String locationAfterWriting = "list";

    // 글 보기 페이지에서 하단 목록 표시 여부
    private boolean showListBelowView;

    // 게시판 스킨 설정 (기본값: "default")
    private String skin = "default";

    // 게시판 분류 (예: 공지사항, 자유게시판 등)
    private String category;

    // 게시판 권한 설정 - 글 목록 조회 가능 대상 (기본값: "ALL")
    private String listAccessType = "ALL";

    // 게시판 권한 설정 - 글 보기 가능 대상 (기본값: "ALL")
    private String viewAccessType = "ALL";

    // 게시판 권한 설정 - 글 작성 가능 대상 (기본값: "ALL")
    private String writeAccessType = "ALL";

    // 게시판 권한 설정 - 답글 작성 가능 대상 (기본값: "ALL")
    private String replyAccessType = "ALL";

    // 게시판 권한 설정 - 댓글 작성 가능 대상 (기본값: "ALL")
    private String commentAccessType = "ALL";

    // 게시판 상단에 표시할 HTML 코드
    private String htmlTop;

    // 게시판 하단에 표시할 HTML 코드
    private String htmlBottom;

    // 아래 주석 처리된 필드는 Transient 속성으로 DB 저장 대상이 아님
    // 게시판 상단에 표시할 이미지 파일 리스트
    // @Transient
    // private List<FileInfo> htmlTopImages;

    // 게시판 하단에 표시할 이미지 파일 리스트
    // @Transient
    // private List<FileInfo> htmlBottomImages;
}
