package payloads;

import java.util.List;

public class UpdateBulkExecutionStatus {
    private List<Integer> executions;
    private int status;

    public List<Integer> getExecutions() {
        return executions;
    }

    public void setExecutions(List<Integer> executions) {
        this.executions = executions;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
