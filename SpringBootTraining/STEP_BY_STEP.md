# Step-by-Step Uncomment Guide

> **How to uncomment**: Find the `/* */` block marked for your current step.
> Delete the `/*` line and the `*/` line. The code inside becomes active.
> For `application.yml`: delete the `#` at the start of each line.

---

## STEP 1 — App Startup

**No code changes needed.**
Just run the application and verify it starts.

**Verify**
- To run app start with command: .\gradlew.bat bootRun 
- Console shows: `Tomcat started on port 8081`
- Open browser: `http://localhost:8081` → may show error page (normal, no endpoints yet)

---

## STEP 2 — First REST Endpoint

**File: `controller/BookController.java`**

| #   | What to uncomment | Location in file                     |
| --- | ----------------- | ------------------------------------ |
| 1   | `@RestController` | above the class declaration          |
| 2   | `hello()` method  | first `/* */` block inside the class |

**Verify**

```
GET http://localhost:8081/hello
Expected → 200 OK  |  Body: Hello World
```

---

## STEP 3 — H2 Database Connection

**File: `resources/application.yml`**

| #   | What to uncomment                                   | How                           |
| --- | --------------------------------------------------- | ----------------------------- |
| 1   | All lines under `# spring:` to `# ddl-auto: create` | Delete the `#` from each line |

**Verify**

- App starts without errors (restart the app)
- Open browser: `http://localhost:8081/h2-console`
- Connect with JDBC URL: `jdbc:h2:file:~/bookdb` / Username: `sa` / Password: _(empty)_
- H2 Console opens successfully

---

## STEP 4 — Book Entity (JPA Basics)

**File: `domain/Book.java`**

| #   | What to uncomment                                            | Location in file                       |
| --- | ------------------------------------------------------------ | -------------------------------------- |
| 1   | `@AllArgsConstructor` `@NoArgsConstructor` `@Data` `@Entity` | first `/* */` block, above the class   |
| 2   | `@Id` `@GeneratedValue` `id` `title` `author` fields         | second `/* */` block, inside the class |

**Verify**

- App starts without errors
- In H2 Console: run `SELECT * FROM BOOK;`
- BOOK table exists with columns: `ID`, `TITLE`, `AUTHOR`

---

## STEP 5 — Librarian Entity

**File: `domain/Librarian.java`**

| #   | What to uncomment                                            | Location in file                       |
| --- | ------------------------------------------------------------ | -------------------------------------- |
| 1   | `@AllArgsConstructor` `@NoArgsConstructor` `@Data` `@Entity` | first `/* */` block, above the class   |
| 2   | `@Id` `@GeneratedValue` `id` `name` `email` fields           | second `/* */` block, inside the class |

**Verify**

- App starts without errors
- In H2 Console: run `SELECT * FROM LIBRARIAN;`
- LIBRARIAN table exists with columns: `ID`, `NAME`, `EMAIL`

---

## STEP 6 — JPA 1:N Relationship

**File: `domain/Book.java`**

| #   | What to uncomment                            | Location in file                         |
| --- | -------------------------------------------- | ---------------------------------------- |
| 1   | `@ManyToOne` `@JoinColumn` `librarian` field | `/* */` block at the bottom of the class |

**File: `domain/Librarian.java`**

| #   | What to uncomment                        | Location in file                         |
| --- | ---------------------------------------- | ---------------------------------------- |
| 1   | `@JsonIgnore` `@OneToMany` `books` field | `/* */` block at the bottom of the class |

**Verify**

- App starts without errors
- In H2 Console: run `SELECT * FROM BOOK;`
- BOOK table now has a `LIBRARIAN_ID` column (foreign key)

---

## STEP 7 — Repository Layer

**File: `repository/BookRepository.java`**

| #   | What to do                                          | Location in file                                 |
| --- | --------------------------------------------------- | ------------------------------------------------ |
| 1   | Remove `/*` and `*/` around `@Repository` interface | middle of the file                               |
| 2   | **Delete** the compilation stub line at the bottom  | last line: `public interface BookRepository { }` |

**File: `repository/LibrarianRepository.java`**

| #   | What to do                                          | Location in file                                      |
| --- | --------------------------------------------------- | ----------------------------------------------------- |
| 1   | Remove `/*` and `*/` around `@Repository` interface | middle of the file                                    |
| 2   | **Delete** the compilation stub line at the bottom  | last line: `public interface LibrarianRepository { }` |

**Verify**

- App starts without errors
- No `BeanCreationException` in the console
- Spring has registered `BookRepository` and `LibrarianRepository` as beans

---

## STEP 8 — Repository Testing (JUnit)

**File: `test/.../repository/BookRepositoryTest.java`**

| #   | What to uncomment                                      | Location in file               |
| --- | ------------------------------------------------------ | ------------------------------ |
| 1   | Entire test class (`@DataJpaTest` through closing `}`) | the single large `/* */` block |

**Run the tests**

- Right-click `BookRepositoryTest` → **Run**
- All 5 tests should pass ✅

