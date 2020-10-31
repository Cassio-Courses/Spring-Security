package br.com.cassio.springauth.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.cassio.springauth.model.Student;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/students")
public class StudentController {

	private static final List<Student> STUDENTS = Arrays.asList(
			new Student(1, "Cassio"),
			new Student(2, "Maria"),
			new Student(3, "Jorge"));

	@GetMapping(path = "{studentId}")
	public Student getStudents(@PathVariable("studentId") Integer studentId) throws IllegalAccessException {
		return STUDENTS.stream()
				.filter(student -> studentId.equals(student.getId()))
				.findFirst()
				.orElseThrow(() -> new IllegalAccessException("Student " + studentId + " does not exists"));
	}

}
