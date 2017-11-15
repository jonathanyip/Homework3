package core.test;

import static org.junit.Assert.*;

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
		admin.createClass("Class", 2017, "Instructor", 1);
		instructor.addHomework("Instructor", "Class", 2017, "Homework");
		
		assertTrue(instructor.homeworkExists("Class", 2017, "Homework"));
	}

	/* Test that HW isn't added if the class doesn't exist */
	@Test
	public void testAddHomework_classDoesntExist1() {
		instructor.addHomework("Instructor", "Class", 2017, "Homework");
		
		assertFalse(instructor.homeworkExists("Class", 2017, "Homework"));
	}
	
	/* Test that HW isn't added if the instructor isn't added to this class */
	@Test
	public void testAddHomework_instructorNotInClass1() {
		admin.createClass("Class", 2017, "Instructor2", 1);
		instructor.addHomework("Instructor1", "Class", 2017, "Homework");
		
		assertFalse(instructor.homeworkExists("Class", 2017, "Homework"));
	}
	
	/* Test that null's don't do unusual things */
	@Test
	public void testAddHomework_nulls() {
		try {
			instructor.addHomework(null, null, 2017, null);
		} catch(Exception e) {
			fail("Raised exception with null!");
		}
	}
	
	/* Test grades can be assigned */
	@Test
	public void testAssignGrade1() {
		admin.createClass("Class", 2017, "Instructor", 1);
		
		instructor.addHomework("Instructor", "Class", 2017, "Homework");
		
		student.registerForClass("Student", "Class", 2017);
		student.submitHomework("Student", "Homework", "Answer", "Class", 2017);
		
		instructor.assignGrade("Instructor", "Class", 2017, "Homework", "Student", 50);
		
		assertEquals(new Integer(50), instructor.getGrade("Class", 2017, "Homework", "Student"));
	}
	
	/* Test can assign grade 0 */
	@Test
	public void testAssignGrade2() {
		admin.createClass("Class", 2017, "Instructor", 1);
		
		instructor.addHomework("Instructor", "Class", 2017, "Homework");
		
		student.registerForClass("Student", "Class", 2017);
		student.submitHomework("Student", "Homework", "Answer", "Class", 2017);
		
		instructor.assignGrade("Instructor", "Class", 2017, "Homework", "Student", 0);
		
		assertEquals(new Integer(0), instructor.getGrade("Class", 2017, "Homework", "Student"));
	}
	
	/* Test can assign grade 100 */
	@Test
	public void testAssignGrade3() {
		admin.createClass("Class", 2017, "Instructor", 1);
		
		instructor.addHomework("Instructor", "Class", 2017, "Homework");
		
		student.registerForClass("Student", "Class", 2017);
		student.submitHomework("Student", "Homework", "Answer", "Class", 2017);
		
		instructor.assignGrade("Instructor", "Class", 2017, "Homework", "Student", 100);
		
		assertEquals(new Integer(100), instructor.getGrade("Class", 2017, "Homework", "Student"));
	}
	
	/* Test cannot assign grade if the instructor is not that instructor of that class */
	@Test
	public void testAssignGrade_instructorNotInClass1() {
		admin.createClass("Class", 2017, "Instructor2", 1);
		
		instructor.addHomework("Instructor2", "Class", 2017, "Homework");

		student.registerForClass("Student", "Class", 2017);
		student.submitHomework("Student", "Homework", "Answer", "Class", 2017);
		
		instructor.assignGrade("Instructor1", "Class", 2017, "Homework", "Student", 100);
		
		assertNotEquals(new Integer(100), instructor.getGrade("Class", 2017, "Homework", "Student"));
	}
	
	/* Test cannot assign grade if that HW was not assigned */
	@Test
	public void testAssignGrade_notAssignedHomework1() {
		admin.createClass("Class", 2017, "Instructor", 1);
		
		student.registerForClass("Student", "Class", 2017);
		student.submitHomework("Student", "Homework", "Answer", "Class", 2017);
		
		instructor.assignGrade("Instructor", "Class", 2017, "Homework", "Student", 100);
		
		assertNotEquals(new Integer(100), instructor.getGrade("Class", 2017, "Homework", "Student"));
	}
	
	/* Test cannot assign grade if student hasn't submitted it */
	@Test
	public void testAssignGrade_notSubmittedHomework1() {
		admin.createClass("Class", 2017, "Instructor", 1);
		
		instructor.addHomework("Instructor", "Class", 2017, "Homework");
		
		student.registerForClass("Student", "Class", 2017);
		instructor.assignGrade("Instructor", "Class", 2017, "Homework", "Student", 100);
		
		assertNotEquals(new Integer(100), instructor.getGrade("Class", 2017, "Homework", "Student"));
	}
	
	/* Test grade given is within percentage bounds */
	@Test
	public void testAssignGrade_percentage1() {
		admin.createClass("Class", 2017, "Instructor", 1);
		
		instructor.addHomework("Instructor", "Class", 2017, "Homework");
		
		student.registerForClass("Student", "Class", 2017);
		student.submitHomework("Student", "Homework", "Answer", "Class", 2017);

		instructor.assignGrade("Instructor", "Class", 2017, "Homework", "Student", 110);
		
		assertNotEquals(new Integer(110), instructor.getGrade("Class", 2017, "Homework", "Student"));
	}
	
	@Test
	public void testAssignGrade_percentage2() {
		admin.createClass("Class", 2017, "Instructor", 1);
		
		instructor.addHomework("Instructor", "Class", 2017, "Homework");
		
		student.registerForClass("Student", "Class", 2017);
		student.submitHomework("Student", "Homework", "Answer", "Class", 2017);

		instructor.assignGrade("Instructor", "Class", 2017, "Homework", "Student", -10);
		
		assertNotEquals(new Integer(-10), instructor.getGrade("Class", 2017, "Homework", "Student"));
	}
	
	/* Test that null's don't do unusual things */
	@Test
	public void testAssignGrade_nulls() {
		try {
			instructor.assignGrade(null, null, 2017, null, null, 50);
		} catch(Exception e) {
			fail("Raised exception with null!");
		}
	}
}
