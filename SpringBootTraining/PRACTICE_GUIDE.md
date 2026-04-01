# Spring Boot Training — Step-by-Step Practice Guide

## Project Overview

In this project, you will build a **Library Book Management API** step by step.
A Librarian can register and manage books. The relationship between Librarian and Book is **One-to-Many**.

**Final API Endpoints**

| Method | URL          | Description             |
| ------ | ------------ | ----------------------- |
| GET    | `/hello`     | Server connection check |
| POST   | `/librarian` | Register a librarian    |
| POST   | `/book`      | Register a book         |
| GET    | `/books`     | Get all books           |

**Tech Stack**

- Java 17
- Spring Boot 3.4.2
- Spring Data JPA
- H2 Database
- Lombok
- Gradle

---

## Step 1 — Project Structure & Spring Boot Basics

### Concept

**`@SpringBootApplication`**
This single annotation does three things at once:

- `@SpringBootConfiguration` — Marks this class as a configuration source
- `@EnableAutoConfiguration` — Tells Spring Boot to automatically configure beans based on dependencies in the classpath
- `@ComponentScan` — Scans all classes in the package and registers beans annotated with `@Component`, `@Service`, `@Repository`, `@Controller`, etc.

**`application.yml`**
The central configuration file for the application.
You can configure the server port, database connection, JPA behavior, and more.

### Project Package Structure

```
com.example.demo
├── DemoApplication.java          ← Entry point
├── controller
│   ├── BookController.java
│   └── LibrarianController.java
├── service
│   ├── BookService.java
│   └── LibrarianService.java
├── repository
│   ├── BookRepository.java
│   └── LibrarianRepository.java
└── domain
    ├── Book.java
    └── Librarian.java
```

### Layered Architecture

```
Client (Postman)
      │  HTTP Request / Response
      ▼
 Controller      — Handles HTTP requests, maps URLs
      │
      ▼
  Service        — Contains business logic, manages transactions
      │
      ▼
 Repository      — Communicates with the database
      │
      ▼
  Database (H2)
```

Each layer has a single responsibility.
The Controller does not access the database directly — it always goes through the Service.

### Practice

1. Open `DemoApplication.java` and check `@SpringBootApplication`
2. Open `application.yml` and review the server port setting
3. Run the application and verify it starts on port `8081`

---

## Step 2 — H2 Database Connection

### Concept

**H2 Database**
H2 is an in-memory relational database written in Java.
It does not require any installation and is ideal for development and learning.

**File-based vs In-memory**

| Mode       | URL                     | Data after restart |
| ---------- | ----------------------- | ------------------ |
| In-memory  | `jdbc:h2:mem:testdb`    | Lost               |
| File-based | `jdbc:h2:file:~/bookdb` | Preserved          |

This project uses **file-based** mode so data is preserved between restarts.

**`ddl-auto: create`**
Hibernate automatically **drops and recreates** all tables every time the application starts.
This means all data is deleted on each restart.
Other options: `update` (keep existing data), `create-drop` (drop on shutdown), `none` (no action).

**H2 Console**
H2 provides a browser-based SQL console so you can inspect tables and run queries directly.

### `application.yml` Configuration

```yaml
server:
  port: 8081

spring:
  datasource:
    driver-class-name: org.h2.Driver
    url: jdbc:h2:file:~/bookdb
    username: sa
    password:

  h2:
    console:
      enabled: true
      path: /h2-console

  jpa:
    hibernate:
      ddl-auto: create
```

### Practice

1. Run the application
2. Open your browser and go to `http://localhost:8081/h2-console`
3. Enter the following connection settings:
   - JDBC URL: `jdbc:h2:file:~/bookdb`
   - Username: `sa`
   - Password: _(leave empty)_
4. Click **Connect**
5. After completing Step 3, come back and verify that the `BOOK` and `LIBRARIAN` tables were created automatically

---

## Step 3 — Domain & JPA Entity

### Concept

**ORM (Object-Relational Mapping)**
ORM maps Java classes to database tables.
You work with Java objects, and Hibernate translates them into SQL automatically.

```
Java Class  ──────►  Database Table
field       ──────►  column
object      ──────►  row
```

**Key JPA Annotations**

| Annotation                                            | Description                                                 |
| ----------------------------------------------------- | ----------------------------------------------------------- |
| `@Entity`                                             | Maps this class to a database table                         |
| `@Id`                                                 | Marks the field as the Primary Key                          |
| `@GeneratedValue(strategy = GenerationType.IDENTITY)` | Auto-increments the PK value (like AUTO_INCREMENT in MySQL) |

**Lombok Annotations**

| Annotation            | Generated Code                                              |
| --------------------- | ----------------------------------------------------------- |
| `@Data`               | `@Getter` + `@Setter` + `@ToString` + `@EqualsAndHashCode`  |
| `@NoArgsConstructor`  | Default constructor with no arguments — **required by JPA** |
| `@AllArgsConstructor` | Constructor with all fields as parameters                   |

