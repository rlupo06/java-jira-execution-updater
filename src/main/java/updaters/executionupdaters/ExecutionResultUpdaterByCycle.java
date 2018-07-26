package updaters.executionupdaters;

import com.google.gson.Gson;
import data.Version;
import deserializers.CucumberReportDeserializer;
import java.util.ArrayList;
import java.util.List;
import payloads.UpdateBulkExecutionStatus;
import utils.Config;
import zephyrapi.ZephyrAPI;
import zephyrapiclasses.ListOfCycle;
import zephyrapiclasses.ListOfExecutions;

public class ExecutionResultUpdaterByCycle {

    private ZephyrAPI zephyrAPI;
    private CucumberReportDeserializer cucumberReportDeserializer;
    private Gson gson;

    public ExecutionResultUpdaterByCycle(){
        zephyrAPI = new ZephyrAPI();
        cucumberReportDeserializer = new CucumberReportDeserializer();
        gson = new Gson();
    }

    public void updateExecutionResults(){
        List<Version.Issue> cucumberReportIssues = cucumberReportDeserializer.deserializeCucumberJsonReport();
        updatePassingExecutions(cucumberReportIssues);
        updateFailingExecutions(cucumberReportIssues);
    }

    private void updatePassingExecutions(List<Version.Issue> cucumberReportIssues){
        String passingPaylod = createPassingExecutionPayload(cucumberReportIssues);
        zephyrAPI.updateBulkExecutionStatus(passingPaylod);
    }

    private void updateFailingExecutions(List<Version.Issue> cucumberReportIssues){
        String failingPayload = createFailingExecutionPayload(cucumberReportIssues);
        zephyrAPI.updateBulkExecutionStatus(failingPayload);
    }

    private String createPassingExecutionPayload(List<Version.Issue> issues){
        UpdateBulkExecutionStatus updateBulkExecutionStatus = new UpdateBulkExecutionStatus();
        List<Integer> passingExecutionIds = getListOfPassingExecutions(updateBulkExecutionStatus, issues);
        updateBulkExecutionStatus.setExecutions(passingExecutionIds);
        updateBulkExecutionStatus.setStatus(1);
        return gson.toJson(updateBulkExecutionStatus);
    }

    public String createFailingExecutionPayload(List<Version.Issue> issues){
        UpdateBulkExecutionStatus updateBulkExecutionStatus = new UpdateBulkExecutionStatus();
        List<Integer> failingExecution = getListOfFailingExecutions(updateBulkExecutionStatus, issues);
        updateBulkExecutionStatus.setExecutions(failingExecution);
        updateBulkExecutionStatus.setStatus(2);
        return gson.toJson(updateBulkExecutionStatus);
    }

    private List<Integer> getListOfPassingExecutions(UpdateBulkExecutionStatus updateBulkExecutionStatus, List<Version.Issue> issues){
        List<Integer> passingExecutions = new ArrayList<>();
        List<Integer> cycleIds = getCycleIds();
        cycleIds.forEach(cycleId->{
            ListOfExecutions listOfExecutions = zephyrAPI.getListOfExecutionsByCycleId(cycleId);
            passingExecutions.addAll(getPassingExecutionIds(updateBulkExecutionStatus, listOfExecutions, issues));
        });
        return passingExecutions;

    }


    private List<Integer> getListOfFailingExecutions(UpdateBulkExecutionStatus updateBulkExecutionStatus, List<Version.Issue> issues){
        List<Integer> failingExecutionIds = new ArrayList<>();
        List<Integer> cycleIds = getCycleIds();
        cycleIds.forEach(cycleId->{
            ListOfExecutions listOfExecutions = zephyrAPI.getListOfExecutionsByCycleId(cycleId);
            failingExecutionIds.addAll(getFailedExecutionIds(listOfExecutions, issues));
        });
        return failingExecutionIds;

    }

    private List<Integer> getPassingExecutionIds(UpdateBulkExecutionStatus updateBulkExecutionStatus, ListOfExecutions listOfExecutions, List<Version.Issue> issues){
        List<Integer> passingExecutions = new ArrayList<>();
        List<ListOfExecutions.Execution> executions =  listOfExecutions.getExecutions();
        executions.forEach(execution -> {
            passingExecutions.addAll(getPassingExecutionsIds(execution, issues));
        });
        return passingExecutions;
    }

    private List<Integer> getFailedExecutionIds(ListOfExecutions listOfExecutions, List<Version.Issue> issues){
        List<Integer> failingExecutionsIds = new ArrayList<>();
        List<ListOfExecutions.Execution> executions =  listOfExecutions.getExecutions();
        executions.forEach(execution -> {
            failingExecutionsIds.addAll(getFailedExecutionIds(execution, issues));
        });
        return failingExecutionsIds;
    }

    private List<Integer> getFailedExecutionIds(ListOfExecutions.Execution execution, List<Version.Issue> issues){
        List<Integer> failExecutions = new ArrayList<>();
        issues.forEach(issue -> {
            if(execution.getIssueId()==issue.getIssueId()&issue.getStatus().equals("failed")){
                failExecutions.add(execution.getId());
            }
        });
        return failExecutions;
    }

    private List<Integer> getPassingExecutionsIds(ListOfExecutions.Execution execution, List<Version.Issue> issues){
        List<Integer> passingExecutions = new ArrayList<>();
        issues.forEach(issue -> {
            if(execution.getIssueId()==issue.getIssueId()&issue.getStatus().equals("passed")){
                passingExecutions.add(execution.getId());
            }
        });
        return passingExecutions;
    }

    private List<Integer> getCycleIds(){
        ListOfCycle listOfCycle = getListOfCycles();
        List<Integer> cycleIds = new ArrayList<>();
                listOfCycle.getCycle().forEach(cycle-> {
                    if(cycle.getCycleId()!=-1)
                    cycleIds.add(cycle.getCycleId());
        });
                return cycleIds;
    }

    private ListOfCycle getListOfCycles(){
        String deviceVersionName = String.format("%s %s", Config.getPORTAL(), Config.getDevice());
        int deviceVersionId = zephyrAPI.getZephyrVersionId(deviceVersionName);
        return zephyrAPI.getListOfCycle(deviceVersionId);
    }


}
