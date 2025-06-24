package kz.abylai.spring.controllers;

import jakarta.validation.Valid;
import kz.abylai.spring.models.Book;
import kz.abylai.spring.models.Person;
import kz.abylai.spring.services.BookService;
import kz.abylai.spring.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String showBooks(Model model,
            @RequestParam(name = "page", required = false) Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "sort_by_year", defaultValue = "false") boolean sort) {

        if(page == null || size == null){
            model.addAttribute("books", bookService.findAll());
        }else {
            model.addAttribute("bookPage", bookService.getBooksPage(page, size, sort));
        }
        model.addAttribute("sort", sort);

        return "books/showAllBooks";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model) {
        Book book = bookService.getBook(id);

        model.addAttribute("book", bookService.getBook(id));
        model.addAttribute("id", id);
        model.addAttribute("people", personService.getPeople());

        //если у книги есть назначенный человек — передаём его в модель
        if (book.getOwner() != null) {
            Person assignedPerson = book.getOwner();
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
        bookService.createBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model) throws SQLException {
        model.addAttribute("book", bookService.getBook(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookService.updateBook(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @PostMapping("/{id}/addPerson")
    public String addPerson(@PathVariable("id") int id,
                               @RequestParam("idPerson") int idPerson) {
        bookService.addPerson(bookService.getBook(id)
                , personService.getPerson(idPerson));
        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int bookId) {
        bookService.releaseBook(bookId);
        return "redirect:/books/" + bookId;
    }

    // Показывает форму поиска
    @GetMapping("/search")
    public String searchPage(Model model) {
        model.addAttribute("books", null); // пусто при первом открытии
        return "books/search";
    }

    // Обрабатывает форму
    @PostMapping("/search")
    public String performSearch(@RequestParam("query") String query, Model model) {
        List<Book> books = bookService.searchBooksByName(query);
        model.addAttribute("books", books);
        return "books/search";
    }


}
