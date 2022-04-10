package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import validation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TemaValidatorTest {


    private StudentValidator studentValidator = new StudentValidator();
    private Validator<Tema> temaValidator = new TemaValidator();
    private Validator<Nota> notaValidator = new NotaValidator();


    StudentXMLRepository fileRepository1;
    TemaXMLRepository fileRepository2;
    NotaXMLRepository fileRepository3;

    private Service service;

    @BeforeEach
    void setUp() throws IOException {
        fileRepository1 = new StudentXMLRepository(studentValidator, "studenti_test.xml");
        fileRepository2 = new TemaXMLRepository(temaValidator, "teme_test.xml");
        fileRepository3 = new NotaXMLRepository(notaValidator, "note_test.xml");
        service = new Service(fileRepository1, fileRepository2, fileRepository3);
    }

    @AfterEach
    void tearDown() throws IOException {
        deleteAllStudents();
        deleteAllTema();
        deleteAllNota();
    }

    private void deleteAllStudents() {
        List<Student> studentCopies = StreamSupport.stream(fileRepository1.findAll().spliterator(), false).collect(Collectors.toList());
        for (Student student: studentCopies) {
            fileRepository1.delete(student.getID());
        }
    }

    private void deleteAllTema() {
        List<Tema> temaCopies = StreamSupport.stream(fileRepository2.findAll().spliterator(), false).collect(Collectors.toList());
        for (Tema tema: temaCopies) {
            fileRepository2.delete(tema.getID());
        }
    }

    private void deleteAllNota() {
        List<Nota> temaCopies = StreamSupport.stream(fileRepository3.findAll().spliterator(), false).collect(Collectors.toList());
        for (Nota nota: temaCopies) {
            fileRepository3.delete(nota.getID());
        }
    }

    @Test
    void addTema_Id_Null() {
        Tema tema = new Tema(null, "bruh", 14, 3);
        assertThrows(ValidationException.class, () -> temaValidator.validate(tema));
    }

    @Test
    void addTema_Id_Empty() {
        Tema tema = new Tema("", "bruh", 14, 3);
        assertThrows(ValidationException.class, () -> temaValidator.validate(tema));
    }


    @Test
    void addTema_Description_Null() {
        Tema tema = new Tema("33", null, 14, 3);
        assertThrows(ValidationException.class, () -> temaValidator.validate(tema));
    }

    @Test
    void addTema_Description_Empty() {
        Tema tema = new Tema("33", "", 14, 3);
        assertThrows(ValidationException.class, () -> temaValidator.validate(tema));
    }

    @Test
    void addTema_Deadline_LowerThanMinBound() {
        Tema tema = new Tema("33", "bruh", 0, 3);
        assertThrows(ValidationException.class, () -> temaValidator.validate(tema));
    }

    @Test
    void addTema_Deadline_HigherThanMaxBound() {
        Tema tema = new Tema("33", "bruh", 15, 3);
        assertThrows(ValidationException.class, () -> temaValidator.validate(tema));
    }

    @Test
    void addTema_Deadline_LowerThanStartline() {
        Tema tema = new Tema("33", "bruh", 2, 3);
        assertThrows(ValidationException.class, () -> temaValidator.validate(tema));
    }

    @Test
    void addTema_Startline_LowerThanMinBound() {
        Tema tema = new Tema("33", "bruh", 14, 0);
        assertThrows(ValidationException.class, () -> temaValidator.validate(tema));
    }

    @Test
    void addTema_Startline_HigherThanMaxBound() {
        Tema tema = new Tema("33", "bruh", 14, 15);
        assertThrows(ValidationException.class, () -> temaValidator.validate(tema));
    }

    @Test
    void addTema_Startline_HigherThanDealine() {
        Tema tema = new Tema("33", "bruh", 2, 3);
        assertThrows(ValidationException.class, () -> temaValidator.validate(tema));
    }

}
