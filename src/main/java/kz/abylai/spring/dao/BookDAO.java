package kz.abylai.spring.dao;

import kz.abylai.spring.models.Book;
import kz.abylai.spring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getBooks() {
        return jdbcTemplate.query("SELECT * FROM Book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book getBook(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE id_book=?",new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class)).stream().findFirst().orElse(null);
    }

    public void createBook(Book book) {
        jdbcTemplate.update("INSERT INTO book(name, author, year) VALUES (?,?,?)",
                book.getName(),book.getAuthor(),book.getYear());
    }

    public void updateBook(int id, Book book) {
        jdbcTemplate.update("UPDATE Book SET name=?, author=?, year=? WHERE id_book =?",
                book.getName(), book.getAuthor(), book.getYear(), id);
    }

    public void deleteBook(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE id_book=?", id);
    }

    public void addPerson(int id, Person person) {
        System.out.println("add person");
        jdbcTemplate.update("UPDATE Book SET id_person=? WHERE id_book=?",
                person.getIdPerson(), id);
    }

    public void releaseBook(int id) {
        jdbcTemplate.update("UPDATE Book SET id_person=NULL WHERE id_book=?", id);
    }

    public List<Book> getBooks(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE id_person=?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
    }
}
