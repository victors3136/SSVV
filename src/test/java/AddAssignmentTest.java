import domain.Tema;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class AddAssignmentTest {

    private static Service service;

    @BeforeAll
    public static void init() {
        TemaValidator temaValidator = new TemaValidator();

        String filenameStudent = "fisiere_mock/Teme.xml";

        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameStudent);
        service = new Service(null, null, temaXMLRepository, temaValidator, null, null);
    }

    @AfterAll
    public static void clean() {
        for (final var tema : service.getAllTeme()) {
            service.deleteTema(tema.getID());
        }
    }

    @Test
    public void testAddAssignment_ValidTema_SuccessfullyAdded() {
        Tema tema = new Tema("1", "Tema JUnit", 10, 8);

        Tema result = service.addTema(tema);

        assertNotNull(result);
        assertEquals(tema, service.findTema("1"));
    }


    @Test
    public void testAddAssignment_InvalidID_ThrowsValidationException() {

        Tema invalidTema = new Tema("", "Descriere", 10, 8);

        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addTema(invalidTema);
        });

        assertEquals("Numar tema invalid!", exception.getMessage());
    }

    @Test
    public void testAddAssignment_InvalidDescription_ThrowsValidationException() {

        Tema invalidTema = new Tema("1", "", 10, 8);

        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addTema(invalidTema);
        });

        assertEquals("Descriere invalida!", exception.getMessage());
    }

    @Test
    public void testAddAssignment_InvalidDeadline_ThrowsValidationException() {

        Tema invalidTema = new Tema("1", "Descriere", -1, 8);

        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addTema(invalidTema);
        });

        assertEquals("Deadlineul trebuie sa fie intre 1-14.", exception.getMessage());
    }

    @Test
    public void testAddAssignment_InvalidPrimire_ThrowsValidationException() {

        Tema invalidTema = new Tema("1", "Descriere", 10, 16);

        Exception exception = assertThrows(ValidationException.class, () -> {
            service.addTema(invalidTema);
        });

        assertEquals("Saptamana primirii trebuie sa fie intre 1-14.", exception.getMessage());
    }

    @Test
    public void testAddAssignment_ExistingID_ThrowsValidationException() {
        Tema tema = new Tema("1", "Tema JUnit", 10, 8);
        service.addTema(tema);

        // Get the length of the list before adding a duplicate
        int initialSize = ((Collection<?>)service.getAllTeme()).size();
        Tema duplicateTema = new Tema("1", "Tema JUnit 2", 10, 8);

        Tema result = service.addTema(duplicateTema);

        int finalSize = ((Collection<?>)service.getAllTeme()).size();
        assertEquals(initialSize, finalSize);
    }
}
