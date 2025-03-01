//package com.thxforservice.board.controllers;
//
//import com.thxforservice.board.services.WishListService;
//import com.thxforservice.global.rests.JSONData;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
///**
// * 위시리스트 관련 API 컨트롤러
// * - 사용자가 원하는 항목을 추가/삭제하고 조회할 수 있도록 함
// */
//@RestController // RESTful API 컨트롤러 선언 (@Controller + @ResponseBody 역할)
//@RequestMapping("/wish") // 기본 URL을 /wish로 설정
//@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성 (의존성 주입 간편화)
//public class WishListController {
//
//    private final WishListService service; // 위시리스트 서비스 (비즈니스 로직 담당)
//
//    /**
//     * 위시리스트 목록 조회 API
//     * - HTTP Method: GET
//     * - 사용자의 위시리스트에 저장된 항목 목록을 반환함
//     */
//    @GetMapping("/list") // HTTP GET 요청을 처리
//    public JSONData list() {
//        List<Long> seqs = service.getList(); // 위시리스트 항목 ID 목록 조회
//        return new JSONData(seqs); // JSON 응답 반환
//    }
//
//    /**
//     * 위시리스트에 항목 추가 API
//     * - HTTP Method: POST (RESTful 방식)
//     * - 특정 항목(seq)을 위시리스트에 추가함
//     */
//    @PostMapping("/{seq}") // HTTP POST 요청을 처리 (항목 추가)
//    public ResponseEntity<Void> add(@PathVariable("seq") Long seq) {
//        service.add(seq); // 서비스 로직을 통해 항목 추가
//        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created 응답 반환
//    }
//
//
//    /**
//     * 위시리스트에서 항목 삭제 API
//     * - HTTP Method: DELETE
//     * - 특정 항목(seq)을 위시리스트에서 제거함
//     */
//    @DeleteMapping("/{seq}") // HTTP DELETE 요청을 처리 (항목 삭제)
//    public ResponseEntity<Void> remove(@PathVariable("seq") Long seq) {
//        service.remove(seq); // 서비스 로직을 통해 항목 삭제
//        return ResponseEntity.ok().build(); // 200 OK 응답 반환
//    }
//}
