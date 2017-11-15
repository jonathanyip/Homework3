package core.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import core.api.IAdmin;
import core.api.IStudent;
import core.api.impl.Admin;
import core.api.impl.Student;

public class AdminTest {
	
	private IAdmin admin;
	private IStudent student;
	
	@Before
	public void setup() {
		admin = new Admin();
		student = new Student();
	}

	/* Test Capacity Works */
	@Test
	public void testCreateClass_capacity1() {
		admin.createClass("Class", 2017, "Instructor", 1);
		assertTrue(admin.classExists("Class", 2017));
		assertEquals(1, admin.getClassCapacity("Class", 2017));
		assertEquals("Instructor", admin.getClassInstructor("Class", 2017));
	}
	
	/* Test can't have 0 capacity */
	@Test
	public void testCreateClass_capacity2() {
		admin.createClass("Class", 2017, "Instructor", 0);
		assertFalse(admin.classExists("Class", 2017));
	}
	
	/* Test can't have -1 capacity */
	@Test
	public void testCreateClass_capacity3() {
		admin.createClass("Class", 2017, "Instructor", -1);
		assertFalse(admin.classExists("Class", 2017));
	}
	
	/* Test can't create more non-unique className/Year pair */
	@Test
	public void testCreateClass_uniqueClassYear1() {
		admin.createClass("Class", 2017, "Instructor1", 1);
		admin.createClass("Class", 2017, "Instructor2", 1);
		
		assertNotEquals("Instructor2", admin.getClassInstructor("Class", 2017));
	}
	
	/* Test can create more unique className/Year pair */
	@Test
	public void testCreateClass_uniqueClassYear2() {
		admin.createClass("Class1", 2017, "Instructor1", 1);
		admin.createClass("Class2", 2017, "Instructor2", 1);
		
		assertEquals("Instructor1", admin.getClassInstructor("Class1", 2017));
		assertEquals("Instructor2", admin.getClassInstructor("Class2", 2017));
	}
	
	/* Test instructor can't have more than 2 classes per year */
	@Test
	public void testCreateClass_twoClassInYear1() {
		admin.createClass("Class1", 2017, "Instructor", 1);
		admin.createClass("Class2", 2017, "Instructor", 1);
		admin.createClass("Class3", 2017, "Instructor", 1);
		
		assertTrue(admin.classExists("Class1", 2017));
		assertTrue(admin.classExists("Class2", 2017));
		assertFalse(admin.classExists("Class3", 2017));
	}

	/* Test instructor can have exactly 2 classes */
	@Test
	public void testCreateClass_twoClassInYear2() {
		admin.createClass("Class1", 2017, "Instructor", 1);
		admin.createClass("Class2", 2017, "Instructor", 1);
		
		assertTrue(admin.classExists("Class1", 2017));
		assertTrue(admin.classExists("Class2", 2017));
	}
	
	/* Test can't make classes in the past */
	@Test
	public void testCreateClass_noPastClass1() {
		admin.createClass("Class", 2016, "Instructor", 1);
		assertFalse(admin.classExists("Class", 2016));
	}
	
	/* Test can make class in present */
	@Test
	public void testCreateClass_noPastClass2() {
		admin.createClass("Class", 2017, "Instructor", 1);
		assertTrue(admin.classExists("Class", 2017));
	}
	
	/* Test that null's don't do unusual things */
	@Test
	public void testCreateClass_nulls() {
		try {
			admin.createClass(null, 2017, null, 1);
			assertFalse(admin.classExists(null, 2017));
		} catch(Exception e) {
			fail("Raised exception with null!");
		}
	}
	
	/* Test that change capacity changes the capacity of a class */
	@Test
	public void testChangeCapacity_changeCapacity1() {
		admin.createClass("Class", 2017, "Instructor", 1);
		admin.changeCapacity("Class", 2017, 5);
		
		assertEquals(5, admin.getClassCapacity("Class", 2017));
	}
	
	/* Test can't change capacity to -1 */
	@Test
	public void testChangeCapacity_changeCapacity2() {
		admin.createClass("Class", 2017, "Instructor", 1);
		admin.changeCapacity("Class", 2017, -1);
		
		assertNotEquals(-1, admin.getClassCapacity("Class", 2017));
	}
	
	/* Test can't change capacity to 0 */
	@Test
	public void testChangeCapacity_changeCapacity3() {
		admin.createClass("Class", 2017, "Instructor", 1);
		admin.changeCapacity("Class", 2017, 0);
		
		assertNotEquals(0, admin.getClassCapacity("Class", 2017));
	}
	
	/* Test that can change capacity to enrolled limits */
	@Test
	public void testChangeCapacity_atLeastEnrolled1() {
		admin.createClass("Class", 2017, "Instructor", 5);
		
		student.registerForClass("Student1", "Class", 2017);
		student.registerForClass("Student2", "Class", 2017);
		
		admin.changeCapacity("Class", 2017, 2);
		assertEquals(2, admin.getClassCapacity("Class", 2017));
	}
	
	/* Test that change capacity is at least equal to number of enrolled */
	@Test
	public void testChangeCapacity_atLeastEnrolled2() {
		admin.createClass("Class", 2017, "Instructor", 5);
		
		student.registerForClass("Student1", "Class", 2017);
		student.registerForClass("Student2", "Class", 2017);
		
		admin.changeCapacity("Class", 2017, 1);
		assertNotEquals(1, admin.getClassCapacity("Class", 2017));
		assertTrue(student.isRegisteredFor("Student1", "Class", 2017));
		assertTrue(student.isRegisteredFor("Student2", "Class", 2017));
	}
	
	/* Test that if we change the capacity, students can be enrolled */
	@Test
	public void testChangeCapacity_canAddStudent1() {
		admin.createClass("Class", 2017, "Instructor", 2);
		
		student.registerForClass("Student1", "Class", 2017);
		student.registerForClass("Student2", "Class", 2017);
		
		admin.changeCapacity("Class", 2017, 3);
		
		student.registerForClass("Student3", "Class", 2017);
		assertEquals(3, admin.getClassCapacity("Class", 2017));
		assertTrue(student.isRegisteredFor("Student3", "Class", 2017));
	}
	
	/* Test that if students drop, we can now drop capacity */
	@Test
	public void testChangeCapacity_canLowerCapacityAfterDrop1() {
		admin.createClass("Class", 2017, "Instructor", 2);
		
		student.registerForClass("Student1", "Class", 2017);
		student.registerForClass("Student2", "Class", 2017);
		student.dropClass("Student2", "Class", 2017);

		admin.changeCapacity("Class", 2017, 1);
		
		assertEquals(1, admin.getClassCapacity("Class", 2017));
		assertTrue(student.isRegisteredFor("Student1", "Class", 2017));
		assertFalse(student.isRegisteredFor("Student2", "Class", 2017));
	}
	
	/* Test that null's don't do unusual things */
	@Test
	public void testChangeCapacity_nulls() {
		try {
			admin.changeCapacity(null, 2017, 1);
		} catch(Exception e) {
			fail("Raised exception with null!");
		}
	}
}
