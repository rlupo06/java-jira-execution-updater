package data;

import java.util.ArrayList;
import java.util.List;
import utils.Config;

public class Version {
    public List<Cycle> cycles;
    private String versionName;
    private int versionId;
    private int projectId = Config.getProjectId();
    public Version() {
        cycles = new ArrayList<Cycle>();
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getVersionId() {
        return versionId;
    }

    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public List<Cycle> getCycles() {
        return cycles;
    }

    public void setCycles(List<Cycle> cycles) {
        this.cycles = cycles;
    }

    public class Cycle {
        public List<Issue> issues;
        private int cycleId;
        private String cycleName;

        public Cycle() {
            issues = new ArrayList<Issue>();
        }

        public String getCycleName() {
            return cycleName;
        }

        public void setCycleName(String cycleName) {
            this.cycleName = cycleName;
        }

        public List<Issue> getIssues() {
            return issues;
        }

        public void setIssues(List<Issue> issues) {
            this.issues = issues;
        }

        public int getCycleId() {
            return cycleId;
        }

        public void setCycleId(int cycleId) {
            this.cycleId = cycleId;
        }
    }

    public class Issue {

        private String testName;

        private String status;

        private int issueId;

        private int executionId;

        private List<Step> steps;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getExecutionId() {
            return executionId;
        }

        public void setExecutionId(int executionId) {
            this.executionId = executionId;
        }

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }

        public int getIssueId() {
            return issueId;
        }

        public void setIssueId(int issueId) {
            this.issueId = issueId;
        }

        public List<Step> getSteps() {
            return steps;
        }

        public void setSteps(List<Step> steps) {
            this.steps = steps;
        }

        @Override
        public String toString() {
            return "TestResult [testName = "
                    + testName
                    + ", issueId = "
                    + issueId
                    + ", steps = "
                    + steps
                    + "]";
        }
    }

    public class Step {
        private String errorMessage;

        private int duration;

        private String status;

        private int stepId;

        private String stepName;

        private int stepResultId;

        public Step() {}

        public int getStepResultId() {
            return stepResultId;
        }

        public void setStepResultId(int stepResultId) {
            this.stepResultId = stepResultId;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getStepId() {
            return stepId;
        }

        public void setStepId(int stepId) {
            this.stepId = stepId;
        }

        public String getStepName() {
            return stepName;
        }

        public void setStepName(String stepName) {
            this.stepName = stepName;
        }

        @Override
        public String toString() {
            return "Step [errorMessage = "
                    + errorMessage
                    + ", duration = "
                    + duration
                    + ", status = "
                    + status
                    + ", stepId = "
                    + stepId
                    + ", stepName = "
                    + stepName
                    + "]";
        }
    }
}
