package payloads;

import java.util.List;

public class UpdateJiraIssue {
    private Update update;

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public class Update {
        private List<Summary> summary;

        public List<Summary> getSummary() {
            return summary;
        }

        public void setSummary(List<Summary> summary) {
            this.summary = summary;
        }
    }

    public class Summary {
        private String set;

        public String getSet() {
            return set;
        }

        public void setSet(String set) {
            this.set = set;
        }
    }
}