> **Why does JPA need `@NoArgsConstructor`?**
> JPA creates entity objects using reflection. It needs a no-argument constructor to instantiate the class before setting field values.

### Entity Classes

**`Book.java`**

```
DB Table: BOOK
┌──────────────┬──────────────┐
│ Column       │ Type         │
├──────────────┼──────────────┤
│ ID (PK)      │ BIGINT       │
│ TITLE        │ VARCHAR      │
│ AUTHOR       │ VARCHAR      │
│ LIBRARIAN_ID │ BIGINT (FK)  │
└──────────────┴──────────────┘
```

**`Librarian.java`**

```
DB Table: LIBRARIAN
┌──────────────┬──────────────┐
│ Column       │ Type         │
├──────────────┼──────────────┤
│ ID (PK)      │ BIGINT       │
│ NAME         │ VARCHAR      │
│ EMAIL        │ VARCHAR      │
└──────────────┴──────────────┘
```

### Practice

1. Open `Book.java` and `Librarian.java`
2. Identify `@Entity`, `@Id`, `@GeneratedValue` on each class
3. Run the application, go to H2 Console, and confirm both tables are created
4. Check the column names in each table

---

## Step 4 — JPA Relationships (One-to-Many)

### Concept

**One-to-Many Relationship**
One Librarian can manage many Books.
This is expressed in JPA using `@OneToMany` and `@ManyToOne`.

```
LIBRARIAN               BOOK
┌────────────┐          ┌──────────────────┐
│ ID (PK)    │◄────────│ LIBRARIAN_ID (FK) │
│ NAME       │   1 : N  │ ID (PK)          │
│ EMAIL      │          │ TITLE            │
└────────────┘          │ AUTHOR           │
                        └──────────────────┘
```

**`@OneToMany` — on the "One" side (Librarian)**

```java
@OneToMany(mappedBy = "librarian", cascade = CascadeType.ALL)
private List<Book> books = new ArrayList<>();
```

- `mappedBy = "librarian"` — tells JPA that the `librarian` field in `Book` owns the relationship
- `cascade = CascadeType.ALL` — any operation on Librarian (save, delete) is cascaded to its Books

**`@ManyToOne` — on the "Many" side (Book)**

```java
@ManyToOne
@JoinColumn(name = "librarian_id")
private Librarian librarian;
```

- `@ManyToOne` — many Books belong to one Librarian
- `@JoinColumn(name = "librarian_id")` — creates the `LIBRARIAN_ID` foreign key column in the BOOK table

**`@JsonIgnore`**
Without this, serializing a `Librarian` would include its `books` list,
and each `Book` would include its `librarian`, creating an **infinite loop**.
`@JsonIgnore` tells Jackson to skip this field during JSON serialization.

```
Without @JsonIgnore:
  Librarian → books → Book → librarian → Librarian → books → ...  ← StackOverflowError

With @JsonIgnore on Librarian.books:
  Librarian → (books skipped)  ← Safe
  Book → librarian → (books skipped)  ← Safe
```

### Practice

1. Open `Librarian.java` — find `@OneToMany` and `@JsonIgnore`
2. Open `Book.java` — find `@ManyToOne` and `@JoinColumn`
3. Run the app, go to H2 Console
4. In the BOOK table, confirm the `LIBRARIAN_ID` foreign key column exists

---

## Step 5 — Repository Layer

### Concept

**Repository**
The Repository layer is responsible for all database access.
In Spring Data JPA, you only need to declare an interface — Spring generates the implementation automatically.

**`JpaRepository<Entity, ID>`**
By extending `JpaRepository`, you get the following methods for free:

| Method           | SQL equivalent        |
| ---------------- | --------------------- |
| `save(entity)`   | `INSERT` or `UPDATE`  |
| `findById(id)`   | `SELECT WHERE id = ?` |
| `findAll()`      | `SELECT *`            |
| `deleteById(id)` | `DELETE WHERE id = ?` |
| `count()`        | `SELECT COUNT(*)`     |

**`@Repository`**
Marks the interface as a Spring bean.
Also enables Spring to translate JPA exceptions into Spring's unified exception hierarchy.

### Practice

1. Open `BookRepository.java` and `LibrarianRepository.java`
2. Notice there is no implementation — only the interface declaration
3. Confirm they extend `JpaRepository` with the correct type parameters
4. In the next step, you will call `save()` and `findAll()` from the Service layer

---

## Step 6 — Service Layer

### Concept

**Service**
The Service layer contains the **business logic** of the application.
It coordinates between the Controller and the Repository.
The Controller should not call the Repository directly.

**`@Service`**
Marks the class as a Spring service bean.
Spring registers it in the application context and makes it available for injection.

**`@Transactional`**
Wraps the method in a database transaction.

- If the method completes successfully → `COMMIT` (changes are saved)
- If an exception is thrown → `ROLLBACK` (all changes are undone)

