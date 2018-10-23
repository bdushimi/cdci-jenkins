package demo.tpm.payment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import demo.tpm.utility.CustomJsonDateDeserializer;

 @Entity
 @Table(name = "vat_payment_logs", schema = "vsdc")
public class Payment {
	
	@Id
	private String mcashReferenceNumber;
	
	private String invoiceNumber;
	private String debitAccount;
	private String creditAccount;
	private float paymentAmount;
	private String bankReferenceNumber;
	
	//@JsonDeserialize(using = CustomJsonDateDeserializer.class)
    /*@JsonFormat
    (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")*/
	private String paymentDatetime;
	
	private String lastUpdateDatetime;
	
	private String paymentStatus;
	private String paymentStatusDescription;
	
	
	
	public Payment() {
		super();
	}
	
	public Payment(String invoiceNumber, String debitAccount, String creditAccount,
					String mcashReferenceNumber, float paymentAmount, String paymentDatetime,
					String lastUpdateDatetime) {
		super();
		this.invoiceNumber = invoiceNumber;
		this.debitAccount = debitAccount;
		this.creditAccount = creditAccount;
		this.mcashReferenceNumber = mcashReferenceNumber;
		this.paymentAmount = paymentAmount;
		this.paymentDatetime = paymentDatetime;
		this.lastUpdateDatetime = lastUpdateDatetime;
	}
	
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public String getDebitAccount() {
		return debitAccount;
	}
	public void setDebitAccount(String debitAccount) {
		this.debitAccount = debitAccount;
	}
	public String getCreditAccount() {
		return creditAccount;
	}
	public void setCreditAccount(String creditAccount) {
		this.creditAccount = creditAccount;
	}
	public String getMcashReferenceNumber() {
		return mcashReferenceNumber;
	}
	public void setMcashReferenceNumber(String mcashReferenceNumber) {
		this.mcashReferenceNumber = mcashReferenceNumber;
	}
	public float getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(float paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getPaymentDatetime() {
		return paymentDatetime;
	}
	
	//@JsonDeserialize(using = CustomJsonDateDeserializer.class)
	public void setPaymentDatetime(String paymentDatetime) {
		this.paymentDatetime = paymentDatetime;
	}
	public String isPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public String getPaymentStatusDescription() {
		return paymentStatusDescription;
	}
	public void setPaymentStatusDescription(String paymentStatusDescription) {
		this.paymentStatusDescription = paymentStatusDescription;
	}

	public String getBankReferenceNumber() {
		return bankReferenceNumber;
	}

	public void setBankReferenceNumber(String bankReferenceNumber) {
		this.bankReferenceNumber = bankReferenceNumber;
	}
	
	public String getPaymentStatus() {
		return paymentStatus;
	}

	public String getLastUpdateDatetime() {
		return lastUpdateDatetime;
	}

	public void setLastUpdateDatetime(String lastUpdateDatetime) {
		this.lastUpdateDatetime = lastUpdateDatetime;
	}

	@Override
	public String toString() {
		return "Payment [mcashReferenceNumber=" + mcashReferenceNumber + ", invoiceNumber=" + invoiceNumber
				+ ", debitAccount=" + debitAccount + ", creditAccount=" + creditAccount + ", paymentAmount="
				+ paymentAmount + ", bankReferenceNumber=" + bankReferenceNumber + ", paymentDatetime="
				+ paymentDatetime + ", lastUpdateDatetime=" + lastUpdateDatetime + ", paymentStatus=" + paymentStatus
				+ ", paymentStatusDescription=" + paymentStatusDescription + "]";
	}

	

}
