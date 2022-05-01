package service;

import domain.Nota;
import domain.Pair;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepository;
import repository.StudentXMLRepository;
import repository.TemaXMLRepository;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.Validator;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TopDownIntegration {

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
        service.saveStudent("1", "andrei", 933);
        service.saveTema("1", "ok", 14, 1);
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
    public void addStudent() {
        String id = "2";
        String nume = "radu";
        int grupa = 933;
        int returnValue = service.saveStudent(id, nume, grupa);
        assertEquals(returnValue, 1);
        assertNotNull(fileRepository1.findOne(id));
    }


    @Test
    void addStudent_addTema() {
        String id = "2";
        String nume = "radu";
        int grupa = 933;
        int returnValue = service.saveStudent(id, nume, grupa);
        assertEquals(returnValue, 1);

        String idTema = "2";
        String descriere = "ok";
        int deadline = 14;
        int startline = 1;
        service.saveTema(idTema, descriere, deadline, startline);
        assertNotNull(fileRepository2.findOne("2"));
    }

    @Test
    void addStudent_addTema_addNota() {
        String id = "2";
        String nume = "radu";
        int grupa = 933;
        int returnValue = service.saveStudent(id, nume, grupa);


        String idTema = "2";
        String descriere = "ok";
        int deadline = 14;
        int startline = 1;
        service.saveTema(idTema, descriere, deadline, startline);

        service.saveNota("2", "2", 10, 14, "bruh");
        assertNotNull(fileRepository3.findOne(new Pair<>("2", "2")));

    }
}
