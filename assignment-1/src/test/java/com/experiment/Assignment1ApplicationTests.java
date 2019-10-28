package com.experiment;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Assignment1ApplicationTests {

	@Test
	void contextLoads() {
	}
	
	public void testApplication() {
		
		AssignmentController assignmentController=new AssignmentController();
		
		assignmentController.createFile(null);
		
		
	
		
		
	}

}
