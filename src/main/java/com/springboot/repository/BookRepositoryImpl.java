package com.springboot.repository;

import com.springboot.domain.Book;
import com.springboot.exception.BookIdException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository // 레포지토리 컴포넌트로 등록(Bean)
public class BookRepositoryImpl implements BookRepository {
    private List<Book> listOfBooks = new ArrayList<Book>(); // 도서 목록들을 저장할 리스트

    public BookRepositoryImpl() {
        // 샘플 도서 데이터 추가
        Book book1 = new Book();
        book1.setBookId("B001");
        book1.setName("Java Programming");
        book1.setUnitPrice(new java.math.BigDecimal("29.99"));
        book1.setAuthor("John Doe");
        book1.setDescription("A comprehensive guide to Java programming.");
        book1.setPublisher("Tech Books Publishing");
        book1.setCategory("Programming");
        book1.setUnitsInStock(100);
        book1.setReleaseDate("2023-01-15");
        book1.setCondition("New");
        book1.setReleaseDate("2024/02/20");
        book1.setFileName("ISBN1234.jpg");

        Book book2 = new Book();
        book2.setBookId("B002");
        book2.setName("Spring Boot in Action");
        book2.setUnitPrice(new java.math.BigDecimal("39.99"));
        book2.setAuthor("Jane Smith");
        book2.setDescription("Learn Spring Boot with practical examples.");
        book2.setPublisher("Tech Books Publishing");
        book2.setCategory("Programming");
        book2.setUnitsInStock(50);
        book2.setReleaseDate("2023-03-10");
        book2.setCondition("New");
        book2.setReleaseDate("2023/02/20");
        book2.setFileName("ISBN1235.jpg");

        Book book3 = new Book();
        book3.setBookId("ISBN1236");
        book3.setName("안드로이드 프로그래밍");
        book3.setUnitPrice(new BigDecimal(36000));
        book3.setAuthor("송미영");
        book3.setDescription(
                "안드로이드의 기본 개념을 체계적으로 익히고, 이를 실습 예제를 통해 익힙니다. 기본 개념과 사용법을 스스로 실전에 적용하는 방법을 학습한 다음 실습 예제와 응용 예제를 통해 실전 프로젝트 응용력을 키웁니다.");
        book3.setPublisher("길벗");
        book3.setCategory("IT교육교재");
        book3.setUnitsInStock(1000);
        book3.setReleaseDate("2023/06/30");
        book3.setReleaseDate("2022/02/20");
        book3.setFileName("ISBN1236.jpg");

        // 리스트에 도서 추가
        listOfBooks.add(book1);
        listOfBooks.add(book2);
        listOfBooks.add(book3);
    }

    @Override
    public List<Book> getAllBookList() {
        return listOfBooks;
    }

    public Book getBookById(String bookId) {
        Book bookInfo = null;
        for (int i = 0; i < listOfBooks.size(); i++) {
            Book book = listOfBooks.get(i);
            if (book != null && book.getBookId() != null && book.getBookId().equals(bookId)) {
                bookInfo = book;
                break;
            }
        }
        if (bookId == null) {
            throw new BookIdException(bookId);
        }
        return bookInfo;
    }

    @Override
    public void setNewById(Book book) {
        listOfBooks.add(book);
    }
    public void setNewBook(Book book) {
        listOfBooks.add(book);
    }
    @Override
    public List<Book> getBooksByCategory(String category) {
        List<Book> filteredBooks = new ArrayList<Book>();
        if (category == null) {
            return filteredBooks;
        }
        for (Book book : listOfBooks) {
            if (book != null && book.getCategory() != null && book.getCategory().equals(category)) {
                filteredBooks.add(book);
            }
        }
        return filteredBooks;
    }

    @Override
    public Set<Book> getAllBookListByFilter(Map<String, List<String>> filterParams) {
        Set<Book> books = new HashSet<Book>();
        if (filterParams == null || filterParams.isEmpty()) {
            books.addAll(listOfBooks);
            return books;
        }

        List<String> categories = filterParams.get("category");
        List<String> publishers = filterParams.get("publisher");

        for (Book book : listOfBooks) {
            boolean categoryMatches = (categories == null || categories.isEmpty()) || (book.getCategory() != null && categories.contains(book.getCategory()));
            boolean publisherMatches = (publishers == null || publishers.isEmpty()) || (book.getPublisher() != null && publishers.contains(book.getPublisher()));
            if (categoryMatches && publisherMatches) {
                books.add(book);
            }
        }
        return books;
    }
}
