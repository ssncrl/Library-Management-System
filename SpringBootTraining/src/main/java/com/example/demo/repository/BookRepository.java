package com.example.demo.repository;

import com.example.demo.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// ==============================================================
// [STEP-7] Repository Layer — BookRepository
// --------------------------------------------------------------
// The Repository layer is responsible for all database access.
// It sits between the Service layer and the database.
//
// JpaRepository<Entity, ID>
//   Extending JpaRepository gives you these methods for free —
//   no SQL or implementation code needed:
//
//     save(entity)      →  INSERT or UPDATE
//     findById(id)      →  SELECT WHERE id = ?
//     findAll()         →  SELECT * FROM book
//     deleteById(id)    →  DELETE WHERE id = ?
//     count()           →  SELECT COUNT(*)
//
//   Spring Data JPA auto-generates the implementation at runtime.
//
// @Repository
//   Marks this interface as a Spring Data bean.
//   Enables Spring to translate JPA exceptions into Spring's
//   unified DataAccessException hierarchy.
//
// TODO [STEP-7]:
//   1. Remove /* and */ below to activate the real interface.
//   2. Delete the compilation stub at the bottom of this file.
//   Restart the app — no errors means the bean is registered.
// ==============================================================


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}



