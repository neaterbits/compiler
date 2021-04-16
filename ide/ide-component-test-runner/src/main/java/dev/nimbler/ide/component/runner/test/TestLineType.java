package dev.nimbler.ide.component.runner.test;

public enum TestLineType {

    START, // Start of test
    END, // End of test
    
    MODEL, // Test model
    
    METHOD, // Start of test method
    
    EXCEPTION(TestStatus.EXCEPTION), // exception in runtime framework
    ERROR(TestStatus.ERROR), // Exception in test
    FAILURE(TestStatus.FAILURE), // Test failure
    SUCCESS(TestStatus.SUCCESS);
    
    private final TestStatus status;

    private TestLineType() {
        this(null);
    }

    private TestLineType(TestStatus status) {
        this.status = status;
    }

    public TestStatus getStatus() {
        return status;
    }
}
