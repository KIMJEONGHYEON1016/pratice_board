//@Tag(name="BoardAdmin", description = "게시판 관리") // Swagger API 문서화 (게시판 관리 기능 그룹)
//@RestController // REST API 컨트롤러 (JSON 데이터 반환)
//@RequestMapping("/admin") // 기본 URL을 "/admin"으로 설정
//@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성 (의존성 주입을 편하게 하기 위함)
//public class BoardAdminController {
//
//    private final BoardConfigSaveService configSaveService; // 게시판 설정 저장 서비스
//    private final BoardConfigInfoService configInfoService; // 게시판 설정 조회 서비스
//    private final BoardConfigValidator configValidator; // 게시판 설정 유효성 검사기
//    private final BoardConfigDeleteService boardConfigDeleteService; // 게시판 삭제 서비스
//    private final HttpServletRequest request; // HTTP 요청 정보를 담고 있는 객체
//    private final Utils utils; // 공통 유틸리티 클래스
//
//    /**
//     * 게시판 등록/수정 API
//     * - HTTP Method: POST (등록) / PATCH (수정)
//     * - 게시판 정보를 저장하거나 수정하는 기능
//     */
//    @Operation(summary = "게시판 등록") // API 문서화 (Swagger)
//    @ApiResponse(responseCode = "201", description = "게시판 등록 성공시 201 반환") // 응답 코드 설명
//    @Parameters({
//            @Parameter(name="mode", required = true, description = "add - 등록, edit - 수정"), // 모드 (등록 또는 수정)
//            @Parameter(name="gid", required = true, description = "그룹 ID, 게시판 상단, 하단 이미지 관련"), // 그룹 ID
//            @Parameter(name="listOrder", description = "진열 가중치, 수치가 높을 수록 먼저 게시판 노출"), // 게시판 진열 순서
//            @Parameter(name="bid", required = true, description = "게시판 아이디"), // 게시판 ID
//            @Parameter(name="bname", required = true, description = "게시판 이름"), // 게시판 이름
//            @Parameter(name="active", example = "false", description = "게시판 사용 여부") // 게시판 활성화 여부
//    })
//    @RequestMapping(method={RequestMethod.POST, RequestMethod.PATCH}, path="/save") // POST(등록), PATCH(수정) 모두 허용
//    public ResponseEntity<Void> save(@RequestBody @Valid RequestBoardConfig form, Errors errors) {
//
//        String method = request.getMethod().toUpperCase(); // 현재 요청된 HTTP 메서드 가져오기
//        String mode = method.equals("POST") ? "add" : "edit"; // POST면 "add", PATCH면 "edit"으로 설정
//        form.setMode(mode); // 요청 객체에 모드 설정
//
//        configValidator.validate(form, errors); // 게시판 설정 유효성 검사
//
//        if (errors.hasErrors()) { // 에러 발생 시 예외 처리
//            throw new BadRequestException(utils.getErrorMessages(errors)); // 유효성 검사를 통과하지 못한 경우 예외 발생
//        }
//
//        configSaveService.save(form); // 게시판 저장 서비스 호출
//
//        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created 응답 반환
//    }
//
//    /**
//     * 특정 게시판 설정 조회 API
//     * - HTTP Method: GET
//     * - 게시판 아이디(bid)를 기준으로 설정 정보를 조회함
//     */
//    @Operation(summary = "게시판 설정 하나 조회", description = "설정 조회는 게시판 아이디(bid) 기준 입니다.") // API 문서화
//    @ApiResponse(responseCode = "200") // 정상 응답 코드
//    @Parameter(name="bid", required = true, description = "경로변수", example = "notice") // 요청 파라미터 설명
//    @GetMapping("/info/{bid}") // 특정 게시판 설정 조회
//    public JSONData info(@PathVariable("bid") String bid) {
//        Board board = configInfoService.get(bid); // 게시판 정보 조회
//        return new JSONData(board); // JSON 응답 반환
//    }
//
//    /**
//     * 게시판 목록 조회 API
//     * - HTTP Method: GET
//     * - 검색 조건을 기반으로 게시판 목록을 조회함
//     */
//    @Operation(summary = "게시판 목록 조회") // API 문서화
//    @ApiResponse(responseCode = "200", description = "items - 조회된 게시판 목록, pagination - 페이징 기초 데이터") // 응답 코드 설명
//    @Parameters({ // API 요청 시 사용 가능한 파라미터 설명
//            @Parameter(name="page", description = "페이지 번호", example = "1"), // 페이지 번호
//            @Parameter(name="limit", description = "한 페이지당 게시판 개수", example = "20"), // 한 페이지당 게시판 개수
//            @Parameter(name="sopt", description = "검색 옵션", example = "ALL"), // 검색 옵션 (예: ALL, NAME 등)
//            @Parameter(name="skey", description = "검색 키워드"), // 검색 키워드
//            @Parameter(name="bid", description = "게시판 ID"), // 특정 게시판 ID
//            @Parameter(name="bids", description = "게시판 ID 목록"), // 여러 게시판 ID 목록
//            @Parameter(name="bname", description = "게시판 이름"), // 게시판 이름으로 검색
//            @Parameter(name="active", description = "게시판 사용 여부", example = "true") // 게시판 활성화 여부
//    })
//    @GetMapping("/list") // 게시판 목록 조회
//    public JSONData list(@ModelAttribute BoardSearch search) {
//        ListData data = configInfoService.getList(search, true); // 게시판 목록 조회 서비스 호출
//        return new JSONData(data); // JSON 응답 반환
//    }
//
//    /**
//     * 게시판 삭제 API
//     * - HTTP Method: DELETE
//     * - 게시판 아이디(bid)를 기준으로 특정 게시판을 삭제함
//     */
//    @Operation(summary = "게시판 삭제", description = "게시판 아이디(bid) 기준으로 게시판을 삭제합니다.") // API 문서화
//    @ApiResponse(responseCode = "200", description = "게시판 삭제 성공") // 성공 응답 코드
//    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시판") // 실패 응답 코드 (게시판이 없을 경우)
//    @Parameter(name = "bid", required = true, description = "게시판 아이디") // 요청 파라미터 설명
//    @DeleteMapping("/delete/{bid}") // 게시판 삭제 엔드포인트
//    public void deleteBoard(@PathVariable("bid") String bid) {
//        boardConfigDeleteService.delete(bid); // 게시판 삭제 처리
//    }
//}
