package deserializers;

import data.CucumberReport;
import data.Version;
import data.Version.Issue;
import data.Version.Step;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public class CucumberReportDeserializer {
    private Version version;
    private Deserializer deserializer;

    public CucumberReportDeserializer() {
        version = new Version();
        deserializer = new Deserializer();
    }

    public List<Issue> deserializeCucumberJsonReport() {
        return getReportIssues(deserializer.deserializeCucumberReport());
    }

    private List<Issue> getReportIssues(CucumberReport[] lines) {
        List<Issue> issues = new ArrayList<>();
        for (CucumberReport line : lines) {
            if (line.getElements() != null) {
                List<Issue> lineIssues = createIssues(line.getElements());
                issues.addAll(lineIssues);
            }
        }
        return issues;
    }

    private List<Issue> createIssues(List<CucumberReport.Element> elements) {
        List<Issue> issues = new ArrayList<>();
        elements.forEach(
                (CucumberReport.Element element) -> {
                    issues.add(createIssue(element));
                });
        return issues;
    }

    private Issue createIssue(CucumberReport.Element element) {
        Issue issue = version.new Issue();
        try {
            issue.setIssueId(getIssueId(element.getTags()));
        } catch (NoSuchElementException e) {
            throw new IllegalArgumentException("Test Name:" + element.getName() + ". Problem: Is missing a jira issueId");
        } catch (NullPointerException e){
            throw new IllegalArgumentException("Test Name:" + element.getName() + ". Problem: Is missing Tags");
        }
        issue.setSteps(getTestSteps(element.getSteps()));
        issue.setStatus(getIssueStatus(issue.getSteps()));
        issue.setTestName(element.getName());
        return issue;
    }

    private String getIssueStatus(List<Version.Step> steps) {

        List<String> statuses = getListOfStatuses(steps);
        if (statuses.contains("failed")) {
            return "failed";
        } else if (statuses.contains("skipped")) {
            return "skipped";
        } else if (statuses.contains("passed")) {
            return "passed";
        }
        throw new IllegalArgumentException("Steps are missing statuses");
    }

    private List<String> getListOfStatuses(List<Version.Step> steps) {
        return steps.stream().map(step -> step.getStatus()).collect(Collectors.toList());
    }

    private int getIssueId(List<CucumberReport.Tag> tags) throws NoSuchElementException, NullPointerException {
            return Integer.parseInt(
                    tags.stream()
                            .filter(tag -> StringUtils.isNumeric(tag.getName().replace("@", "")))
                            .findFirst()
                            .get()
                            .getName()
                            .replace("@", ""));
    }

    private List<Step> getTestSteps(List<CucumberReport.Event> steps) {
        List<Step> listOfSteps = new ArrayList<Step>();
        steps.forEach(
                (CucumberReport.Event event) -> {
                    Step step = createStep(event);
                    listOfSteps.add(step);
                });
        return listOfSteps;
    }

    private Step createStep(CucumberReport.Event event) {
        Version.Step step = version.new Step();
        step.setStepName(getStepName(event));
        step.setErrorMessage(getStepErrorMessage(event));
        step.setStatus(getStepStatus(event));
        step.setDuration(getStepDuration(event));
        return step;
    }

    private String getStepName(CucumberReport.Event event) {
        return event.getKeyword() + " " + event.getName();
    }

    private String getStepErrorMessage(CucumberReport.Event event) {
        return event.getResult().getErrorMessage();
    }

    private String getStepStatus(CucumberReport.Event event) {
        return event.getResult().getStatus();
    }

    private int getStepDuration(CucumberReport.Event event) {
        return event.getResult().getDuration();
    }
}
