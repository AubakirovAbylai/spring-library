package kz.abylai.spring.dao;

import kz.abylai.spring.models.Book;
import kz.abylai.spring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> getPeople() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Optional<Person> getPerson(String fullName) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE full_name=?",new Object[]{fullName},
                new BeanPropertyRowMapper<>(Person.class)).stream().findFirst();
    }

    public Person getPerson(int id) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE id_person=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class))
                .stream().findFirst().orElse(null);
    }

    public void createPerson(Person person) {
        jdbcTemplate.update("INSERT INTO Person(full_name,year_of_birth) VALUES (?,?)",
                person.getFullName(), person.getYearOfBirth());
    }

    public void updatePerson(int id, Person person) {
        jdbcTemplate.update("UPDATE Person SET full_name=?, year_of_birth=? WHERE id_person = ?",
                person.getFullName(), person.getYearOfBirth(), id);
    }

    public void deletePerson(int id) {
        jdbcTemplate.update("DELETE FROM Person WHERE id_person=?", id);
    }

}
