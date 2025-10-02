package com.springboot.repository;

import com.springboot.domain.Book;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BookRepository {
    List<Book> getAllBookList();        // 모든 도서 목록들을 가져오는 메소드
    Book getBookById(String bookId);  // 도서 아이디로 도서 정보를 가져오는 메소드
    void setNewById(Book book);
    List<Book> getBooksByCategory(String category); // 카테고리로 도서 목록 가져오기
    Set<Book> getAllBookListByFilter(Map<String, List<String>> filterParams);
}
