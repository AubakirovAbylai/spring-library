package kz.abylai.spring.repositories;

import kz.abylai.spring.models.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    List<Book> findByNameStartingWithIgnoreCase(String prefix);
}
