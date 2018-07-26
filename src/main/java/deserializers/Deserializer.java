package deserializers;


import data.CucumberReport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import payloads.UpdateJiraIssue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import java.util.ArrayList;
import java.util.List;
import utils.Config;
import zephyrapiclasses.AllVersions;
import zephyrapiclasses.ListOfCycle;
import zephyrapiclasses.ListOfExecutions;
import zephyrapiclasses.ListOfFolderForCycle;
import zephyrapiclasses.ListOfTestStep;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;

public class Deserializer {
    private Gson gson;

    public Deserializer() {
        gson = new Gson();
    }

    public CucumberReport[] deserializeCucumberReport() {
        File initialFile = new File(Config.getCucumberJsonReportLocation());
        InputStream file = null;
        try {
            file = new FileInputStream(initialFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //	InputStream file = getClass().getResourceAsStream(Config.getCucumberJsonReportLocation());
        BufferedReader reader = new BufferedReader(new InputStreamReader(file));
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(reader).getAsJsonArray();
        return gson.fromJson(jsonElement, CucumberReport[].class);
    }

    public ListOfTestStep[] deserializeListOfTestStep(String json) {
        return gson.fromJson(getJsonElementAsArray(json), ListOfTestStep[].class);
    }

    public AllVersions deserializeListOfVersions(String json) {
        return gson.fromJson(getJsonElementAsObject(json), AllVersions.class);
    }

    public ListOfCycle deserializeListOfCycle(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ListOfCycle.class, new ListOfCycleDeserializer());
        Gson gson = builder.setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES).create();
        return gson.fromJson(json, ListOfCycle.class);
    }

    public ListOfExecutions deserializeListOfExecutions(String json){
        return gson.fromJson(getJsonElementAsObject(json), ListOfExecutions.class);

    }

    public ListOfFolderForCycle[] deserializerListOfFolderForCycle(String json){
        return gson.fromJson(getJsonElementAsArray(json), ListOfFolderForCycle[].class);

    }

    private JsonElement getJsonElementAsArray(String json) {
        JsonParser parser = new JsonParser();
        return parser.parse(json).getAsJsonArray();
    }

    private JsonElement getJsonElementAsObject(String json) {
        try {
            JsonParser parser = new JsonParser();
            return parser.parse(json).getAsJsonObject();
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("cannot convert to Json" + json);
        }
    }

    public String createUpdateJiraIssuePayload(String scenarioName){
        UpdateJiraIssue updateJiraIssue = new UpdateJiraIssue();
        UpdateJiraIssue.Summary summary = updateJiraIssue.new Summary();
        summary.setSet(scenarioName);
        List<UpdateJiraIssue.Summary> summaries = new ArrayList<>();
        summaries.add(summary);
        UpdateJiraIssue.Update update = updateJiraIssue.new Update();
        update.setSummary(summaries);
        updateJiraIssue.setUpdate(update);
        updateJiraIssue.getUpdate().setSummary(summaries);

        return gson.toJson(updateJiraIssue);

    }
}
