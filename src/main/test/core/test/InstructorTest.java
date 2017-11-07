package core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import core.api.IAdmin;
import core.api.IInstructor;
import core.api.IStudent;
import core.api.impl.Admin;
import core.api.impl.Instructor;
import core.api.impl.Student;

public class InstructorTest {

	IInstructor instructor;
	IAdmin admin;
	IStudent student;
	
	@Before
	public void setup() {
		admin = new Admin();
		instructor = new Instructor();
		student = new Student();
	}
	
	/* Test general adding HW */
	@Test
	public void testAddHomework1() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		instructor.addHomework("Instructor1", "Class1", 2017, "Homework1");
		
		assertTrue(instructor.homeworkExists("Class1", 2017, "Homework1"));
	}
	
	/* Test that HW isn't added if the class doesn't exist */
	@Test
	public void testAddHomework_classDoesntExist1() {
		instructor.addHomework("Instructor1", "Class1", 2017, "Homework1");
		
		assertFalse(instructor.homeworkExists("Class1", 2017, "Homework1"));
	}
	
	/* Test that HW isn't added if the instructor isn't added to this class */
	@Test
	public void testAddHomework_instructorNotInClass1() {
		admin.createClass("Class1", 2017, "Instructor2", 1);
		instructor.addHomework("Instructor1", "Class1", 2017, "Homework1");
		
		assertFalse(instructor.homeworkExists("Class1", 2017, "Homework1"));
	}
	
	/* Test grades can be assigned */
	@Test
	public void testAssignGrade1() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		
		instructor.addHomework("Instructor1", "Class1", 2017, "Homework1");
		
		student.registerForClass("Student1", "Class1", 2017);
		student.submitHomework("Student1", "Homework1", "Answer", "Class1", 2017);
		
		instructor.assignGrade("Instructor1", "Class1", 2017, "Homework1", "Student1", 100);
		
		assertEquals(new Integer(100), instructor.getGrade("Class1", 2017, "Homework1", "Student1"));
	}
	
	/* Test cannot assign grade if the instructor is not that instructor of that class */
	@Test
	public void testAssignGrade_instructorNotInClass1() {
		admin.createClass("Class1", 2017, "Instructor2", 1);
		
		instructor.addHomework("Instructor2", "Class1", 2017, "Homework1");

		student.registerForClass("Student1", "Class1", 2017);
		student.submitHomework("Student1", "Homework1", "Answer", "Class1", 2017);
		
		instructor.assignGrade("Instructor1", "Class1", 2017, "Homework1", "Student1", 100);
		
		assertNotEquals(new Integer(100), instructor.getGrade("Class1", 2017, "Homework1", "Student1"));
	}
	
	/* Test cannot assign grade if that HW was not assigned */
	@Test
	public void testAssignGrade_notAssignedHomework1() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		
		student.registerForClass("Student1", "Class1", 2017);
		student.submitHomework("Student1", "Homework1", "Answer", "Class1", 2017);
		
		instructor.assignGrade("Instructor1", "Class1", 2017, "Homework1", "Student1", 100);
		
		assertNotEquals(new Integer(100), instructor.getGrade("Class1", 2017, "Homework1", "Student1"));
	}
	
	/* Test cannot assign grade if student hasn't submitted it */
	@Test
	public void testAssignGrade_notSubmittedHomework1() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		
		instructor.addHomework("Instructor1", "Class1", 2017, "Homework1");
		
		student.registerForClass("Student1", "Class1", 2017);
		instructor.assignGrade("Instructor1", "Class1", 2017, "Homework1", "Student1", 100);
		
		assertNotEquals(new Integer(100), instructor.getGrade("Class1", 2017, "Homework1", "Student1"));
	}
	
	/* Test grade given is within percentage bounds */
	@Test
	public void testAssignGrade_percentage1() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		
		instructor.addHomework("Instructor1", "Class1", 2017, "Homework1");
		
		student.registerForClass("Student1", "Class1", 2017);
		student.submitHomework("Student1", "Homework1", "Answer", "Class1", 2017);

		instructor.assignGrade("Instructor1", "Class1", 2017, "Homework1", "Student1", 110);
		
		assertNotEquals(new Integer(110), instructor.getGrade("Class1", 2017, "Homework1", "Student1"));
	}
	
	@Test
	public void testAssignGrade_percentage2() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		
		instructor.addHomework("Instructor1", "Class1", 2017, "Homework1");
		
		student.registerForClass("Student1", "Class1", 2017);
		student.submitHomework("Student1", "Homework1", "Answer", "Class1", 2017);

		instructor.assignGrade("Instructor1", "Class1", 2017, "Homework1", "Student1", -10);
		
		assertNotEquals(new Integer(-10), instructor.getGrade("Class1", 2017, "Homework1", "Student1"));
	}
}
