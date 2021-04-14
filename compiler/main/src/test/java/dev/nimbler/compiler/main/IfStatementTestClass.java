package dev.nimbler.compiler.main;

public class IfStatementTestClass {

	int testVariable;
	
	public void testMethod1() {
		if (1 == testVariable) {
			//testVariable = 1;
		}
		else if (2 == testVariable) {
			//testVariable = 2;
			
		}
		else if (3 == testVariable) {
			//testVariable = 3;
		}
		else {
			testVariable = 4;
		}
	}

	public void testMethod2() {
		if (1 == testVariable) {
			
		}
		else if (2 == testVariable) {
			
		}
		else if (3 == testVariable) {
			testVariable = 234;
		}
		else {
			testVariable = 123;
		}
		
		someStatement();
	}

	public void testMethod3() {
		if (1 == testVariable) {
			
		}
		else if (2 == testVariable) {
			
		}
		else if (3 == testVariable) {
			
		}
	}

	public void testMethod4() {
		if (1 == testVariable) {
			
		}
		else if (2 == testVariable) {
			
		}
		else if (3 == testVariable) {
			
		}
		
		someStatement();
	}

	public void testMethod5() {
		if (1 == testVariable) {
			testVariable = 1;
		}
		else if (2 == testVariable) {
			
		}
		else if (3 == testVariable) {
			
		}
	}

	private void someStatement() {
		
	}
}
