// 게시판 관리 기능을 제공하는 관리자 전용 컨트롤러
@Tag(name = "BoardAdmin", description = "게시판 관리")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BoardAdminController {

    // 게시판 관련 서비스 및 유틸리티 클래스들 의존성 주입
    private final BoardConfigSaveService configSaveService;
    private final BoardConfigInfoService configInfoService;
    private final BoardConfigValidator configValidator;
    private final BoardConfigDeleteService boardConfigDeleteService;
    private final HttpServletRequest request;
    private final Utils utils;

    // 게시판 등록 또는 수정 API (POST - 등록, PATCH - 수정)
    @Operation(summary = "게시판 등록")
    @ApiResponse(responseCode = "201", description = "게시판 등록 성공시 201")
    @Parameters({
            @Parameter(name = "mode", required = true, description = "add - 등록, edit - 수정"),
            @Parameter(name = "gid", required = true, description = "그룹 ID"),
            @Parameter(name = "listOrder", description = "진열 가중치"),
            @Parameter(name = "bid", required = true, description = "게시판 아이디"),
            @Parameter(name = "bname", required = true, description = "게시판 이름"),
            @Parameter(name = "active", example = "false", description = "사용 여부")
    })
    @RequestMapping(method = {RequestMethod.POST, RequestMethod.PATCH}, path = "/save")
    public ResponseEntity<Void> save(@RequestBody @Valid RequestBoardConfig form, Errors errors) {

        // HTTP 메서드에 따라 등록(add)인지 수정(edit)인지 구분
        String method = request.getMethod().toUpperCase();
        String mode = method.equals("POST") ? "add" : "edit";
        form.setMode(mode);

        // 유효성 검사
        configValidator.validate(form, errors);
        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        // 저장 처리
        configSaveService.save(form);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 게시판 설정 단건 조회 API
    @Operation(summary = "게시판 설정 하나 조회", description = "게시판 ID로 설정 조회")
    @ApiResponse(responseCode = "200")
    @Parameter(name = "bid", required = true, description = "게시판 ID")
    @GetMapping("/info/{bid}")
    public JSONData info(@PathVariable("bid") String bid) {
        Board board = configInfoService.get(bid);
        return new JSONData(board);
    }

    // 게시판 설정 목록 조회 API
    @Operation(summary = "게시판 목록 조회")
    @ApiResponse(responseCode = "200", description = "items - 목록, pagination - 페이징 정보")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호"),
            @Parameter(name = "limit", description = "페이지당 레코드 수"),
            @Parameter(name = "sopt", description = "검색 옵션"),
            @Parameter(name = "skey", description = "검색 키워드"),
            @Parameter(name = "bid", description = "게시판 ID"),
            @Parameter(name = "bids", description = "게시판 ID 목록"),
            @Parameter(name = "bname", description = "게시판 이름"),
            @Parameter(name = "active", description = "사용 여부")
    })
    @GetMapping("/list")
    public JSONData list(@ModelAttribute BoardSearch search) {
        ListData data = configInfoService.getList(search, true);
        return new JSONData(data);
    }

    // 게시판 삭제 API
    @Operation(summary = "게시판 삭제", description = "게시판 ID로 삭제")
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @ApiResponse(responseCode = "404", description = "존재하지 않는 게시판")
    @Parameter(name = "bid", required = true, description = "게시판 아이디")
    @DeleteMapping("/delete/{bid}")
    public void deleteBoard(@PathVariable("bid") String bid) {
        boardConfigDeleteService.delete(bid);
    }
}
