package dev.nimbler.build.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import javax.xml.bind.JAXBException;

import org.jutils.concurrency.dependencyresolution.executor.TargetBuildResult;
import org.jutils.concurrency.dependencyresolution.executor.logger.BinaryTargetExecutorLogger;
import org.jutils.concurrency.dependencyresolution.executor.logger.DelegatingTargetExecutorLogger;
import org.jutils.concurrency.dependencyresolution.executor.logger.StructuredTargetExecutorLogger;
import org.jutils.concurrency.dependencyresolution.executor.logger.TargetExecutorLogger;

import org.jutils.structuredlog.binary.logging.LogContext;
import org.jutils.structuredlog.xml.model.Log;
import org.jutils.structuredlog.xml.model.LogIO;

import dev.nimbler.build.buildsystem.common.ArgumentException;
import dev.nimbler.build.buildsystem.common.BuildSystemMain;
import dev.nimbler.build.buildsystem.common.ScanException;

public class BuildMain {

	private static void usage() {
		System.err.println("usage: build [... build system specific arguments ...]");
	}
	
	public static void main(String [] args) throws ScanException {

	    final AllBuildSystems buildSystems = new AllBuildSystems(); 
	    
	    boolean buildPerformed = false;

        final StructuredTargetExecutorLogger structuredLogger = new StructuredTargetExecutorLogger(target -> new ArrayList<>());

        final Function<LogContext, TargetExecutorLogger> getLogger = logContext -> {

	        // final TargetExecutorLogger logger = new PrintlnTargetExecutorLogger();
	        final BinaryTargetExecutorLogger binaryTargetExecutorLogger = new BinaryTargetExecutorLogger(logContext);
	        
	        final DelegatingTargetExecutorLogger delegatingLogger = new DelegatingTargetExecutorLogger(
	                structuredLogger,
	                binaryTargetExecutorLogger);

	        return delegatingLogger; // new PrintlnTargetExecutorLogger();
	    };
	    
	    try {
    		if (args.length >= 1) {
    		    
    		    // First argument may be project dir
    		    buildPerformed = BuildSystemMain.performBuild(
    		            buildSystems,
    		            args[0],
    		            Arrays.copyOfRange(args, 1, args.length),
                        getLogger,
                        (TargetBuildResult result, LogContext lc) -> processResult(result, structuredLogger, lc));
    		}
    		
    		if (!buildPerformed) {
    	        
    	        final String userDir = System.getProperty("user.dir");

    	        if (userDir != null) {
    
    	            buildPerformed = BuildSystemMain.performBuild(
    	                    buildSystems,
    	                    userDir,
    	                    args,
    	                    getLogger,
    	                    (TargetBuildResult result, LogContext lc) -> processResult(result, structuredLogger, lc));
    	        }
    		}
    		
    		if (!buildPerformed) {
    		    usage();
    		}
	    }
	    catch (ArgumentException ex) {
	        usage();
	    }
	}
	
    private static void processResult(
            TargetBuildResult result,
            StructuredTargetExecutorLogger structuredLogger,
            LogContext logContext) {

        final Log log = structuredLogger.makeLog();
        
        final LogIO logIO = new LogIO();
        
        try (FileOutputStream outputStream = new FileOutputStream(new File("buildlog.xml"))) {
            logIO.writeLog(log, outputStream);
        } catch (IOException | JAXBException ex) {
            throw new IllegalStateException(ex);
        }

        try (FileOutputStream outputStream = new FileOutputStream(new File("binarylog"))) {
            logContext.writeLogBufferToOutput(outputStream);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
