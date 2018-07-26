package updaters;

import gherkin.ast.GherkinDocument;
import gherkin.pickles.Pickle;
import gherkin.pickles.PickleStep;
import java.util.List;
import utils.Utilities;
import zephyrapi.ZephyrAPI;
import zephyrapiclasses.ListOfTestStep;

public class TestCaseUpdater {
    private ZephyrAPI zephyrApi;


    public TestCaseUpdater() {
        zephyrApi = new ZephyrAPI();
    }

    public void updateJiraTestCases(String pathToFeatureFiles) {
        List<GherkinDocument> gherkinDocuments = Utilities.getAllGerkinDocuments(pathToFeatureFiles);
        updateJiraTestCases(gherkinDocuments);
    }

    private void updateJiraTestCases(List<GherkinDocument> gherkinDocuments) {
        gherkinDocuments.forEach(gherkinDocument -> {
            List<Pickle> pickles = Utilities.parseGerkinDocument(gherkinDocument);
            deleteAllJiraSteps(pickles);
            addStepsToJiraIssues(pickles);
        });
    }

    private void updateJiraIssueSummary(Pickle pickle) {
        int jiraIssueId = Utilities.extractJiraIssueId(pickle);
        String scenarioName = pickle.getName();
        zephyrApi.updateJiraIssue(jiraIssueId, scenarioName);
    }

    private void addStepsToJiraIssues(List<Pickle> pickles) {
        pickles.forEach(this::addStepsToJiraIssue);
    }

    private void addStepsToJiraIssue(Pickle pickle) {
        int jiraIssue = Utilities.extractJiraIssueId(pickle);
        pickle.getSteps().forEach(pickleStep -> {
            addStepToJiraIssue(jiraIssue, pickleStep);
            updateJiraIssueSummary(pickle);
        });
    }

    private void addStepToJiraIssue(int jiraIssueId, PickleStep pickleStep) {
        zephyrApi.createNewTestStep(jiraIssueId, pickleStep.getText());
    }

    private void deleteAllJiraSteps(List<Pickle> pickles) {
        pickles.forEach(this::deleteJiraIssueSteps);
    }

    private void deleteJiraIssueSteps(Pickle pickle) {
        int jiraIssueId = Utilities.extractJiraIssueId(pickle);
        deleteJiraIssueSteps(jiraIssueId);
    }

    private void deleteJiraIssueSteps(int jiraIssueId) {
        ListOfTestStep[] zephyrTestSteps = zephyrApi.getListOfTestSteps(jiraIssueId);
        for (ListOfTestStep zephyrTestStep : zephyrTestSteps) {
            zephyrApi.deleteTestStep(jiraIssueId, zephyrTestStep.getId());
        }
    }
}
