//@Tag(name="Comment", description = "댓글 API") // API 문서화용 Swagger 애노테이션 (댓글 API 그룹 설정)
//@RestController // RESTful API 컨트롤러 선언 (@Controller + @ResponseBody 역할)
//@RequestMapping("/comment") // 이 컨트롤러의 기본 URL을 "/comment"로 설정
//@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성 (의존성 주입 간편화)
//public class CommentController {
//    private final CommentInfoService infoService; // 댓글 정보 조회 서비스
//    private final CommentSaveService saveService; // 댓글 저장 서비스
//    private final CommentDeleteService deleteService; // 댓글 삭제 서비스
//    private final CommentValidator validator; // 댓글 데이터 유효성 검사
//    private final Utils utils; // 공통 유틸리티 클래스
//
//    /**
//     * 댓글 작성 API
//     * - HTTP Method: POST
//     * - 요청 본문(@RequestBody)으로 데이터를 전달하여 새로운 댓글을 작성함
//     */
//    @Operation(summary = "댓글 작성", method = "POST") // Swagger 문서용 주석 (API 설명)
//    @ApiResponse(responseCode = "200") // 응답 코드 200일 때 정상 처리됨을 명시
//    @Parameters({ // API 요청 시 필요한 파라미터 목록 설명 (Swagger 문서화용)
//            @Parameter(name="mode", required = true, description = "write로 고정", example = "write"),
//            @Parameter(name="boardDataSeq", required = true, description = "게시글 등록 번호"),
//            @Parameter(name="commenter", required = true, description = "작성자", example = "작성자01"),
//            @Parameter(name="content", required = true, description = "댓글 내용")
//    })
//    @PostMapping // HTTP POST 요청을 처리 (새로운 댓글 작성)
//    public JSONData write(@RequestBody @Valid RequestComment form, Errors errors) { // 요청 데이터를 JSON 형식으로 받음
//        return save(form, errors); // 작성 로직은 save() 메서드 재사용
//    }
//
//    /**
//     * 댓글 수정 API
//     * - HTTP Method: PATCH
//     * - 기존 댓글 데이터를 수정함
//     */
//    @Operation(summary = "댓글 수정", method = "PATCH") // API 문서화용 주석
//    @ApiResponse(responseCode = "200")
//    @Parameters({
//            @Parameter(name="mode", required = true, description = "update로 고정", example = "update"),
//            @Parameter(name="seq", required = true, description = "댓글 등록번호", example = "100"),
//            @Parameter(name="commenter", required = true, description = "작성자", example = "작성자01"),
//            @Parameter(name="content", required = true, description = "댓글 내용")
//    })
//    @PatchMapping // HTTP PATCH 요청을 처리 (기존 댓글 수정)
//    public JSONData update(@RequestBody @Valid RequestComment form, Errors errors) {
//        return save(form, errors); // 수정 로직도 save() 메서드 재사용
//    }
//
//    /**
//     * 댓글 저장 로직 (작성 및 수정 공통)
//     */
//    public JSONData save(RequestComment form, Errors errors) {
//        validator.validate(form, errors); // 유효성 검사 실행
//
//        if (errors.hasErrors()) { // 에러가 있으면 예외 발생
//            throw new BadRequestException(utils.getErrorMessages(errors));
//        }
//
//        saveService.save(form); // 댓글 저장 처리
//
//        List<CommentData> items = infoService.getList(form.getBoardDataSeq()); // 해당 게시글의 댓글 목록 조회
//
//        return new JSONData(items); // JSON 응답 데이터 반환
//    }
//
//    /**
//     * 특정 댓글 조회 API
//     * - HTTP Method: GET
//     * - 경로 변수(@PathVariable)로 댓글 번호(seq)를 받아 해당 댓글 정보를 조회함
//     */
//    @Operation(summary = "댓글 하나 조회", description = "댓글 번호를 가지고 작성된 댓글을 조회 한다.", method = "GET")
//    @ApiResponse(responseCode = "200")
//    @Parameter(name="seq", required = true, description = "경로변수, 댓글 등록 번호")
//    @GetMapping("/info/{seq}") // URL 경로 변수로 댓글 번호를 받아 조회
//    public JSONData getInfo(@PathVariable("seq") Long seq) {
//        CommentData item = infoService.get(seq); // 특정 댓글 정보 조회
//        return new JSONData(item); // JSON 응답 반환
//    }
//
//    /**
//     * 특정 게시글의 모든 댓글 조회 API
//     * - HTTP Method: GET
//     * - 게시글 번호(bSeq)를 받아 해당 게시글의 모든 댓글을 조회함
//     */
//    @Operation(summary = "댓글 조회", description = "게시글 번호를 가지고 작성된 댓글 목록을 조회 한다.", method = "GET")
//    @ApiResponse(responseCode = "200")
//    @Parameter(name="bSeq", required = true, description = "경로변수, 게시글 번호")
//    @GetMapping("/list/{bSeq}") // 특정 게시글의 댓글 리스트 조회
//    public JSONData getList(@PathVariable("bSeq") Long bSeq) {
//        List<CommentData> items = infoService.getList(bSeq); // 해당 게시글의 댓글 목록 조회
//        return new JSONData(items); // JSON 응답 반환
//    }
//
//    /**
//     * 댓글 삭제 API
//     * - HTTP Method: DELETE
//     * - 댓글 번호(seq)를 받아 해당 댓글을 삭제함
//     */
//    @Operation(summary = "댓글 하나 삭제", description = "댓글 번호를 가지고 작성된 댓글을 삭제한다.", method = "DELETE")
//    @ApiResponse(responseCode = "200")
//    @Parameter(name="seq", required = true, description = "경로변수, 댓글 등록 번호")
//    @DeleteMapping("/{seq}") // HTTP DELETE 요청을 처리
//    public JSONData delete(@PathVariable("seq") Long seq) {
//        Long bSeq = deleteService.delete(seq); // 댓글 삭제 처리 후 해당 게시글 번호 반환
//        List<CommentData> items = infoService.getList(bSeq); // 해당 게시글의 댓글 목록 조회
//        return new JSONData(items); // JSON 응답 반환
//    }
//}
