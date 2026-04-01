package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// ==============================================================
// [STEP-4] Domain & JPA Entity — Book
// --------------------------------------------------------------
// JPA (Java Persistence API)
//   A specification for mapping Java objects to relational
//   database tables. Hibernate is the JPA implementation used
//   in this project (via spring-boot-starter-data-jpa).
//
// ORM (Object-Relational Mapping)
//   Automatically translates between Java objects and DB tables:
//     Java Class  →  Database Table (BOOK)
//     field       →  column
//     object      →  row
//
// Lombok
//   @Data           — generates getters, setters, toString,
//                     equals, hashCode
//   @NoArgsConstructor — generates a no-argument constructor.
//                        REQUIRED by JPA: it uses reflection to
//                        instantiate objects before setting fields.
//   @AllArgsConstructor — generates a constructor with all fields.
//
// @Entity
//   Tells JPA to map this class to a database table.
//   The table name defaults to the class name → BOOK.
//
// TODO [STEP-4]: Remove /* and */ below to activate the annotations.
//               Restart the app → verify BOOK table in H2 Console.
//               Expected columns: ID, TITLE, AUTHOR
// ==============================================================


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class Book {

    // ==========================================================
    // [STEP-4] Primary Key Configuration
    // ----------------------------------------------------------
    // @Id
    //   Marks this field as the Primary Key of the BOOK table.
    //
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    //   Delegates ID generation to the database engine.
    //   Equivalent to AUTO_INCREMENT in SQL.
    //   The database assigns the next available ID automatically.
    //
    // TODO [STEP-4]: Remove /* and */ below.
    // ==========================================================

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
   


    // ==========================================================
    // [STEP-6] JPA Relationship — Many-to-One
    // ----------------------------------------------------------
    // @ManyToOne
    //   Defines the "many" side of the 1:N relationship.
    //   Many Books can belong to One Librarian.
    //
    // @JoinColumn(name = "librarian_id")
    //   Creates a LIBRARIAN_ID foreign key column in the BOOK table.
    //   This column stores the ID of the associated Librarian row.
    //
    // TODO [STEP-6]: Remove /* and */ below.
    //               Restart the app → BOOK table should now have
    //               a LIBRARIAN_ID column in H2 Console.
    // ==========================================================

    
    @ManyToOne
    @JoinColumn(name = "librarian_id")
    private Librarian librarian;
    

}
