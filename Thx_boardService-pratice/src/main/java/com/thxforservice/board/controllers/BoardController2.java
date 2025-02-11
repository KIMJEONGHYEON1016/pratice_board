//package com.thxforservice.board.controllers;
//
//import com.thxforservice.board.constants.DeleteStatus;
//import com.thxforservice.board.entities.Board;
//import com.thxforservice.board.entities.BoardData;
//import com.thxforservice.board.services.BoardDeleteService;
//import com.thxforservice.board.services.BoardInfoService;
//import com.thxforservice.board.services.BoardSaveService;
//import com.thxforservice.board.services.BoardViewCountService;
//import com.thxforservice.board.services.config.BoardConfigInfoService;
//import com.thxforservice.board.validators.BoardValidator;
//import com.thxforservice.global.CommonSearch;
//import com.thxforservice.global.ListData;
//import com.thxforservice.global.Utils;
//import com.thxforservice.global.exceptions.BadRequestException;
//import com.thxforservice.global.rests.JSONData;
//import com.thxforservice.member.MemberUtil;
//import com.thxforservice.member.entities.Member;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.Parameters;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.validation.Errors;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Tag(name = "Board", description = "게시글 API")
//@RestController
//@RequiredArgsConstructor
//public class BoardController2 {
//    private final BoardConfigInfoService configInfoService;
//    private final BoardInfoService infoService;
//    private final BoardSaveService saveService;
//    private final BoardDeleteService deleteService;
//    private final BoardViewCountService viewCountService;
//    private final BoardValidator validator;
//    private final Utils utils;
//    private final MemberUtil memberUtil;
//
//    @Operation(summary = "게시판 설정 조회", method = "GET")
//    @ApiResponse(responseCode = "200", description = "게시판 ID(bid)로 설정 조회")
//    @Parameter(name="bid", required = true, description = "경로변수, 게시판 ID(bid)", example = "notice")
//    @GetMapping("/config/{bid}")
//    public JSONData getConfig(@PathVariable("bid") String bid) {
//        Board board = configInfoService.get(bid);
//        return new JSONData(board);
//    }
//
//    @Operation(summary = "게시글 작성", method = "POST")
//    @ApiResponse(responseCode = "201")
//    @PostMapping("/write/{bid}")
//    public JSONData write(@PathVariable("bid") String bid, @RequestBody @Valid RequestBoard form, Errors errors) {
//        form.setBid(bid);
//        form.setMode("write");
//        return save(form, errors);
//    }
//
//    @Operation(summary = "게시글 수정", method = "PATCH")
//    @ApiResponse(responseCode = "200")
//    @PatchMapping("/update/{seq}")
//    public JSONData update(@PathVariable("seq") Long seq, @RequestBody @Valid RequestBoard form, Errors errors) {
//        form.setSeq(seq);
//        form.setMode("update");
//        return save(form, errors);
//    }
//
//    // 글 작성, 수정 처리 (ResponseEntity 제거 후 JSONData만 반환)
//    private JSONData save(RequestBoard form, Errors errors) {
//        validator.validate(form, errors);
//
//        if (errors.hasErrors()) { // 검증 실패
//            throw new BadRequestException(utils.getErrorMessages(errors));
//        }
//
//        BoardData data = saveService.save(form);
//        data.setBoard(null);
//        data.setComments(null);
//
//        return new JSONData(data); // 단순 JSONData 반환
//    }
//
//    @Operation(summary = "게시글 하나 조회", method = "GET")
//    @ApiResponse(responseCode = "200")
//    @GetMapping("/info/{seq}")
//    public JSONData info(@PathVariable("seq") Long seq) {
//        BoardData item = infoService.get(seq);
//        viewCountService.update(seq); // 조회수 카운트
//        return new JSONData(item);
//    }
//
//    @Operation(summary = "게시글 목록", method = "GET")
//    @ApiResponse(responseCode = "200")
//    @GetMapping("/list/{bid}")
//    public JSONData list(@PathVariable("bid") String bid, @ModelAttribute BoardDataSearch search) {
//        ListData<BoardData> data = infoService.getList(bid, search);
//        return new JSONData(data);
//    }
//
//    @Operation(summary = "내 게시글 목록", method = "GET")
//    @ApiResponse(responseCode = "200")
//    @GetMapping("/mylist")
//    public JSONData myList(BoardDataSearch search) {
//        if (!memberUtil.isLogin()) {
//            return new JSONData(new ListData<>());
//        }
//
//        Member member = memberUtil.getMember();
//        search.setEmail(List.of(member.getEmail()));
//
//        ListData<BoardData> data = infoService.getList(search, DeleteStatus.UNDELETED);
//        return new JSONData(data);
//    }
//
//    @Operation(summary = "게시글 삭제")
//    @ApiResponse(responseCode = "200")
//    @DeleteMapping("/delete/{seq}")
//    public JSONData delete(@PathVariable("seq") Long seq) {
//        BoardData item = deleteService.delete(seq);
//        return new JSONData(item);
//    }
//
//    @Operation(summary = "찜한 게시글 목록 조회")
//    @ApiResponse(responseCode = "200")
//    @GetMapping("/wish")
//    @PreAuthorize("isAuthenticated()")
//    public JSONData wishList(CommonSearch search) {
//        ListData<BoardData> data = infoService.getWishList(search);
//        return new JSONData(data);
//    }
//
//    @Operation(summary = "모든 게시글 목록", method = "GET")
//    @ApiResponse(responseCode = "200")
//    @GetMapping("/posts")
//    public JSONData listAllPosts(
//            @RequestParam(value = "page", defaultValue = "1") int page,
//            @RequestParam(value = "limit", defaultValue = "10") int limit,
//            @ModelAttribute BoardDataSearch search) {
//
//        search.setPage(page);
//        search.setLimit(limit);
//
//        ListData<BoardData> data = infoService.getList(search, DeleteStatus.ALL);
//        return new JSONData(data);
//    }
//}
