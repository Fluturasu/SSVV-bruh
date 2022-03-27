package service;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.*;
import validation.*;
import validation.ValidationException;

import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

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
    void saveStudentGrupa() {
        int grupa = -222;
        int returnValue = service.saveStudent("19", "bruhtest", grupa);
        assertEquals(returnValue, 1);
        assertNull(fileRepository1.findOne("19"));
    }

    @Test
    void saveStudentGrupaWithNegativeGrupa() {
        int grupa = -1;
        int returnValue = service.saveStudent("19", "bruhtest", grupa);
        assertEquals(returnValue, 1);
        assertNull(fileRepository1.findOne("19"));
    }

    @Test
    void saveStudentWithEmptyId() {
        String id = "";
        String nume = "andrei";
        int grupa = 933;
        int returnValue = service.saveStudent(id, nume, grupa);
        assertEquals(returnValue, 1);
        assertNull(fileRepository1.findOne(id));
    }

    @Test
    void saveStudentWithEmptyName() {
        String id = "castravete";
        String nume = "";
        int grupa = 499;
        int returnValue = service.saveStudent(id, nume, grupa);
        assertEquals(returnValue, 1);
        assertNull(fileRepository1.findOne(id));
    }


    @Test
    void saveStudentWithMinBoundaryGroup() {
        String id = "999999";
        String nume = "2";
        int grupa = 111;
        int returnValue = service.saveStudent(id,nume,grupa);
        assertEquals(returnValue, 1);
        assertNotNull(fileRepository1.findOne(id));
    }

    @Test
    void saveStudentId() {
        String id = "-1";
        int returnValue = service.saveStudent(id, "bruhtest", 222);
        assertEquals(returnValue, 1);
        assertNotNull(fileRepository1.findOne(id));
    }
}