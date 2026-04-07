package com.example.demo.service;

import com.example.demo.domain.Book;
import com.example.demo.domain.Librarian;
import com.example.demo.repository.LibrarianRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

// ==============================================================
// [STEP-9] Service Layer — LibrarianService
// --------------------------------------------------------------
// The Service layer contains the business logic of the application.
// It coordinates between the Controller and the Repository.
// The Controller should never access the Repository directly.
//
// @Service
//   Marks this class as a Spring service bean.
//   Spring registers it in the application context and makes
//   it available for dependency injection.
//
// @AllArgsConstructor (Lombok)
//   Generates a constructor with all fields as parameters.
//   Combined with "private final" fields, this is the recommended
//   approach for constructor-based dependency injection in Spring.
//   Spring detects the constructor and automatically injects
//   the matching bean (LibrarianRepository).
//
// @Transactional
//   Wraps the method in a database transaction.
//   SUCCESS   →  COMMIT   (changes are permanently saved)
//   EXCEPTION →  ROLLBACK (all changes are undone)
//
// TODO [STEP-9]: Remove /* and */ below.
//               Also uncomment [STEP-9] in LibrarianController.java.
//               Restart the app → test:
//                 POST http://localhost:8081/librarian
//                 Body: { "name": "John Smith", "email": "john@library.com" }
//                 Expected: 201 Created
// ==============================================================


@Service
@AllArgsConstructor

public class LibrarianService {

    
    private final LibrarianRepository librarianRepository;
    

    
    @Transactional
    public Librarian create(Librarian librarian) {
        return librarianRepository.save(librarian);
    }


    public Optional<Librarian> findById(Long id) {
        return librarianRepository.findById(id);
    }
    
    //     @PathVariable Long id
//     @RequestBody Librarian updatedLibrarian

    public Optional<Librarian> update(Long id, Librarian updatedLibrarian) {
        return librarianRepository.findById(id)
                .map(existingLibrarian -> {
                    existingLibrarian.setName(updatedLibrarian.getName());
                    existingLibrarian.setEmail(updatedLibrarian.getEmail());
                    return librarianRepository.save(existingLibrarian);
                });
    }
                               
}
