package kz.abylai.spring.services;

import kz.abylai.spring.models.Person;
import kz.abylai.spring.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getPeople() {
        return personRepository.findAll();
    }

    public Optional<Person> getPerson(String fullName) {
        return personRepository.findByFullName(fullName);
    }

    public Person getPerson(int id) {
        Person person = personRepository.getById(id);
        person.getBooks().size();
        return person;
    }

    @Transactional
    public void createPerson(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void updatePerson(int id, Person person) {
        person.setId(id);
        personRepository.save(person);
    }

    @Transactional
    public void deletePerson(int id) {
        Person person = personRepository.getById(id);
        personRepository.delete(person);
    }
}
