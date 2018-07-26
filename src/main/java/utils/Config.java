package utils;

import java.util.Arrays;
import java.util.List;

public class Config {

    // File Locations
    public static final String CUCUMBER_JSON_REPORT_LOCATION =  System.getProperty("cucumberReportLocation", "target/cucumber-report/cucumber.json");
    private static final String GHERKIN_FILE_LOCATION = System.getProperty("gherkinFileLocation", "");
    // System Properties
    private static final String DEVICES = System.getProperty("devices", "iPhone6s,iPhone7,iPadMini4,iPad");
    private static final String DEVICE = System.getProperty("device", "414px");
    private static final String VERSION_TO_COPY_FROM = System.getProperty("versionToCopy", "QA iOS Mobilev2");
    private static final String SCREENSHOT_LOCATION = "target/";
    private static final int PROJECT_ID = 10304;
    private static final String JIRA_USERNAME = System.getProperty("username", "WrongUsername");
    private static final String JIRA_PASSWORD = System.getProperty("password", "NotThepasword");
    private static final String PORTAL = System.getProperty("portal", "QA iOS Mobilev2");

    public static String getCucumberJsonReportLocation() {
        return CUCUMBER_JSON_REPORT_LOCATION;
    }

    public static String getGherkinFileLocation() {
        return GHERKIN_FILE_LOCATION;
    }

    public static String getPORTAL() {
        return PORTAL;
    }

    public static String getScreenshotLocation() {
        return SCREENSHOT_LOCATION;
    }

    public static int getProjectId() {
        return PROJECT_ID;
    }

    public static String getJiraUsername() {
        return JIRA_USERNAME;
    }

    public static String getJiraPassword() {
        return JIRA_PASSWORD;
    }

    public static List<String> getDevices() {
        return Arrays.asList(DEVICES.split(","));
    }

    public static String getVersionToCopyFrom() {
        return VERSION_TO_COPY_FROM;
    }

    public static String getDevice() {
        return DEVICE;
    }

}
