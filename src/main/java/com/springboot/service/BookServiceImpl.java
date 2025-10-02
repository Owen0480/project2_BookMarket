package com.springboot.service;

import com.springboot.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.springboot.repository.BookRepository;


import java.util.List;
import java.util.Map;
import java.util.Set;

@Service    //서비스 컴포넌트로 등록
public class BookServiceImpl implements BookService {

    @Autowired  // 의존성 주입
    private BookRepository bookRepository;
    @Override
    public List<Book> getAllBookList() {
        return bookRepository.getAllBookList();
    }
    @Override
    public Book getBookById(String bookId) {
        Book bookByid = bookRepository.getBookById(bookId);
        return bookByid;
    }

    @Override
    public void setNewBook(Book book) {
        bookRepository.setNewById(book);
    }

    @Override
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.getBooksByCategory(category);
    }

    @Override
    public Set<Book> getAllBookListByFilter(Map<String, List<String>> bookFilter) {
        return bookRepository.getAllBookListByFilter(bookFilter);
    }


}
