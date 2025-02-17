@Tag(name="Comment", description = "댓글 API")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentInfoService infoService;
    private final CommentSaveService saveService;
    private final CommentDeleteService deleteService;
    private final CommentValidator validator;
    private final Utils utils;

    /**
     * 댓글 작성 API
     * - HTTP Method: POST
     * - 요청 본문(@RequestBody)으로 데이터를 전달하여 새로운 댓글을 작성함
     */
    @Operation(summary = "댓글 작성", method = "POST")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name="mode", required = true, description = "write로 고정", example = "write"),
            @Parameter(name="boardDataSeq", required = true, description = "게시글 등록 번호"),
            @Parameter(name="commenter", required = true, description = "작성자", example = "작성자01"),
            @Parameter(name="content", required = true, description = "댓글 내용")
    })
    @PostMapping
    public JSONData write(@RequestBody @Valid RequestComment form, Errors errors) {
        return save(form, errors);
    }

    /**
     * 댓글 수정 API
     * - HTTP Method: PATCH
     * - 기존 댓글 데이터를 수정함
     */
    @Operation(summary = "댓글 수정", method = "PATCH")
    @ApiResponse(responseCode = "200")
    @Parameters({
            @Parameter(name="mode", required = true, description = "update로 고정", example = "update"),
            @Parameter(name="seq", required = true, description = "댓글 등록번호", example = "100"),
            @Parameter(name="commenter", required = true, description = "작성자", example = "작성자01"),
            @Parameter(name="content", required = true, description = "댓글 내용")
    })
    @PatchMapping
    public JSONData update(@RequestBody @Valid RequestComment form, Errors errors) {
        return save(form, errors);
    }

    /**
     * 댓글 저장 로직 (작성 및 수정 공통)
     */
    public JSONData save(RequestComment form, Errors errors) {
        validator.validate(form, errors);

        if (errors.hasErrors()) {
            throw new BadRequestException(utils.getErrorMessages(errors));
        }

        saveService.save(form);

        List<CommentData> items = infoService.getList(form.getBoardDataSeq());

        return new JSONData(items);
    }

    /**
     * 특정 댓글 조회 API
     * - HTTP Method: GET
     * - 경로 변수(@PathVariable)로 댓글 번호(seq)를 받아 해당 댓글 정보를 조회함
     */
    @Operation(summary = "댓글 하나 조회", description = "댓글 번호를 가지고 작성된 댓글을 조회 한다.", method = "GET")
    @ApiResponse(responseCode = "200")
    @Parameter(name="seq", required = true, description = "경로변수, 댓글 등록 번호")
    @GetMapping("/info/{seq}")
    public JSONData getInfo(@PathVariable("seq") Long seq) {
        CommentData item = infoService.get(seq);
        return new JSONData(item);
    }

    /**
     * 특정 게시글의 모든 댓글 조회 API
     * - HTTP Method: GET
     * - 게시글 번호(bSeq)를 받아 해당 게시글의 모든 댓글을 조회함
     */
    @Operation(summary = "댓글 조회", description = "게시글 번호를 가지고 작성된 댓글 목록을 조회 한다.", method = "GET")
    @ApiResponse(responseCode = "200")
    @Parameter(name="bSeq", required = true, description = "경로변수, 게시글 번호")
    @GetMapping("/list/{bSeq}")
    public JSONData getList(@PathVariable("bSeq") Long bSeq) {
        List<CommentData> items = infoService.getList(bSeq);
        return new JSONData(items);
    }

    /**
     * 댓글 삭제 API
     * - HTTP Method: DELETE
     * - 댓글 번호(seq)를 받아 해당 댓글을 삭제함
     */
    @Operation(summary = "댓글 하나 삭제", description = "댓글 번호를 가지고 작성된 댓글을 삭제한다.", method = "DELETE")
    @ApiResponse(responseCode = "200")
    @Parameter(name="seq", required = true, description = "경로변수, 댓글 등록 번호")
    @DeleteMapping("/{seq}")
    public JSONData delete(@PathVariable("seq") Long seq) {
        Long bSeq = deleteService.delete(seq);
        List<CommentData> items = infoService.getList(bSeq);
        return new JSONData(items);
    }
}
