package com.example.demo.controller;

import com.example.demo.domain.Book;
import com.example.demo.domain.Librarian;
import com.example.demo.service.LibrarianService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// ==============================================================
// [STEP-9] Controller Layer — LibrarianController
// --------------------------------------------------------------
// @RestController
//   Combines @Controller and @ResponseBody.
//   Registers this class as a Spring MVC controller and ensures
//   every return value is serialized to JSON automatically.
//
// @PostMapping("/librarian")
//   Maps HTTP POST requests sent to /librarian to this method.
//
// @RequestBody
//   Deserializes the JSON request body into a Java object.
//   Jackson reads the incoming JSON and maps each field
//   to the corresponding field in the Librarian class.
//
//   Example request body:
//   {
//     "name": "John Smith",
//     "email": "john@library.com"
//   }
//
// ResponseEntity<?>
//   Gives full control over the HTTP response:
//     new ResponseEntity<>(body, HttpStatus.CREATED)
//     →  returns HTTP 201 Created with the saved object as body.
//
// TODO [STEP-9]: Remove /* and */ below.
//               Also uncomment [STEP-9] in LibrarianService.java.
//               Restart the app → test:
//                 POST http://localhost:8081/librarian
//                 Expected: 201 Created
// ==============================================================


@RestController
@AllArgsConstructor

public class LibrarianController {

    
    private final LibrarianService librarianService;
    

    
    @PostMapping("/librarian")
    public ResponseEntity<?> create(@RequestBody Librarian librarian) {
        return new ResponseEntity<>(librarianService.create(librarian), HttpStatus.CREATED);
    }
    
    @GetMapping("/librarian/{id}")
    public ResponseEntity<Librarian> findById(@PathVariable Long id) {
        return librarianService.findById(id)
                .map(librarian -> new ResponseEntity<>(librarian, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //@PutMapping("/librarian/{id}") — maps PUT /librarian/1 to your method
    @PutMapping("/librarian/{id}")
    public ResponseEntity<Librarian> update(@PathVariable Long id, @RequestBody Librarian updatedLibrarian) {
        return librarianService.update(id, updatedLibrarian)
                .map(librarian -> new ResponseEntity<>(librarian, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
