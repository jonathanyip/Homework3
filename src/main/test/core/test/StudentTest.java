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

public class StudentTest {

	public IAdmin admin;
	public IInstructor instructor;
	public IStudent student;
	
	@Before
	public void setup() {
		admin = new Admin();
		instructor = new Instructor();
		student = new Student();
	}
	
	/* Test a student can enroll in a class, normally */
	@Test
	public void testRegisterForClass_general1() {
		admin.createClass("Class", 2017, "Instructor", 5);
		student.registerForClass("Student", "Class", 2017);
		
		assertTrue(student.isRegisteredFor("Student", "Class", 2017));
	}
	
	/* Test two students can fit comfortably in a 2 space class */
	@Test
	public void testRegisterForClass_twoSpaces1() {
		admin.createClass("Class", 2017, "Instructor", 2);
		student.registerForClass("Student1", "Class", 2017);
		student.registerForClass("Student2", "Class", 2017);
		
		assertTrue(student.isRegisteredFor("Student1", "Class", 2017));
		assertTrue(student.isRegisteredFor("Student2", "Class", 2017));
	}
	
	/* Test that student is not enrolled if full */
	@Test
	public void testRegisterForClass_enrollmentFull1() {
		admin.createClass("Class", 2017, "Instructor", 1);
		student.registerForClass("Student1", "Class", 2017);
		student.registerForClass("Student2", "Class", 2017);
		
		assertTrue(student.isRegisteredFor("Student1", "Class", 2017));
		assertFalse(student.isRegisteredFor("Student2", "Class", 2017));
	}
	
	/* Test that student cannot enroll in non-existent class */
	@Test
	public void testRegisterForClass_classDoesntExist1() {
		student.registerForClass("Student", "Class", 2017);
		
		assertFalse(student.isRegisteredFor("Student", "Class", 2017));
	}
	
	/* Test that student can drop class, normally */
	@Test
	public void testDropClass_general1() {
		admin.createClass("Class", 2017, "Instructor", 1);
		student.registerForClass("Student", "Class", 2017);
		student.dropClass("Student", "Class", 2017);
		
		assertFalse(student.isRegisteredFor("Student", "Class", 2017));
	}
	
	/* Test that student cant drop class they are not enrolled in */
	@Test
	public void testDropClass_general2() {
		admin.createClass("Class", 2017, "Instructor", 5);
		student.dropClass("Student", "Class", 2017);
		
		assertFalse(student.isRegisteredFor("Student", "Class", 2017));
	}
	
	/* Test that student can submit homework, normally */
	@Test
	public void testSubmitHomework_general1() {
		admin.createClass("Class", 2017, "Instructor", 5);
		instructor.addHomework("Instructor", "Class", 2017, "Homework");
		student.registerForClass("Student", "Class", 2017);
		
		student.submitHomework("Student", "Homework", "Answer", "Class", 2017);
		
		assertTrue(student.hasSubmitted("Student", "Homework", "Class", 2017));
	}
	
	/* Test can't submit HW for non existent HW */
	@Test
	public void testSubmitHomework_HWDoesntExist() {
		admin.createClass("Class", 2017, "Instructor", 5);
		
		student.registerForClass("Student", "Class", 2017);
		student.submitHomework("Student", "Homework", "Answer", "Class", 2017);
		
		assertFalse(student.hasSubmitted("Student", "Homework", "Class", 2017));
	}
	
	/* Test can't submit HW for class not registered in */
	@Test
	public void testSubmitHomework_notRegistered1() {
		admin.createClass("Class", 2017, "Instructor", 5);
		instructor.addHomework("Instructor", "Class", 2017, "Homework");
		
		student.submitHomework("Student", "Homework", "Answer", "Class", 2017);
		
		assertFalse(student.hasSubmitted("Student", "Homework", "Class", 2017));
	}
	
	/* Test can't submit HW for future class */
	@Test
	public void testSubmitHomework_futureYear1() {
		admin.createClass("Class", 2018, "Instructor", 5);
		instructor.addHomework("Instructor", "Class", 2018, "Homework");
		
		student.registerForClass("Student", "Class", 2018);
		student.submitHomework("Student", "Homework", "Answer", "Class", 2018);
		
		assertFalse(student.hasSubmitted("Student", "Homework", "Class", 2018));
	}
}
