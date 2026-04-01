package com.example.demo.service;

import com.example.demo.domain.Book;
import com.example.demo.domain.Librarian;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.LibrarianRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// ==============================================================
// [STEP-10] Service Layer — BookService (create)
// --------------------------------------------------------------
// Same @Service, @AllArgsConstructor, @Transactional concepts
// as LibrarianService (see LibrarianService.java).
//
// This service depends on TWO repositories:
//   BookRepository      →  saves the Book
//   LibrarianRepository →  pre-fetches the full Librarian object
//
// Why pre-fetch Librarian before saving?
//   The request body only contains: { "librarian": { "id": 1 } }
//   Jackson sets only the "id" field — name and email stay null.
//   Calling save() directly would return:
//     "librarian": { "id": 1, "name": null, "email": null }
//
//   Solution: use librarianRepository.findById() to load the full
//   Librarian from DB, then set it on the book before saving.
//
// TODO [STEP-10]: Remove /* and */ below.
//                Also uncomment [STEP-10] in BookController.java.
//                Restart the app → test:
//                  POST http://localhost:8081/book
//                  Body: { "title": "Clean Code",
//                          "author": "Robert C. Martin",
//                          "librarian": { "id": 1 } }
//                  Expected: 201 Created with full librarian info
// ==============================================================


@Service
@AllArgsConstructor

public class BookService {

    
    private final BookRepository bookRepository;
    private final LibrarianRepository librarianRepository;
    


    
    @Transactional
    public Book create(Book book) {
        if (book.getLibrarian() != null) {
            Librarian librarian = librarianRepository
                    .findById(book.getLibrarian().getId())
                    .orElseThrow();
            book.setLibrarian(librarian);
        }
        return bookRepository.save(book);
    }
    


    // ==========================================================
    // [STEP-11] Service Layer — BookService (findAll)
    // ----------------------------------------------------------
    // findAll() retrieves all Book records from the database.
    // bookRepository.findAll() translates to: SELECT * FROM BOOK
    // Each row is automatically mapped back to a Book object.
    //
    // TODO [STEP-11]: Remove /* and */ below.
    //                Also uncomment [STEP-11] in BookController.java.
    //                Restart the app → test:
    //                  GET http://localhost:8081/books
    //                  Expected: 200 OK with a list of all books
    // ==========================================================

    
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
    
    //@GetMapping("/book/{id}") — maps GET /book/1 to your method
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

}
