package runner;

import org.junit.Test;
import updaters.TestCaseUpdater;
import updaters.VersionUpdater;
import updaters.executionupdaters.ExecutionResultUpdaterByCycle;
import updaters.executionupdaters.ExecutionResultUpdaterByFolder;
import utils.Config;

public class runner {

    @Test
    public void updateTestCases() {
       TestCaseUpdater updateSteps = new TestCaseUpdater();
       updateSteps.updateJiraTestCases(Config.getGherkinFileLocation());
    }

    @Test
    public void updateZephyrVersions() {
        VersionUpdater versionUpdater = new VersionUpdater();
        versionUpdater.updateCycles();
    }

    @Test
    public void updateExecutionResults() {
        ExecutionResultUpdaterByCycle executionResultUpdater = new ExecutionResultUpdaterByCycle();
        executionResultUpdater.updateExecutionResults();

        ExecutionResultUpdaterByFolder executionResultUpdaterByFolder = new ExecutionResultUpdaterByFolder();
        executionResultUpdaterByFolder.updateExecutionResults();

    }



}
