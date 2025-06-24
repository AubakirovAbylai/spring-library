package kz.abylai.spring.services;

import kz.abylai.spring.models.Book;
import kz.abylai.spring.models.Person;
import kz.abylai.spring.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BookService {
    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book getBook(int id) {
        return bookRepository.findById(id).get();
    }

    @Transactional
    public void createBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void updateBook(int id, Book book) {
        Book bookToBeUpdated = bookRepository.findById(id).get();
        book.setId(id);
        book.setOwner(bookToBeUpdated.getOwner());
        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(int id) {
        Book book = bookRepository.findById(id).get();
        bookRepository.delete(book);
    }

    @Transactional
    public void addPerson(Book book, Person person) {
        book.setPersonTakeDate(new Date());
        book.setOwner(person);
        person.getBooks().add(book);
        bookRepository.save(book);
    }

    @Transactional
    public void releaseBook(int id) {
        bookRepository.findById(id).ifPresent(
                book ->{
                    book.setOwner(null);
                    book.setPersonTakeDate(null);
                }
        );
    }


    public Page<Book> getBooksPage(int page, int size, boolean sort) {
        Pageable pageable;
        if (sort == true) {
            pageable = PageRequest.of(page, size, Sort.by("year"));
        }else {
            pageable = PageRequest.of(page, size);
        }
        return bookRepository.findAll(pageable);
    }

    public List<Book> searchBooksByName(String prefix) {
        return bookRepository.findByNameStartingWithIgnoreCase(prefix);
    }

    private boolean isBookExpired(Book book) {
        if (book.getPersonTakeDate() == null)
            return false;

        long now = System.currentTimeMillis();
        long taken = book.getPersonTakeDate().getTime();
        long diffDays = (now - taken) / (1000 * 60 * 60 * 24);

        return diffDays > 10;
    }

    public List<Book> getBooksWithExpireCheck(List<Book> books) {
        for (Book book : books) {
            book.setExpired(isBookExpired(book));
        }
        return books;
    }


}
