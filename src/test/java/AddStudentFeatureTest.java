import domain.Student;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.StudentXMLRepo;
import service.Service;
import validation.StudentValidator;
import validation.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

public class AddStudentFeatureTest {

    private static Service service;

    @BeforeAll
    public static void init() {
        StudentValidator studentValidator = new StudentValidator();

        String filenameStudent = "fisiere_mock/Studenti.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        service = new Service(studentXMLRepository, studentValidator, null, null, null, null);
    }

    @AfterAll
    public static void clean() {
        for (final var stud : service.getAllStudenti()) {
            service.deleteStudent(stud.getID());
        }
    }

    @Test
    public void testAddStudent_ValidInput_Succeeds() {
        final var validStudent = new Student("123", "John Doe", 101, "john.doe@example.com");
        try {
            final var savedStudent = service.addStudent(validStudent);
            assertNull(savedStudent, "Student should be added successfully.");
        } catch (ValidationException e) {
            fail("Validation failed for a valid student.");
        }
    }

    @Test
    public void testAddStudent_NullID_ThrowsValidationException() {
        final var student = new Student(null, "Jane Doe", 101, "jane.doe@example.com");
        final var exception = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertTrue(exception.getMessage().contains("Id incorect!"));
    }

    @Test
    public void testAddStudent_EmptyID_ThrowsValidationException() {
        final var student = new Student("", "John Doe", 101, "john.doe@example.com");
        final var exception = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertTrue(exception.getMessage().contains("Id incorect!"));
    }

    @Test
    public void testAddStudent_LongID_DoesNotThrowException() {
        final var student = new Student("12345678901234567890123456789098765432101234567890",
                "John Doe", 101, "john.doe@example.com");
        assertDoesNotThrow(() -> service.addStudent(student));
    }


    @Test
    public void testAddStudent_NullName_ThrowsValidationException() {
        final var student = new Student("127", null, 101, "jane.doe@example.com");
        final var exception = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertTrue(exception.getMessage().contains("Nume incorect!"));
    }

    @Test
    public void testAddStudent_EmptyName_ThrowsValidationException() {
        final var student = new Student("124", "", 101, "john.doe@example.com");
        final var exception = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertTrue(exception.getMessage().contains("Nume incorect!"));
    }

    @Test
    public void testAddStudent_LongName_DoesNotThrowException() {
        final var student = new Student("124",
                "JohnathanMaximillianAlexandersonBellecCorinDemianEminescuFabioGeorge",
                101, "john.doe@example.com");
        assertDoesNotThrow(() -> service.addStudent(student));
    }


    @Test
    public void testAddStudent_ZeroGroup_DoesNotThrowException() {
        final var student = new Student("125", "Jane Doe", 0,
                "jane.doe@example.com");
        assertDoesNotThrow(() -> service.addStudent(student));
    }

    @Test
    public void testAddStudent_NegativeGroup_ThrowsValidationException() {
        final var student = new Student("125", "Jane Doe", -1,
                "jane.doe@example.com");
        final var exception = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertTrue(exception.getMessage().contains("Grupa incorecta!"));
    }

    @Test
    public void testAddStudent_NullEmail_ThrowsValidationException() {
        final var student = new Student("128", "Jane Doe", 101, null);
        final var exception = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertTrue(exception.getMessage().contains("Email incorect!"));
    }

    @Test
    public void testAddStudent_EmptyEmail_ThrowsValidationException() {
        final var student = new Student("126", "Jane Doe", 101, "");
        final var
                exception = assertThrows(ValidationException.class, () -> service.addStudent(student));
        assertTrue(exception.getMessage().contains("Email incorect!"));
    }

    @Test
    public void testAddStudent_LongEmail_DoesNotThrowException() {
        final var student = new Student("126", "Jane Doe", 101,
                "verylongemailaddress12345678901234567890abcdefghijklmnopqrstuvwxyz@example.com");
        assertDoesNotThrow(() -> service.addStudent(student));
    }
}
