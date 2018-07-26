package updaters.executionupdaters;

import com.google.gson.Gson;
import data.Version;
import deserializers.CucumberReportDeserializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import payloads.UpdateBulkExecutionStatus;
import utils.Config;
import zephyrapi.ZephyrAPI;
import zephyrapiclasses.ListOfCycle;
import zephyrapiclasses.ListOfExecutions;

public class ExecutionResultUpdater {

    private ZephyrAPI zephyrAPI;
    private CucumberReportDeserializer cucumberReportDeserializer;
    private Gson gson;

    public ExecutionResultUpdater() {
        zephyrAPI = new ZephyrAPI();
        cucumberReportDeserializer = new CucumberReportDeserializer();
        gson = new Gson();
    }

    public void updateExecutionResults(List<Version.Issue> issues) {
        Map<String, List<Version.Issue>> issuesWithStatus = getIssuesWithStatusByCycleId(issues);
        updatePassedExecutions(issuesWithStatus);
        updateFailingExecutions(issuesWithStatus);
    }

    private void updatePassedExecutions(Map<String, List<Version.Issue>> issuesWithStatus) {
        String passedExecutionPayload = createPassedExecutionPayload(issuesWithStatus);
        zephyrAPI.updateBulkExecutionStatus(passedExecutionPayload);
    }

    private void updateFailingExecutions(Map<String, List<Version.Issue>> issuesWithStatus) {
        String failedExecutionPayload = createFailedExecutionPayload(issuesWithStatus);
        zephyrAPI.updateBulkExecutionStatus(failedExecutionPayload);
    }

    private String createFailedExecutionPayload(Map<String, List<Version.Issue>> issuesWithStatus) {
        List<Integer> failingExecutions = getFailingExecutionIds(issuesWithStatus);
        return createExecutionPayload(failingExecutions, 0);
    }

    private String createPassedExecutionPayload(Map<String, List<Version.Issue>> issuesWithStatus) {
        List<Integer> passingExecutions = getPassingExecutionIds(issuesWithStatus);
        return createExecutionPayload(passingExecutions, 1);
    }

    private String createExecutionPayload(List<Integer> executionIds, int status) {
        UpdateBulkExecutionStatus updateBulkExecutionStatus = new UpdateBulkExecutionStatus();
        updateBulkExecutionStatus.setExecutions(executionIds);
        updateBulkExecutionStatus.setStatus(status);
        return gson.toJson(updateBulkExecutionStatus);

    }

    private List<Integer> getFailingExecutionIds(Map<String, List<Version.Issue>> issuesWithStatus) {
        return issuesWithStatus.get("failed").stream().map(issue -> issue.getExecutionId()).collect(Collectors.toList());
    }

    private List<Integer> getPassingExecutionIds(Map<String, List<Version.Issue>> issuesWithStatus) {
        return issuesWithStatus.get("passed").stream().map(issue -> issue.getExecutionId()).collect(Collectors.toList());
    }






















    //Everything below is for getting by cycleID

    private Map<String, List<Version.Issue>> getIssuesWithStatusByCycleId(List<Version.Issue> issues) {
        Map<String, List<Version.Issue>> executionResults = new HashMap<>();
        List<Integer> cycleIds = getCycleIds();
        cycleIds.forEach(cycleId -> {
            ListOfExecutions listOfExecutions = zephyrAPI.getListOfExecutionsByCycleId(cycleId);
            executionResults.putAll(getIssuesWithStatus(listOfExecutions, issues));
        });
        return executionResults;
    }

    private Map<String, List<Version.Issue>> getIssuesWithStatus(ListOfExecutions listOfExecutions, List<Version.Issue> issues) {
        Map<String, List<Version.Issue>> issuesWithStatus = new HashMap<>();
        List<ListOfExecutions.Execution> executions = listOfExecutions.getExecutions();
        executions.forEach(execution -> {
            issuesWithStatus.putAll(getIssuesWithStatus(execution, issues));
        });
        return issuesWithStatus;

    }

    private Map<String, List<Version.Issue>> getIssuesWithStatus(ListOfExecutions.Execution execution, List<Version.Issue> issues) {
        Map<String, List<Version.Issue>> issuesWithStatus = new HashMap<>();
        List<Version.Issue> passingIssues = getIssuesWithStatus(execution, issues, "passed");
        List<Version.Issue> failingIssues = getIssuesWithStatus(execution, issues, "failed");

        issuesWithStatus.put("passed", passingIssues);
        issuesWithStatus.put("failed", failingIssues);
        return issuesWithStatus;
    }

    private List<Version.Issue> getIssuesWithStatus(ListOfExecutions.Execution execution, List<Version.Issue> issues, String status) {
        List<Version.Issue> issuesWithStatus = issues.stream().filter(issue -> issue.getIssueId() == execution.getIssueId() & issue.getStatus().equals(status)).collect(Collectors.toList());
        issuesWithStatus.forEach(issue -> issue.setIssueId(execution.getId()));
        return issuesWithStatus;
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
