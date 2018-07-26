package zephyrapi;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deserializers.Deserializer;
import java.util.NoSuchElementException;
import utils.ApiCalls;
import utils.Config;
import zephyrapiclasses.AllVersions;
import zephyrapiclasses.ListOfCycle;
import zephyrapiclasses.ListOfExecutions;
import zephyrapiclasses.ListOfFolderForCycle;
import zephyrapiclasses.ListOfTestStep;

public class ZephyrAPI {

    private static final String BASE_URI = "https://jira.avirat.net/rest/zapi/latest/";
    private static final String EXECUTION_URI = BASE_URI + "execution/";
    private static final String UPDATE_BULK_EXECUTION_STATUS_URI = EXECUTION_URI + "updateBulkStatus";
    private static final String CYCLE_URI = BASE_URI + "cycle/";
    private static final String GET_LIST_OF_EXECUTIONS = BASE_URI + "execution";
    private static final String EXECUTION_LIST_URI = BASE_URI + "execution?cycleId=";
    private static final String VERSION_LIST_URI =
            BASE_URI + "util/versionBoard-list?projectId=10304";
    private static final String STEPS_URI = BASE_URI + "teststep/";
    private static final String STEP_RESULT_URI = BASE_URI + "stepResult/";
    private static final String JIRA_ISSUE_URI = "https://jira.avirat.net/rest/api/2/issue/";
    private static final String ATTACHMENT_URI = BASE_URI + "attachment";

    private ApiCalls apiCall;
    private Deserializer deserializer;


    public ZephyrAPI() {
        apiCall = new ApiCalls();
        deserializer = new Deserializer();
    }

    public void updateBulkExecutionStatus(String payload) {
        JsonParser parser = new JsonParser();
        JsonObject jsonObjectPayload = parser.parse(payload).getAsJsonObject();
        if (jsonObjectPayload.get("executions").getAsJsonArray().size() > 0) {
            apiCall.putCall(UPDATE_BULK_EXECUTION_STATUS_URI, payload);
        }
    }

    public String updateJiraIssue(int issueId, String scenarioName) {
        String updateJiraIssueUri = JIRA_ISSUE_URI + issueId;
        String payload = deserializer.createUpdateJiraIssuePayload(scenarioName);
        return apiCall.putCall(updateJiraIssueUri, payload);
    }

    public ListOfTestStep[] getListOfTestSteps(int issueId) {
        String listOfTestStepsUri = STEPS_URI + issueId;
        return deserializer.deserializeListOfTestStep(apiCall.getCall(listOfTestStepsUri));

    }

    public void deleteTestStep(int issueId, int stepId) {
        String deleteTestStepUri = STEPS_URI + issueId + "/" + stepId;
        apiCall.deleteCall(deleteTestStepUri);
    }

    public void createNewTestStep(int issueId, String stepName) {
        JsonObject payload = new JsonObject();
        payload.addProperty("step", stepName);
        apiCall.postCall(STEPS_URI + issueId, payload.toString());
    }

    public int getZephyrVersionId(String versionName) {
        try {
            return getAllVerisons()
                    .getUnreleasedVersions()
                    .stream()
                    .filter(
                            unreleasedVersions ->
                                    unreleasedVersions.getLabel().equals(String.format("%s", versionName)))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new)
                    .getValue();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(String.format("Could Not find Version: %s", versionName));
        }
    }

    private AllVersions getAllVerisons() {
        return deserializer.deserializeListOfVersions(apiCall.getCall(VERSION_LIST_URI));
    }

    public ListOfCycle getListOfCycle(int versionId) {
        String cycleListUri = CYCLE_URI + "?versionId=" + versionId;
        return deserializer.deserializeListOfCycle(apiCall.getCall(cycleListUri));

    }

    public ListOfFolderForCycle[] getListOfFolderForCycle(int cycleId, int versionId) {
        String getListOfFolderForCycleUri = String.format("%s%s%s%s%s%s", CYCLE_URI, cycleId, "/folders?projectId=", Config.getProjectId(), "&versionId=", versionId);
        return deserializer.deserializerListOfFolderForCycle(apiCall.getCall(getListOfFolderForCycleUri));
    }

    public ListOfExecutions getListOfExecutionsByCycleId(int cycleId) {
        String listOfExecutionsByCycleIdUri = String.format("%s%s%s", GET_LIST_OF_EXECUTIONS, "?cycleId=", cycleId);
        return deserializer.deserializeListOfExecutions(apiCall.getCall(listOfExecutionsByCycleIdUri));

    }

    public ListOfExecutions getListOfExecutionsByFolderId(int cycleId, int folderId) {
        String listOfExecutionsByCycleIdUri = String.format("%s%s%s%s%s", GET_LIST_OF_EXECUTIONS, "?cycleId=", cycleId, "&folderId=", folderId);
        return deserializer.deserializeListOfExecutions(apiCall.getCall(listOfExecutionsByCycleIdUri));

    }

    public void deleteTestCycles(ListOfCycle cycles) {
        cycles.getCycle().forEach(
                (cycle) -> {
                    deleteCycle(cycle.getCycleId());
                });
    }

    private void deleteCycle(int cycleId) {
        if (cycleId != -1) {
            String cycleDeleteUri = CYCLE_URI + cycleId;
            apiCall.deleteCall(cycleDeleteUri);
        }
    }

    public void cloneCycle(ListOfCycle.Cycle cycle, int versionId) {
        if (cycle.getCycleId() != -1) {
            JsonObject payload = new JsonObject();
            payload.addProperty("clonedCycleId", cycle.getCycleId());
            payload.addProperty("name", cycle.getName());
            payload.addProperty("projectId", Config.getProjectId());
            payload.addProperty("versionId", versionId);

            apiCall.postCall(CYCLE_URI, payload.toString());
        }
    }
}
