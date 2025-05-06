// 게시글 관련 API를 처리하는 컨트롤러 클래스
//@RestController: JSON 형태의 응답을 반환하는 컨트롤러
//@RequiredArgsConstructor: 생성자 주입 방식으로 필요한 의존성 주입
@Tag(name = "Board", description = "게시글 API")
//@RestController
//@RequiredArgsConstructor
public class BoardController2 {

    // 필요한 서비스 클래스 의존성 주입
    private final BoardConfigInfoService configInfoService;
    private final BoardInfoService infoService;
    private final BoardSaveService saveService;
    private final BoardDeleteService deleteService;
    private final BoardViewCountService viewCountService;
    private final BoardValidator validator;
    private final Utils utils;
    private final MemberUtil memberUtil;

    // 게시판 설정 조회 API
    @Operation(summary = "게시판 설정 조회", method = "GET")
    @ApiResponse(responseCode = "200", description = "게시판 ID(bid)로 설정 조회")
    @Parameter(name = "bid", required = true, description = "게시판 ID")
    @GetMapping("/config/{bid}")
    public JSONData getConfig(@PathVariable String bid) {
        Board board = configInfoService.get(bid);
        return new JSONData(board);
    }

    // 게시글 작성 API
    @Operation(summary = "게시글 작성", method = "POST")
    @ApiResponse(responseCode = "201")
    @PostMapping("/write/{bid}")
    public JSONData write(@PathVariable String bid, @RequestBody @Valid RequestBoard form, Errors errors) {
        form.setBid(bid);
        form.setMode("write");
        return save(form, errors);
    }

    // 게시글 수정 API
    @Operation(summary = "게시글 수정", method = "PATCH")
    @ApiResponse(responseCode = "200", description = "게시글 수정 성공")
    @Parameters({
            @Parameter(name = "seq", required = true, description = "수정할 게시글의 고유 번호"),
            @Parameter(name = "form", required = true, description = "수정할 게시글 데이터")
    })
    @PatchMapping("/update/{seq}")
    public JSONData update(@PathVariable Long seq, @RequestBody @Valid RequestBoard form, Errors errors) {
        form.setSeq(seq);
        form.setMode("update");
        return save(form, errors);
    }

    // 게시글 저장 로직 - 작성/수정에서 공통 호출
    private JSONData save(RequestBoard form, Errors errors) {
        validator.validate(form, errors);
        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }
        BoardData data = saveService.save(form);
        data.setBoard(null); // 순환 참조 방지
        data.setComments(null);
        return new JSONData(data);
    }

    // 게시글 단건 조회 API
    @Operation(summary = "게시글 하나 조회", method = "GET")
    @ApiResponse(responseCode = "200")
    @GetMapping("/info/{seq}")
    public JSONData info(@PathVariable Long seq) {
        BoardData item = infoService.get(seq);
        viewCountService.update(seq); // 조회수 증가
        return new JSONData(item);
    }

    // 게시글 목록 조회 API
    @Operation(summary = "게시글 목록", method = "GET")
    @ApiResponse(responseCode = "200")
    @GetMapping("/list/{bid}")
    public JSONData list(@PathVariable String bid, @ModelAttribute BoardDataSearch search) {
        ListData<BoardData> data = infoService.getList(bid, search);
        return new JSONData(data);
    }

    // 게시글 삭제 API
    @Operation(summary = "게시글 삭제")
    @ApiResponse(responseCode = "200")
    @DeleteMapping("/delete/{seq}")
    public JSONData delete(@PathVariable Long seq) {
        BoardData item = deleteService.delete(seq);
        return new JSONData(item);
    }

    // 모든 게시글 조회 API (페이지/제한 포함)
    @Operation(summary = "모든 게시글 목록", method = "GET")
    @ApiResponse(responseCode = "200")
    @GetMapping("/posts")
    public JSONData listAllPosts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @ModelAttribute BoardDataSearch search) {
        search.setPage(page);
        search.setLimit(limit);
        ListData<BoardData> data = infoService.getList(search, DeleteStatus.ALL);
        return new JSONData(data);
    }
}