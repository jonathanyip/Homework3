package core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

import core.api.IAdmin;
import core.api.impl.Admin;
import core.api.impl.Student;

public class AdminTest {
	
	private IAdmin admin;
	
	@Before
	public void setup() {
		admin = new Admin();
	}

	/* Test Capacity */
	@Test
	public void testCreateClass_capacity1() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		assertTrue(admin.classExists("Class1", 2017));
		assertEquals(admin.getClassCapacity("Class1", 2017), 1);
	}
	
	@Test
	public void testCreateClass_capacity2() {
		admin.createClass("Class1", 2017, "Instructor1", 0);
		assertFalse(admin.classExists("Class1", 2017));
	}
	
	@Test
	public void testCreateClass_capacity3() {
		admin.createClass("Class1", 2017, "Instructor1", -1);
		assertFalse(admin.classExists("Class1", 2017));
	}
	
	/* Test unique className/Year pair */
	@Test
	public void testCreateClass_uniqueClassYear1() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		admin.createClass("Class1", 2017, "Instructor2", 1);
		
		assertNotEquals("Instructor2", admin.getClassInstructor("Class1", 2017));
	}
	
	@Test
	public void testCreateClass_uniqueClassYear2() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		admin.createClass("Class2", 2017, "Instructor2", 1);
		
		assertEquals("Instructor1", admin.getClassInstructor("Class1", 2017));
		assertEquals("Instructor2", admin.getClassInstructor("Class2", 2017));
	}
	
	/* Test no instructor can have more than 2 classes per year */
	@Test
	public void testCreateClass_twoClassInYear1() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		admin.createClass("Class2", 2017, "Instructor1", 1);
		admin.createClass("Class3", 2017, "Instructor1", 1);
		
		assertTrue(admin.classExists("Class1", 2017));
		assertTrue(admin.classExists("Class2", 2017));
		assertFalse(admin.classExists("Class3", 2017));
	}
	
	/* Test can't make classes in the past */
	@Test
	public void testCreateClass_noPastClass1() {
		admin.createClass("Class1", 2016, "Instructor1", 1);
		assertFalse(admin.classExists("Class1", 2016));
	}
	
	@Test
	public void testCreateClass_noPastClass2() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		assertTrue(admin.classExists("Class1", 2017));
	}
	
	/* Test that change capacity changes the capacity of a class */
	@Test
	public void testChangeCapacity_changeCapacity1() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		admin.changeCapacity("Class1", 2017, 5);
		
		assertEquals(5, admin.getClassCapacity("Class1", 2017));
	}
	
	@Test
	public void testChangeCapacity_changeCapacity2() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		admin.changeCapacity("Class1", 2017, -1);
		
		assertNotEquals(-1, admin.getClassCapacity("Class1", 2017));
	}
	
	@Test
	public void testChangeCapacity_changeCapacity3() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		admin.changeCapacity("Class1", 2017, 0);
		
		assertNotEquals(0, admin.getClassCapacity("Class1", 2017));
	}
	
	/* Test that new capacity is at least equal to number of enrolled */
	@Test
	public void testChangeCapacity_atLeastEnrolled1() {
		admin.createClass("Class1", 2017, "Instructor1", 5);
		
		Student student1 = new Student();
		student1.registerForClass("Student1", "Class1", 2017);
		Student student2 = new Student();
		student2.registerForClass("Student2", "Class1", 2017);
		
		admin.changeCapacity("Class1", 2017, 2);
		assertEquals(2, admin.getClassCapacity("Class1", 2017));
	}
	
	@Test
	public void testChangeCapacity_atLeastEnrolled2() {
		admin.createClass("Class1", 2017, "Instructor1", 5);
		
		Student student1 = new Student();
		student1.registerForClass("Student1", "Class1", 2017);
		Student student2 = new Student();
		student2.registerForClass("Student2", "Class1", 2017);
		
		admin.changeCapacity("Class1", 2017, 1);
		assertNotEquals(1, admin.getClassCapacity("Class1", 2017));
	}
}
