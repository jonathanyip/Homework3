package core.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import core.api.CourseManager;
import core.api.impl.Admin;

public class TestCourseManager {

	@Mock
	private Admin admin;
	private CourseManager courseManager;
	
	@Before
	public void setup() {
		this.admin = Mockito.mock(Admin.class);
		this.courseManager = new CourseManager(this.admin);

		Mockito.doReturn(false).when(this.admin).classExists(Mockito.anyString(), Mockito.eq(2016));
		Mockito.doCallRealMethod().when(this.admin).changeCapacity(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());
		
		mockAdvanced();
	}

	public void mockAdvanced() {
		Mockito.doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Object[] arguments = invocation.getArguments();
				if ((int) arguments[1] >= 2017 && (int) arguments[3] > 0) {
					admin.createClass((String) arguments[0], (int) arguments[1], (String) arguments[2], (int) arguments[3]);
				}
				return null;
			}
		}).when(this.admin).createClass(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
	}
	
	@Test
	public void testCourseManagerCreateClass() {
		this.courseManager.createClass("ECS161", 2016);
		assertFalse(this.courseManager.classExists("ECS161", 2016));
	}
}
