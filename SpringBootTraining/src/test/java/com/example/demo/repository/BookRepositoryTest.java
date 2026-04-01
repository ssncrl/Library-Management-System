package com.example.demo.repository;

import com.example.demo.domain.Book;
import com.example.demo.domain.Librarian;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// ==============================================================
// [STEP-8] Repository Testing with JUnit
// --------------------------------------------------------------
// @DataJpaTest
//   Loads ONLY the JPA layer — Controller and Service beans
//   are NOT loaded, keeping tests focused and fast.
//   Automatically uses an in-memory H2 database for testing.
//   The datasource in application.yml is ignored.
//
// @Autowired
//   Spring injects the repository beans automatically.
//
// Each @Test method runs in a transaction that is automatically
// ROLLED BACK after completion — tests are fully isolated.
//
// given / when / then
//   A standard structure for readable tests:
//     given  →  prepare test data
//     when   →  call the method under test
//     then   →  verify the result
//
// assertThat (AssertJ)
//   Fluent assertion library included with spring-boot-starter-test.
//     assertThat(value).isEqualTo(expected)
//     assertThat(value).isNotNull()
//     assertThat(list).hasSize(3)
//
// Prerequisite: complete STEP-4, 5, 6, 7 before running tests.
//
// TODO [STEP-8]: Remove /* and */ below to activate all tests.
//               Run: Right-click the class → Run Tests
//               All 5 tests should pass (green).
// ==============================================================


@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LibrarianRepository librarianRepository;


    // ----------------------------------------------------------
    // TEST 1 — Save a Book (without Librarian)
    // Verifies: @Entity, @Id, @GeneratedValue work correctly.
    // A saved Book should have an auto-generated ID assigned.
    // ----------------------------------------------------------
    @Test
    void save_book_without_librarian() {

        // given
        Book book = new Book(null, "Clean Code", "Robert C. Martin", null);

        // when
        Book saved = bookRepository.save(book);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Clean Code");
        assertThat(saved.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(saved.getLibrarian()).isNull();
    }


    // ----------------------------------------------------------
    // TEST 2 — Find a Book by ID
    // Verifies: findById() returns the correct Book.
    // Returns Optional<Book> — use .isPresent() to check result.
    // ----------------------------------------------------------
    @Test
    void findById_returns_correct_book() {

        // given
        Book book = new Book(null, "The Pragmatic Programmer", "David Thomas", null);
        Book saved = bookRepository.save(book);

        // when
        Optional<Book> found = bookRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("The Pragmatic Programmer");
        assertThat(found.get().getAuthor()).isEqualTo("David Thomas");
    }


    // ----------------------------------------------------------
    // TEST 3 — Find All Books
    // Verifies: findAll() returns every saved Book as a list.
    // ----------------------------------------------------------
    @Test
    void findAll_returns_all_books() {

        // given
        bookRepository.save(new Book(null, "Clean Code", "Robert C. Martin", null));
        bookRepository.save(new Book(null, "The Pragmatic Programmer", "David Thomas", null));
        bookRepository.save(new Book(null, "Spring Boot in Action", "Craig Walls", null));

        // when
        List<Book> books = bookRepository.findAll();

        // then
        assertThat(books).hasSize(3);
    }


    // ----------------------------------------------------------
    // TEST 4 — Save a Book with a Librarian (1:N Relationship)
    // Verifies: @ManyToOne and @JoinColumn work correctly.
    // The LIBRARIAN_ID foreign key should be stored in BOOK table.
    // flush() writes pending changes to DB within the transaction.
    // ----------------------------------------------------------
    @Test
    void save_book_with_librarian() {

        // given — save Librarian first (the "one" side)
        Librarian librarian = new Librarian(null, "John Smith", "john@library.com", new ArrayList<>());
        Librarian savedLibrarian = librarianRepository.save(librarian);

        // given — create a Book linked to the saved Librarian
        Book book = new Book(null, "Clean Code", "Robert C. Martin", savedLibrarian);

        // when
        Book savedBook = bookRepository.save(book);
        bookRepository.flush();

        Optional<Book> found = bookRepository.findById(savedBook.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getLibrarian()).isNotNull();
        assertThat(found.get().getLibrarian().getId()).isEqualTo(savedLibrarian.getId());
        assertThat(found.get().getLibrarian().getName()).isEqualTo("John Smith");
    }


    // ----------------------------------------------------------
    // TEST 5 — Find by Non-existent ID
    // Verifies: findById() returns Optional.empty() when no
    // record exists. Safer than returning null — callers must
    // explicitly handle the "not found" case.
    // ----------------------------------------------------------
    @Test
    void findById_returns_empty_when_not_found() {

        // when
        Optional<Book> found = bookRepository.findById(999L);

        // then
        assertThat(found).isEmpty();
    }

}

