package br.com.cassio.springauth.controller;

import static java.lang.String.format;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cassio.springauth.model.Student;

@RestController
@CrossOrigin
@RequestMapping("/admin/api/v1/students")
public class StudentManagementController {
	Logger log = LoggerFactory.getLogger(StudentManagementController.class);

	private static List<Student> STUDENTS = Arrays.asList(
			new Student(1, "Cassio"),
			new Student(2, "Maria"),
			new Student(3, "Jorge"));

	@GetMapping
	public List<Student> getStudents() {
		return STUDENTS;
	}

	@PostMapping
	public void registerNewStudent(@RequestBody Student student) {
		System.out.println("registerNewStudent" );
		System.out.println(student);

	}

	@DeleteMapping(value = "/{studentId}")
	public void deleteStudent(@PathVariable("studentId") Integer studentId) {
		System.out.println("deleteStudent");
		System.out.println(studentId);
	}

	@PutMapping(value = "/{studentId}")
	public void updateStudent(@PathVariable("studentId") Integer studentId, @RequestBody Student newStudent) {
		System.out.println("updateStudent");
		System.out.println(String.format("%s %s", studentId, newStudent));

	}

	@GetMapping(path = "{studentId}")
	public Student getStudents(@PathVariable("studentId") Integer studentId) throws IllegalAccessException {
		System.out.println(format("getStudents{%d}", studentId));
		return STUDENTS.stream()
				.filter(student -> studentId.equals(student.getId()))
				.findFirst()
				.orElseThrow(() -> new IllegalAccessException("Student " + studentId + " does not exists"));
	}

}
