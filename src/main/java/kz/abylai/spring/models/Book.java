package kz.abylai.spring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "id_person", referencedColumnName = "id")
    private Person owner;

    @Column(name = "name")
    @NotEmpty
    private String name;

    @Column(name = "author")
    @NotEmpty
    private String author;

    @Column(name = "year")
    @Max(value = 2025, message = "Пожалуйста введите корректные данные")
    private int year;

    @Column(name = "person_take_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date personTakeDate;

    @Transient
    private boolean expired;

    public Book(String name, String author, int year) {
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public Book() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getPersonTakeDate() {
        return personTakeDate;
    }

    public void setPersonTakeDate(Date personTakeDate) {
        this.personTakeDate = personTakeDate;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}


