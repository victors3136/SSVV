import curent.Curent;
import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import java.time.LocalDate;

public class IntegrationTests {

    private static Service service;
    private static final LocalDate FOURTH_WEEK = Curent.getStartDate().plusWeeks(4);
    @BeforeAll
    public static void init() {
        final var filenameTeme = "fisiere_mock/Teme.xml";
        final var filenameNota = "fisiere_mock/Note.xml";
        final var filenameStudent = "fisiere_mock/Studenti.xml";
        final var temaRepository = new TemaXMLRepo(filenameTeme);
        final var notaRepository = new NotaXMLRepo(filenameNota);
        final var studentRepository = new StudentXMLRepo(filenameStudent);
        final var temaValidator = new TemaValidator();
        final var studentValidator = new StudentValidator();
        final var notaValidator = new NotaValidator(studentRepository, temaRepository);
        service = new Service(
                studentRepository, studentValidator,
                temaRepository, temaValidator,
                notaRepository, notaValidator
        );
    }

    @AfterAll
    public static void clean() {
        for (final var nota : service.getAllNote()) {
            service.deleteNota(nota.getID());
        }
        for (final var tema : service.getAllTeme()) {
            service.deleteTema(tema.getID());
        }
        for (final var student : service.getAllStudenti()) {
            service.deleteStudent(student.getID());
        }
    }

    @Test
    public void testAddStudent() {
        final var student = new Student("1", "John Doe", 931, "john.doe@example.com");
        Assertions.assertDoesNotThrow(() -> service.addStudent(student));
        final var fetched = service.findStudent("1");
        Assertions.assertNotNull(fetched, "Student should be retrievable");
        Assertions.assertEquals("John Doe", fetched.getNume());
    }

    @Test
    public void testAddTema() {
        final var tema = new Tema("1", "desc", 14, 13);
        Assertions.assertDoesNotThrow(() -> service.addTema(tema));
        final var fetched = service.findTema("1");
        Assertions.assertNotNull(fetched, "Tema should be retrievable");
        Assertions.assertEquals("desc", fetched.getDescriere());
    }

    @Test
    public void testAddNota() {
        service.addStudent(new Student("2", "Jane Smith", 932, "jane.smith@example.com"));
        service.addTema(new Tema("2", "tema2", 14, 13));


        final var nota = new Nota("2", "2", "2", 9.5, FOURTH_WEEK);
        Assertions.assertDoesNotThrow(() -> service.addNota(nota, "great job"));

        final var fetched = service.findNota("2");
        Assertions.assertNotNull(fetched, "Nota should be retrievable");
        Assertions.assertEquals(9.5, fetched.getNota());
    }

    @Test
    public void integrationTesting_Full() {
        final var student = new Student("3", "Ana Popescu", 933, "ana@example.com");
        final var tema = new Tema("3", "tema integrata", 14, 13);
        final var nota = new Nota("3", "3", "3", 10, FOURTH_WEEK);

        Assertions.assertDoesNotThrow(() -> {
            service.addStudent(student);
            service.addTema(tema);
            service.addNota(nota, "excelent");
        });

        Assertions.assertNotNull(service.findStudent("3"));
        Assertions.assertNotNull(service.findTema("3"));
        Assertions.assertNotNull(service.findNota("3"));
    }

    @Test
    public void incrementalIntegrationTesting_Student() {
        final var student = new Student("4", "Radu Ionescu", 934, "radu@example.com");
        Assertions.assertDoesNotThrow(() -> service.addStudent(student));
        Assertions.assertNotNull(service.findStudent("4"));
    }

    @Test
    public void incrementalIntegrationTesting_StudentAndTema() {
        incrementalIntegrationTesting_Student(); // already adds student

        final var tema = new Tema("4", "tema inc.", 14, 13);
        Assertions.assertDoesNotThrow(() -> service.addTema(tema));
        Assertions.assertNotNull(service.findTema("4"));
    }

    @Test
    public void incrementalIntegrationTesting_StudentTemaAndNota() {
        incrementalIntegrationTesting_StudentAndTema();

        final var nota = new Nota("4", "4", "4", 8.7, FOURTH_WEEK);
        Assertions.assertDoesNotThrow(() -> service.addNota(nota, "bine"));
        Assertions.assertNotNull(service.findNota("4"));
    }
}