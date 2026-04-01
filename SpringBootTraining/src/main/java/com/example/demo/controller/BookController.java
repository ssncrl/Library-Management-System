package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; //required for @PathVariable in findById()
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Book;
import com.example.demo.service.BookService;

import lombok.AllArgsConstructor;

// ==============================================================
// [STEP-2] Controller Layer — BookController
// --------------------------------------------------------------
 @RestController
//   Combines @Controller and @ResponseBody.
//   Registers this class as a Spring MVC controller and ensures
//   every return value is serialized to JSON automatically.
//
// Layered Architecture
//   Client (Postman)
//        ↓  HTTP Request / Response
//   Controller   ← you are here
//        ↓
//    Service
//        ↓
//   Repository
//        ↓
//    Database (H2)
//
// @AllArgsConstructor (Lombok) — activated at STEP-10
//   Generates a constructor with all fields as parameters.
//   Spring uses this to inject BookService automatically.
//   This is called Constructor Injection.
//
// TODO [STEP-2]: Remove /* and */ around @RestController below.
//               Then remove /* and */ around the hello() method.
//               Restart the app → test:
//                 GET http://localhost:8081/hello
//                 Expected: 200 OK  |  Body: Hello World
// ==============================================================

/*
@RestController
*/

@AllArgsConstructor

public class BookController {

    
    private final BookService bookService;
    


    // ==========================================================
    // [STEP-2] GET /hello — Server Connection Check
    // ----------------------------------------------------------
    // @GetMapping("/hello")
    //   Maps HTTP GET requests sent to /hello to this method.
    //   The String return value is written directly to the
    //   response body (no JSON wrapping needed for plain text).
    //
    // TODO [STEP-2]: Remove /* and */ below.
    // ==========================================================

    
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
    


    // ==========================================================
    // [STEP-10] POST /book — Register a Book
    // ----------------------------------------------------------
    // @PostMapping("/book")
    //   Maps HTTP POST requests sent to /book to this method.
    //
    // @RequestBody Book book
    //   Deserializes the incoming JSON into a Book object.
    //   Example:
    //   {
    //     "title": "Clean Code",
    //     "author": "Robert C. Martin",
    //     "librarian": { "id": 1 }
    //   }
    //
    // HttpStatus.CREATED → HTTP 201
    //   Standard REST response code for a successful creation.
    //
    // TODO [STEP-10]: Remove /* and */ around @AllArgsConstructor above.
    //                Also remove /* and */ below.
    //                Also uncomment [STEP-10] in BookService.java.
    // ==========================================================

    
    @PostMapping("/book")
    public ResponseEntity<?> save(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.create(book), HttpStatus.CREATED);
    }
    


    // ==========================================================
    // [STEP-11] GET /books — Retrieve All Books
    // ----------------------------------------------------------
    // @GetMapping("/books")
    //   Maps HTTP GET requests sent to /books to this method.
    //
    // ResponseEntity<List<Book>>
    //   Returns HTTP 200 OK with a JSON array of all books.
    //   Each Book in the list includes its Librarian info.
    //
    // TODO [STEP-11]: Remove /* and */ below.
    //                Also uncomment [STEP-11] in BookService.java.
    // ==========================================================

    
    @GetMapping("/books")
    public ResponseEntity<List<Book>> findAll() {
        return new ResponseEntity<>(bookService.findAll(), HttpStatus.OK);
    }
    
    /*@PathVariable Long id — extracts the {id} value from the URL
    HttpStatus.OK — return 200 when found
    HttpStatus.NOT_FOUND — return 404 when not found */
    @GetMapping("/book/{id}")
    public ResponseEntity<Book> findById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(book -> new ResponseEntity<>(book, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
