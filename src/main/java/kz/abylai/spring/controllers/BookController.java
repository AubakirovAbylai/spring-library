package kz.abylai.spring.controllers;

import jakarta.validation.Valid;
import kz.abylai.spring.dao.BookDAO;
import kz.abylai.spring.dao.PersonDAO;
import kz.abylai.spring.models.Book;
import kz.abylai.spring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String showBooks(Model model) {
        model.addAttribute("books", bookDAO.getBooks());
        return "books/showAllBooks";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model) {
        Book book = bookDAO.getBook(id);
        model.addAttribute("book", bookDAO.getBook(id));
        model.addAttribute("idBook", id);
        model.addAttribute("people", personDAO.getPeople());
        model.addAttribute("person", new Person());

        //если у книги есть назначенный человек — передаём его в модель
        if (book.getIdPerson() != null) {
            Person assignedPerson = personDAO.getPerson(book.getIdPerson());
            model.addAttribute("assignedPerson", assignedPerson);
        } else {
            model.addAttribute("assignedPerson", null);
        }


        return "books/showBook";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book){
        bookDAO.createBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) throws SQLException {
        model.addAttribute("book", bookDAO.getBook(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookDAO.updateBook(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        bookDAO.deleteBook(id);
        return "redirect:/books";
    }

    @PostMapping("/{id}/addPerson")
    public String addPerson(@PathVariable("id") int bookId,
                               @RequestParam("idPerson") int idPerson) {
        Person person = personDAO.getPerson(idPerson);
        bookDAO.addPerson(bookId, person);
        return "redirect:/books/" + bookId;
    }

    @PostMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int bookId) {
        bookDAO.releaseBook(bookId);
        return "redirect:/books/" + bookId;
    }


}
