package zephyrapiclasses;

public class ListOfTestStep {
	private int id;
	private int orderId;
	private String step;
	private String createdBy;
	private String modifiedBy;
	private String htmlStep;
	private String htmlResult;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getHtmlStep() {
		return htmlStep;
	}

	public void setHtmlStep(String htmlStep) {
		this.htmlStep = htmlStep;
	}

	public String getHtmlResult() {
		return htmlResult;
	}

	public void setHtmlResult(String htmlResult) {
		this.htmlResult = htmlResult;
	}

}
