package zephyrapiclasses;

import java.util.List;

public class ListOfExecutions {

    private List<Execution> executions;

    public List<Execution> getExecutions() {
        return executions;
    }

    public void setExecutions(List<Execution> execution) {
        this.executions = execution;
    }

    public class Execution {
        public String versionName;
        private int id;
        private int cycleId;
        private String cycleName;
        private int versionId;
        private int projectId;
        private int issueId;
        private String summary;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCycleId() {
            return cycleId;
        }

        public void setCycleId(int cycleId) {
            this.cycleId = cycleId;
        }

        public String getCycleName() {
            return cycleName;
        }

        public void setCycleName(String cycleName) {
            this.cycleName = cycleName;
        }

        public int getVersionId() {
            return versionId;
        }

        public void setVersionId(int versionId) {
            this.versionId = versionId;
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

        public void setProjectId(int projectId) {
            this.projectId = projectId;
        }

        public int getIssueId() {
            return issueId;
        }

        public void setIssueId(int issueId) {
            this.issueId = issueId;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }


}