```
@Transactional
public Book create(Book book) {
    // Step 1: find librarian from DB
    // Step 2: set librarian on book
    // Step 3: save book
    // If any step throws an exception → entire operation is rolled back
}
```

**Constructor Injection with Lombok**

```java
@Service
@AllArgsConstructor          // Lombok generates this constructor
public class BookService {
    private final BookRepository bookRepository;            // injected by Spring
    private final LibrarianRepository librarianRepository;  // injected by Spring
}
```

Using `final` + `@AllArgsConstructor` is the recommended way to do dependency injection in Spring.
Spring sees the constructor and automatically injects the matching beans.

### Practice

1. Open `BookService.java` and `LibrarianService.java`
2. Find `@Transactional` and understand when it commits or rolls back
3. In `BookService.create()`, trace the logic:
   - Why is `librarianRepository.findById()` called before `save()`?
   - What would happen if we skip this step? _(hint: name and email would be null in the response)_
4. Open `LibrarianService.java` — it only calls `save()` directly. Why is no pre-fetch needed here?

---

## Step 7 — Controller & RESTful API

### Concept

**REST (Representational State Transfer)**
A design style for building web APIs using standard HTTP methods.

| HTTP Method | Action | URL example | Response Code    |
| ----------- | ------ | ----------- | ---------------- |
| GET         | Read   | `/books`    | `200 OK`         |
| POST        | Create | `/book`     | `201 Created`    |
| PUT         | Update | `/book/1`   | `200 OK`         |
| DELETE      | Delete | `/book/1`   | `204 No Content` |

**`@RestController`**
Combines `@Controller` and `@ResponseBody`.
Every method return value is automatically serialized to JSON and written into the HTTP response body.

**`@GetMapping` / `@PostMapping`**
Maps an HTTP request to a specific handler method.

```java
@GetMapping("/books")    // handles GET http://localhost:8081/books
@PostMapping("/book")    // handles POST http://localhost:8081/book
```

**`@RequestBody`**
Deserializes the incoming JSON request body into a Java object.

```
POST /book
Body: { "title": "Clean Code", "author": "Robert C. Martin", "librarian": { "id": 1 } }
         └─────────────────────────────────────────────────────────────────────────────┘
                                        @RequestBody Book book
```

**`ResponseEntity`**
Gives you full control over the HTTP response — status code + body.

```java
return new ResponseEntity<>(body, HttpStatus.CREATED);   // 201 Created
return new ResponseEntity<>(body, HttpStatus.OK);         // 200 OK
```

### Practice

1. Open `BookController.java` and `LibrarianController.java`
2. Match each method to its HTTP method and URL
3. Test each endpoint using Postman in the following order:

**Test Order**

```
Step 1 — GET  http://localhost:8081/hello
           Expected: 200 OK  |  Body: "Hello World"

Step 2 — POST http://localhost:8081/librarian
           Body: { "name": "John Smith", "email": "john.smith@library.com" }
           Expected: 201 Created  |  Body: { "id": 1, "name": "John Smith", ... }

Step 3 — POST http://localhost:8081/book
           Body: { "title": "Clean Code", "author": "Robert C. Martin", "librarian": { "id": 1 } }
           Expected: 201 Created  |  Body: { "id": 1, ..., "librarian": { "id": 1, "name": "John Smith", ... } }

Step 4 — GET  http://localhost:8081/books
           Expected: 200 OK  |  Body: [ { ... }, { ... } ]
```

---

## Step 8 — Verify with H2 Console

### Practice

After completing all API tests, go to `http://localhost:8081/h2-console` and run the following queries:

```sql
-- Check all librarians
SELECT * FROM LIBRARIAN;

-- Check all books
SELECT * FROM BOOK;

-- Check books with their librarian info (JOIN)
SELECT b.id, b.title, b.author, l.name AS librarian_name
FROM BOOK b
JOIN LIBRARIAN l ON b.librarian_id = l.id;
```

Verify that:

- The `LIBRARIAN_ID` foreign key in the BOOK table matches the `ID` in the LIBRARIAN table
- The 1:N relationship is correctly stored in the database

---

## Summary

| Step | Topic                   | Key Annotations                                                                |
| ---- | ----------------------- | ------------------------------------------------------------------------------ |
| 1    | Spring Boot Basics      | `@SpringBootApplication`                                                       |
| 2    | H2 DB Connection        | `application.yml`                                                              |
| 3    | Domain & JPA Entity     | `@Entity` `@Id` `@GeneratedValue` `@Data`                                      |
| 4    | JPA Relationships       | `@OneToMany` `@ManyToOne` `@JoinColumn` `@JsonIgnore`                          |
| 5    | Repository Layer        | `@Repository` `JpaRepository`                                                  |
| 6    | Service Layer           | `@Service` `@Transactional` `@AllArgsConstructor`                              |
| 7    | Controller & REST API   | `@RestController` `@GetMapping` `@PostMapping` `@RequestBody` `ResponseEntity` |
| 8    | H2 Console Verification | SQL queries to verify data                                                     |
