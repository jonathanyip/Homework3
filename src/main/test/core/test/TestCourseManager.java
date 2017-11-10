package core.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Mockito;
import org.mockito.Spy;

import core.api.ICourseManager;
import core.api.impl.Admin;
import core.api.impl.CourseManager;

/**
 * Tests course manager. Since the Admin implementation is known to be bugging. 
 * 
 * @author Vincent
 *
 */
public class TestCourseManager {

	@Spy
	private Admin admin;
	private ICourseManager courseManager;
	
	@Before
	public void setup() {
		this.admin = Mockito.spy(new Admin());
		this.courseManager = new CourseManager(this.admin);
		setupMocking();
	}

	/*
	 * Shows some initial set-up for the mocking of Admin.
	 * This includes fixing a known bug (year in past is not correctly checked) in the Admin class by Mocking its behavior.
	 * Not all fixes to Admin can be made from here, so for the more complex constraints you can simply Mock the
	 * specific calls to Admin's createClass to yield the correct behavior in the unit test itself.
	 */
	public void setupMocking() {
		Mockito.doNothing().when(this.admin).createClass(Mockito.anyString(), AdditionalMatchers.lt(2017), Mockito.anyString(), Mockito.anyInt());
		Mockito.doNothing().when(this.admin).createClass(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), AdditionalMatchers.leq(0));
	}

	@Test
	public void testCreateClassCorrect() {
		this.courseManager.createClass("ECS161", 2017, "Instructor", 1);
		assertTrue(this.courseManager.classExists("ECS161", 2017));
	}
	
	@Test
	public void testCreateClassInPast() {
		this.courseManager.createClass("ECS161", 2016, "Instructor", 1);
		assertFalse(this.courseManager.classExists("ECS161", 2016));
	}

	@Test
	public void testCreateClassInFuture() {
		this.courseManager.createClass("ECS161", 2018, "Instructor", 1);
		Mockito.verify(this.admin, Mockito.never()).createClass(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
	}
	
	@Test
	public void testCreateClass_capacityCorrect() {
		this.courseManager.createClass("ECS161", 2017, "Instructor", 1);
		assertTrue(this.courseManager.classExists("ECS161", 2017));
	}
	
	@Test
	public void testCreateClass_capacityZero() {
		this.courseManager.createClass("ECS161", 2017, "Instructor", 0);
		assertFalse(this.courseManager.classExists("ECS161", 2017));
	}
	
	@Test
	public void testCreateClass_capacityNegative() {
		this.courseManager.createClass("ECS161", 2017, "Instructor", -1);
		assertFalse(this.courseManager.classExists("ECS161", 2017));
	}
	
	@Test
	public void testCreateClass_capacityTooLarge() {
		this.courseManager.createClass("ECS161", 2017, "Instructor", 1000);
		Mockito.verify(this.admin, Mockito.never()).createClass(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
	}
	
	@Test
	public void testCreateClass_capacityNearTooLarge() {
		this.courseManager.createClass("ECS161", 2017, "Instructor", 999);
		assertTrue(this.courseManager.classExists("ECS161", 2017));
	}
	
	@Test
	public void testCreateClass_noMoreTwoClassPerInstructorCorrect() {
		Mockito.doNothing().when(this.admin).createClass("ECS189", 2017, "Instructor", 1);
		
		this.courseManager.createClass("ECS161", 2017, "Instructor", 1);
		this.courseManager.createClass("ECS160", 2017, "Instructor", 1);
		this.courseManager.createClass("ECS189", 2017, "Instructor", 1);
		
		assertTrue(this.courseManager.classExists("ECS161", 2017));
		assertTrue(this.courseManager.classExists("ECS160", 2017));
		assertFalse(this.courseManager.classExists("ECS189", 2017));
	}
	
	@Test
	public void testCreateClass_noTwoClassesWithSameNameYear() {
		Mockito.doNothing().when(this.admin).createClass("ECS161", 2017, "Instructor2", 1);
		
		this.courseManager.createClass("ECS161", 2017, "Instructor1", 1);
		this.courseManager.createClass("ECS161", 2017, "Instructor2", 1);
		
		assertTrue(this.courseManager.classExists("ECS161", 2017));
		assertEquals("Instructor1", this.courseManager.getClassInstructor("ECS161", 2017));
	}
}
