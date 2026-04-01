package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// ==============================================================
// [STEP-5] Domain & JPA Entity — Librarian
// --------------------------------------------------------------
// Same JPA and Lombok concepts as Book.java apply here.
// Librarian is the "One" side of the One-to-Many relationship.
// One Librarian can manage many Books.
//
// TODO [STEP-5]: Remove /* and */ below to activate the annotations.
//               Restart the app → verify LIBRARIAN table in H2 Console.
//               Expected columns: ID, NAME, EMAIL
// ==============================================================


@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

public class Librarian {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    


    // ==========================================================
    // [STEP-6] JPA Relationship — One-to-Many
    // ----------------------------------------------------------
    // @OneToMany(mappedBy = "librarian")
    //   Defines the "one" side of the 1:N relationship.
    //   One Librarian can have many Books.
    //   mappedBy = "librarian" tells JPA that the "librarian"
    //   field in Book owns the FK — no extra column is created here.
    //
    // cascade = CascadeType.ALL
    //   Operations on Librarian (save, delete) are automatically
    //   propagated to all associated Books.
    //
    // @JsonIgnore
    //   Without this, serializing a Librarian triggers:
    //     Librarian → books → Book → librarian → Librarian → ...
    //   This infinite loop causes a StackOverflowError.
    //   @JsonIgnore tells Jackson to skip this field during
    //   JSON serialization, breaking the cycle.
    //
    // TODO [STEP-6]: Remove /* and */ below.
    //               Restart the app → BOOK table should now have
    //               a LIBRARIAN_ID column in H2 Console.
    // ==========================================================

    
    @JsonIgnore
    @OneToMany(mappedBy = "librarian", cascade = CascadeType.ALL)
    private List<Book> books = new ArrayList<>();
    

}
