package updaters.executionupdaters;

import com.google.gson.Gson;
import data.Version;
import deserializers.CucumberReportDeserializer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import payloads.UpdateBulkExecutionStatus;
import utils.Config;
import zephyrapi.ZephyrAPI;
import zephyrapiclasses.ListOfCycle;
import zephyrapiclasses.ListOfExecutions;
import zephyrapiclasses.ListOfFolderForCycle;

public class ExecutionResultUpdaterByFolder {

    private ZephyrAPI zephyrAPI;
    private CucumberReportDeserializer cucumberReportDeserializer;
    private Gson gson;

    public ExecutionResultUpdaterByFolder() {
        zephyrAPI = new ZephyrAPI();
        cucumberReportDeserializer = new CucumberReportDeserializer();
        gson = new Gson();
    }

    public void updateExecutionResults() {
        List<Version.Issue> cucumberReportIssues = cucumberReportDeserializer.deserializeCucumberJsonReport();
        updatePassingExecutions(cucumberReportIssues);
        updateFailingExecutions(cucumberReportIssues);
    }

    private void updatePassingExecutions(List<Version.Issue> cucumberReportIssues) {
        String passingPaylod = createPassingExecutionPayload(cucumberReportIssues);
        zephyrAPI.updateBulkExecutionStatus(passingPaylod);
    }

    private void updateFailingExecutions(List<Version.Issue> cucumberReportIssues) {
        String failingPayload = createFailingExecutionPayload(cucumberReportIssues);
        zephyrAPI.updateBulkExecutionStatus(failingPayload);
    }

    private String createPassingExecutionPayload(List<Version.Issue> issues) {
        UpdateBulkExecutionStatus updateBulkExecutionStatus = new UpdateBulkExecutionStatus();
        List<Integer> passingExecutionIds = getListOfPassingExecutions(issues);
        updateBulkExecutionStatus.setExecutions(passingExecutionIds);
        updateBulkExecutionStatus.setStatus(1);
        return gson.toJson(updateBulkExecutionStatus);
    }

    private String createFailingExecutionPayload(List<Version.Issue> issues) {
        UpdateBulkExecutionStatus updateBulkExecutionStatus = new UpdateBulkExecutionStatus();
        List<Integer> failingExecution = getListOfFailingExecutions(issues);
        updateBulkExecutionStatus.setExecutions(failingExecution);
        updateBulkExecutionStatus.setStatus(2);
        return gson.toJson(updateBulkExecutionStatus);
    }

    private List<Integer> getListOfPassingExecutions(List<Version.Issue> issues) {
        List<Integer> passingExecutionIds = new ArrayList<>();
        List<ListOfExecutions> listsOfExecutions = getFolderExecutions();
        listsOfExecutions.forEach(listOfExecution -> {
            passingExecutionIds.addAll(getListOfPassingExecutions(listOfExecution, issues));
        });
        return passingExecutionIds;
    }

    private List<Integer> getListOfFailingExecutions(List<Version.Issue> issues) {
        List<Integer> failingExecutionIds = new ArrayList<>();
        List<ListOfExecutions> listsOfExecutions = getFolderExecutions();
        listsOfExecutions.forEach(listOfExecution -> {
            failingExecutionIds.addAll(getListOfFailingExecutions(listOfExecution, issues));
        });
        return failingExecutionIds;
    }

    private List<Integer> getListOfPassingExecutions(ListOfExecutions listOfExecutions, List<Version.Issue> issues) {
        List<Integer> passingExecutionIds = new ArrayList<>();
        listOfExecutions.getExecutions().forEach(execution -> {
            passingExecutionIds.addAll(getListofPassingExecutions(execution, issues));
        });
        return passingExecutionIds;
    }

    private List<Integer> getListOfFailingExecutions(ListOfExecutions listOfExecutions, List<Version.Issue> issues) {
        List<Integer> failingExecutionIds = new ArrayList<>();
        listOfExecutions.getExecutions().forEach(execution -> {
            failingExecutionIds.addAll(getListofFailingExecutions(execution, issues));
        });
        return failingExecutionIds;
    }

    private List<Integer> getListofPassingExecutions(ListOfExecutions.Execution execution, List<Version.Issue> issues) {
        List<Integer> passingExecutionIds = new ArrayList<>();
        issues.forEach(issue -> {
            if (issue.getIssueId() == execution.getIssueId() & issue.getStatus().equals("passed")) {
                passingExecutionIds.add(execution.getId());
            }
        });
        return passingExecutionIds;
    }

    private List<Integer> getListofFailingExecutions(ListOfExecutions.Execution execution, List<Version.Issue> issues) {
        List<Integer> failingExecutionIds = new ArrayList<>();
        issues.forEach(issue -> {
            if (issue.getIssueId() == execution.getIssueId() & issue.getStatus().equals("failed")) {
                failingExecutionIds.add(execution.getId());
            }
        });
        return failingExecutionIds;
    }


    private List<ListOfExecutions> getFolderExecutions() {
        List<ListOfFolderForCycle> listOfFolderForCycles = getListOfFolderForCycle();
        List<ListOfExecutions> listOfExecutions = getExecutionsForFolder(listOfFolderForCycles);
        return listOfExecutions;
    }

    private List<ListOfExecutions> getExecutionsForFolder(List<ListOfFolderForCycle> listOfFolderForCycles) {
        List<ListOfExecutions> listsOfExecutions = new ArrayList<>();
        listOfFolderForCycles.forEach(folder -> {
            listsOfExecutions.add(getExecutionsForFolder(folder));
        });
        return listsOfExecutions;
    }

    private ListOfExecutions getExecutionsForFolder(ListOfFolderForCycle listOfFolderForCycle) {
        return zephyrAPI.getListOfExecutionsByFolderId(listOfFolderForCycle.getCycleId(), listOfFolderForCycle.getFolderId());
    }

    private List<ListOfFolderForCycle> getListOfFolderForCycle() {
        List<ListOfFolderForCycle> listOfFoldersForCycles = new ArrayList<>();
        List<Integer> cycleIds = getCycleIds();
        cycleIds.forEach(cycleId -> {
            List<ListOfFolderForCycle> folders = Arrays.asList(getListOfFolderForCycle(cycleId));
            listOfFoldersForCycles.addAll(folders);
        });
        return listOfFoldersForCycles;
    }


    private ListOfFolderForCycle[] getListOfFolderForCycle(int cycleId) {
        String deviceVersionName = String.format("%s %s", Config.getPORTAL(), Config.getDevice());
        int deviceVersionId = zephyrAPI.getZephyrVersionId(deviceVersionName);
        return zephyrAPI.getListOfFolderForCycle(cycleId, deviceVersionId);
    }

    private List<Integer> getCycleIds() {
        ListOfCycle listOfCycle = getListOfCycles();
        List<Integer> cycleIds = new ArrayList<>();
        listOfCycle.getCycle().forEach(cycle -> {
            if (cycle.getCycleId() != -1)
                cycleIds.add(cycle.getCycleId());
        });
        return cycleIds;
    }

    private ListOfCycle getListOfCycles() {
        String deviceVersionName = String.format("%s %s", Config.getPORTAL(), Config.getDevice());
        int deviceVersionId = zephyrAPI.getZephyrVersionId(deviceVersionName);
        return zephyrAPI.getListOfCycle(deviceVersionId);
    }


}
