package com.neaterbits.compiler.main;

public class ForStatementTestClass {

	public void testMethod1() {
		
		for (int i = 0; i < 100; ++ i) {
			
		}
	}

	public void testMethod2() {
		
		for (int i = 0; i < 100;) {
			
		}
	}

	public void testMethod3() {
		
		for (int i = 0;;) {
			
		}
	}

	public void testMethod4() {
		
		int i = 0;
		for (; i < 100;) {
		}
	}

	public void testMethod5() {
		
		int i = 0;
		for (; i < 100;) {
			
		}
	}
}
