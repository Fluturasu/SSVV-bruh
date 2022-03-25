package service;

import domain.Nota;
import domain.Tema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.*;
import validation.*;
import validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    private StudentValidator studentValidator = new StudentValidator();
    private Validator<Tema> temaValidator = new TemaValidator();
    private Validator<Nota> notaValidator = new NotaValidator();


    StudentXMLRepository fileRepository1 = new StudentXMLRepository(studentValidator, "studenti.xml");
    TemaXMLRepository fileRepository2 = new TemaXMLRepository(temaValidator, "teme.xml");
    NotaXMLRepository fileRepository3 = new NotaXMLRepository(notaValidator, "note.xml");

    private Service service = new Service(fileRepository1, fileRepository2, fileRepository3);

    @BeforeEach
    void setUp() {
        }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveTema() {

    }

    @Test
    void saveStudentGrupa() {
        int grupa = -222;
        int returnValue = service.saveStudent("19", "bruhtest", grupa);
        assertEquals(returnValue, 1);
    }

    @Test
    void saveStudentGrupaWithNegativeGrupa() {
        int grupa = -1;
        int returnValue = service.saveStudent("19", "bruhtest", grupa);
        assertEquals(returnValue, 1);
    }

    @Test
    void saveStudentWithEmptyId() {
        String id = "";
        String nume = "andrei";
        int grupa = 933;
        int returnValue = service.saveStudent(id, nume, grupa);
        assertEquals(returnValue, 1);
    }

    @Test
    void saveStudentWithEmptyName() {
        String id = "castravete";
        String nume = "";
        int grupa = 499;
        int returnValue = service.saveStudent(id, nume, grupa);
        assertEquals(returnValue, 1);
    }

    @Test
    void saveStudentId() {
        String id = "-1";
        service.saveStudent(id, "bruhtest", 222);
    }
}