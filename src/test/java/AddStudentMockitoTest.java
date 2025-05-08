import domain.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.StudentFileRepository;
import repository.StudentXMLRepo;
import service.Service;
import validation.StudentValidator;
import validation.ValidationException;
import validation.Validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddStudentMockitoTest {

    @Mock
    private StudentValidator validator;

    @Mock
    private StudentXMLRepo studentFileRepository;

    @InjectMocks
    private Service service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void addStudent_ShouldReturnNull_StudentAddedToList() {
        Student student = new Student("1", "name", 123, "email");

        doNothing().when(validator).validate(student);
        when(studentFileRepository.save(student)).thenReturn(null);

        Object result =  service.addStudent(student);
        assertNull(result);
    }

    @Test
    void addStudent_ShouldThrowValidationException_EmptyStudentId() {
        Student student = new Student("", "name", 123, "email");

        doThrow(new ValidationException("Id incorect!")).when(validator).validate(student);
        when(studentFileRepository.save(student)).thenReturn(null);

        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudent_ShouldThrowValidationException_NullStudentId() {
        Student student = new Student(null, "name", 123, "email");

        doThrow(new ValidationException("Id incorect!")).when(validator).validate(student);
        when(studentFileRepository.save(student)).thenReturn(null);

        assertThrows(ValidationException.class, () -> service.addStudent(student));
    }

    @Test
    void addStudent_ShouldReturnStudent_StudentExistsInRepository() {
        Student student = new Student("1", "name", 123, "email");

        doNothing().when(validator).validate(student);
        when(studentFileRepository.save(student)).thenReturn(student);

        Object result = service.addStudent(student);
        assertEquals(student, result);
    }
}
