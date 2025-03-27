package com.thxforservice.board.repositories;

import com.thxforservice.board.entities.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Board 엔터티에 대한 Repository 인터페이스
 *
 * - JpaRepository<Board, String>
 *   - 기본적인 CRUD 기능 제공
 *   - findById, save, delete 등 JPA에서 기본 제공하는 메서드 사용 가능
 *
 * - QuerydslPredicateExecutor<Board>
 *   - QueryDSL을 활용한 동적 쿼리 기능 추가
 *   - Predicate를 사용하여 다양한 조건의 검색 가능
 *   - 예: findAll(Predicate predicate), findOne(Predicate predicate) 등
 */
public interface BoardRepository extends JpaRepository<Board, String>, QuerydslPredicateExecutor<Board> {

}
