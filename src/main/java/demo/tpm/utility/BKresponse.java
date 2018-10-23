package demo.tpm.utility;

public class BKresponse {
	
	private String transactionNumber;
	private String referenceNumber;
	private String responseCode;
	private String responseMessage;
	private String responseTime;
	private String acknowledgeNumber;
	private String amountPaid;
	
	
	public BKresponse() {
		super();
	}
	
	public String getTransactionNumber() {
		return transactionNumber;
	}
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	public String getReferenceNumber() {
		return referenceNumber;
	}
	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public String getAcknowledgeNumber() {
		return acknowledgeNumber;
	}
	public void setAcknowledgeNumber(String acknowledgeNumber) {
		this.acknowledgeNumber = acknowledgeNumber;
	}
	public String getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(String amountPaid) {
		this.amountPaid = amountPaid;
	}
	
	@Override
	public String toString() {
		return "BKresponse [transactionNumber=" + transactionNumber + ", referenceNumber=" + referenceNumber
				+ ", responseCode=" + responseCode + ", responseMessage=" + responseMessage + ", responseTime="
				+ responseTime + ", acknowledgeNumber=" + acknowledgeNumber + ", amountPaid=" + amountPaid + "]";
	}

}
