package com.example.demo.repository;

import com.example.demo.domain.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ==============================================================
// [STEP-7] Repository Layer — LibrarianRepository
// --------------------------------------------------------------
// Same concept as BookRepository.
// Provides full CRUD operations for the Librarian entity.
//
// JpaRepository<Librarian, Long>
//   Librarian  →  the entity type this repository manages
//   Long       →  the type of the primary key (id field)
//
// TODO [STEP-7]:
//   1. Remove /* and */ below to activate the real interface.
//   2. Delete the compilation stub at the bottom of this file.
//   Restart the app — no errors means the bean is registered.
// ==============================================================


@Repository
public interface LibrarianRepository extends JpaRepository<Librarian, Long> {
}


