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

        assertNull(result);
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
}
