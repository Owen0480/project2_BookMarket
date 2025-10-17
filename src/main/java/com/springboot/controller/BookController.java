package com.springboot.controller;

import com.springboot.domain.Book;
import com.springboot.exception.BookIdException;
import com.springboot.exception.CategoryException;
import com.springboot.service.BookService;
import com.springboot.validator.BookValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller // 컨트롤러 컴포넌트로 등록(Bean)
@RequestMapping(value = "/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public String requestBookList(Model model) {
        List<Book> bookList = bookService.getAllBookList();

        model.addAttribute("bookList", bookList);
        return "books";
    }
    @GetMapping("/all")
    public String requestAllBooks(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        List<Book> bookList = bookService.getAllBookList();
        modelAndView.addObject("bookList", bookList);
        modelAndView.setViewName("books");
        return "modelAndView";
    }

    @GetMapping("/book")
    public String requestBookById(@RequestParam("id") String bookId, Model model) {
        Book bookById = bookService.getBookById(bookId);

        model.addAttribute("book", bookById);
        return "book";
    }

    @GetMapping("/{category}")
    public String requestBooksByCategory(
            @PathVariable("category") String category, Model model) {
        System.out.println("Searching for category: [" + category + "]");
        List<Book> booksByCategory = bookService.getBooksByCategory(category);
        System.out.println("Found " + (booksByCategory != null ? booksByCategory.size() : 0) + " books");
        if (booksByCategory != null) {
            for (Book book : booksByCategory) {
                System.out.println("Book: " + book.getName() + ", Category: [" + book.getCategory() + "]");
            }
        }
        if(booksByCategory == null || booksByCategory.isEmpty()) {
            throw new CategoryException(category);
        }
        model.addAttribute("bookList", booksByCategory);
        return "books";
    }

    @ExceptionHandler(CategoryException.class)
    public ModelAndView handleError(HttpServletRequest req, CategoryException exception) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", exception.getErrorMessage());
        mav.addObject("category", exception.getCategory());
        mav.addObject("exception", exception);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("errorCategory");
        return mav;
    }

    @GetMapping("/filter/{bookFilter}")
    public String requestBooksByFilter(
            @MatrixVariable(pathVar = "bookFilter") Map<String, List<String>> bookFilter, Model model) {
        Set<Book> booksByFilter = bookService.getAllBookListByFilter(bookFilter);
        model.addAttribute("bookList", booksByFilter);
        return "books";
    }
    @Value("${file.uploadDir}")
    String fileDir;

    //@Autowired
    //private UnitsInStockValidator unitsInStockValidator;

    @Autowired
    private BookValidator bookValidator;

    @GetMapping("/add")
    public String requestAddBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "addBook";
    }
    @PostMapping("/add")
    public String submitNewBook(@Valid @ModelAttribute Book book, BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return "addBook";
        MultipartFile bookImage = book.getBookImage();
        String saveName = bookImage.getOriginalFilename();
        File saveFile = new File(fileDir, saveName);
        if (bookImage != null && !bookImage.isEmpty()) {
            try {
                bookImage.transferTo(saveFile);
            } catch (Exception e) {
                throw new RuntimeException("도서 이미지 업로드 실패", e);
            }
        }
        book.setFileName(saveName);
        bookService.setNewBook(book);
        return "redirect:/books"; // 도서 목록 페이지로 리다이렉트
    }
    @GetMapping("/download")
    public void downloadBookImage(@RequestParam("file") String paramKey, HttpServletResponse response) throws IOException {
        if (paramKey == null || paramKey.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "file parameter required");
            return;
        }
        File imageFile = new File(fileDir + paramKey);
        if (!imageFile.exists() || !imageFile.isFile()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        response.setContentType("application/download");
        response.setContentLength((int) imageFile.length());
        response.setHeader("Content-disposition", "attachment;filename=\"" + paramKey +"\"");
        OutputStream os = response.getOutputStream();
        FileInputStream fis = new FileInputStream(imageFile);
        FileCopyUtils.copy(fis, os);
        fis.close();
        os.close();
    }
    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("addTitle", "신규 도서 등록");
    }
    @InitBinder
    public void initBinder(WebDataBinder binder) {
//        binder.setValidator(unitsInStockValidator);
        binder.setValidator(bookValidator);
        binder.setAllowedFields("bookId", "name", "unitPrice", "author", "description", "publisher", "category", "unitsInStock", "totalPages", "releaseDate",
                                                                                        "condition", "bookImage");
    }
    @ExceptionHandler(value = {BookIdException.class})
        public ModelAndView handleError(HttpServletRequest req, BookIdException
        exception){
        ModelAndView mav=new ModelAndView();
        mav.addObject("invalidBookId",exception.getBookId());
        mav.addObject("exception",exception);
        mav.addObject("url",req.getRequestURL()+"?"+req.getQueryString());
        mav.setViewName("errorBook");
        return mav;
        }
}
