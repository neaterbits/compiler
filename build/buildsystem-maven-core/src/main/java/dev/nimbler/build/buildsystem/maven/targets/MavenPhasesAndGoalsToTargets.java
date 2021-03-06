package dev.nimbler.build.buildsystem.maven.targets;

import java.util.ArrayList;
import java.util.List;

import org.jutils.StringUtils;
import org.jutils.concurrency.dependencyresolution.spec.TargetBuilderSpec;

import dev.nimbler.build.buildsystem.maven.phases.Phases;
import dev.nimbler.build.buildsystem.maven.phases.UnknownPhaseException;
import dev.nimbler.build.types.Build;

public class MavenPhasesAndGoalsToTargets {

    public static List<Build<MavenBuilderContext>> makeTargetBuilders(String [] args) throws UnknownPhaseException, UnknownGoalOrPhaseException {

        final List<Build<MavenBuilderContext>> builds
            = new ArrayList<>(args.length);
        
        for (String arg : args) {

            // Is this a phase or a goal ?
            final String [] parts = StringUtils.split(arg, ':');

            final TargetBuilderSpec<MavenBuilderContext> targetBuilder;
            
            final String targetName;
            
            if (parts.length == 1) {

                final String phaseName = parts[0];
                
                // Phases in order of execution
                final Phases applicablePhases = Phases.getApplicablePhases(phaseName);
                
                targetBuilder = new PhaseTargetBuilder(applicablePhases);
                
                targetName = phaseName;
            }
            else if (parts.length == 2) {
                
                final GoalTargetBuilder goalTargetBuilder = new GoalTargetBuilder(parts[0], parts[1]);
                
                targetBuilder = goalTargetBuilder;
                
                targetName = goalTargetBuilder.getTargetName(); 
            }
            else {
                throw new UnknownGoalOrPhaseException("No such goal or phase", arg);
            }

            builds.add(new Build<>(targetBuilder, targetName));
        }
        
        return builds;
    }
}