| Test                                    | Verifies                                         |
| --------------------------------------- | ------------------------------------------------ |
| `save_book_without_librarian`           | `@Entity`, `@Id`, `@GeneratedValue`              |
| `findById_returns_correct_book`         | `findById()` returns correct result              |
| `findAll_returns_all_books`             | `findAll()` returns all records                  |
| `save_book_with_librarian`              | `@ManyToOne`, `@JoinColumn`, FK stored correctly |
| `findById_returns_empty_when_not_found` | `Optional.empty()` when ID not found             |

---

## STEP 9 — Librarian Service & Controller

**File: `service/LibrarianService.java`**

| #   | What to uncomment                | Location in file                     |
| --- | -------------------------------- | ------------------------------------ |
| 1   | `@Service` `@AllArgsConstructor` | first `/* */` block, above the class |
| 2   | `librarianRepository` field      | second `/* */` block                 |
| 3   | `create()` method                | third `/* */` block                  |

**File: `controller/LibrarianController.java`**

| #   | What to uncomment                       | Location in file                     |
| --- | --------------------------------------- | ------------------------------------ |
| 1   | `@RestController` `@AllArgsConstructor` | first `/* */` block, above the class |
| 2   | `librarianService` field                | second `/* */` block                 |
| 3   | `create()` method with `@PostMapping`   | third `/* */` block                  |

**Verify**

```
POST http://localhost:8081/librarian
Body: { "name": "John Smith", "email": "john@library.com" }
Expected → 201 Created
{
  "id": 1,
  "name": "John Smith",
  "email": "john@library.com"
}
```

---

## STEP 10 — Book Service & Controller (create)

**File: `service/BookService.java`**

| #   | What to uncomment                                 | Location in file                     |
| --- | ------------------------------------------------- | ------------------------------------ |
| 1   | `@Service` `@AllArgsConstructor`                  | first `/* */` block, above the class |
| 2   | `bookRepository` and `librarianRepository` fields | second `/* */` block                 |
| 3   | `create()` method with `@Transactional`           | third `/* */` block                  |

**File: `controller/BookController.java`**

| #   | What to uncomment                   | Location in file                      |
| --- | ----------------------------------- | ------------------------------------- |
| 1   | `@AllArgsConstructor`               | second `/* */` block, above the class |
| 2   | `bookService` field                 | `/* */` block inside the class        |
| 3   | `save()` method with `@PostMapping` | `/* */` block marked STEP-10          |

**Verify**

```
POST http://localhost:8081/book
Body: { "title": "Clean Code",
        "author": "Robert C. Martin",
        "librarian": { "id": 1 } }
Expected → 201 Created
{
  "id": 1,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "librarian": { "id": 1, "name": "John Smith", "email": "john@library.com" }
}
```

---

## STEP 11 — Get All Books

**File: `service/BookService.java`**

| #   | What to uncomment  | Location in file   |
| --- | ------------------ | ------------------ |
| 1   | `findAll()` method | last `/* */` block |

**File: `controller/BookController.java`**

| #   | What to uncomment                               | Location in file   |
| --- | ----------------------------------------------- | ------------------ |
| 1   | `findAll()` method with `@GetMapping("/books")` | last `/* */` block |

**Verify**

```
GET http://localhost:8081/books
Expected → 200 OK
[
  {
    "id": 1,
    "title": "Clean Code",
    "author": "Robert C. Martin",
    "librarian": { "id": 1, "name": "John Smith", "email": "john@library.com" }
  }
]
```

---

## STEP 12 — Final Verification (H2 Console)

**No code changes.** Open H2 Console and run the following SQL queries.

```sql
-- Check all librarians
SELECT * FROM LIBRARIAN;

-- Check all books
SELECT * FROM BOOK;

-- Verify the 1:N relationship with a JOIN
SELECT b.id, b.title, b.author, l.name AS librarian_name
FROM BOOK b
JOIN LIBRARIAN l ON b.librarian_id = l.id;
```

**Verify**

- `LIBRARIAN_ID` in BOOK matches `ID` in LIBRARIAN
- The JOIN query returns books with their librarian's name

---

## Quick Reference

| Step | File(s)                                  | Action                                  |
| ---- | ---------------------------------------- | --------------------------------------- |
| 1    | —                                        | Run the app                             |
| 2    | `BookController`                         | Uncomment `@RestController` + `hello()` |
| 3    | `application.yml`                        | Remove `#` from DB config block         |
| 4    | `Book`                                   | Uncomment annotations + fields          |
| 5    | `Librarian`                              | Uncomment annotations + fields          |
| 6    | `Book` `Librarian`                       | Uncomment relationship fields           |
| 7    | `BookRepository` `LibrarianRepository`   | Uncomment interface + delete stub       |
| 8    | `BookRepositoryTest`                     | Uncomment test class → Run tests        |
| 9    | `LibrarianService` `LibrarianController` | Uncomment all blocks                    |
| 10   | `BookService` `BookController`           | Uncomment create blocks                 |
| 11   | `BookService` `BookController`           | Uncomment findAll blocks                |
| 12   | —                                        | Run SQL in H2 Console                   |
