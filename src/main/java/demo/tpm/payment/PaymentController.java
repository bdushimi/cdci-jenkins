package demo.tpm.payment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PaymentController {

	@Autowired
	PaymentService paymentService;
	
	@PostMapping(value="/api/v1/payments")
	public Payment makePayment(@RequestBody Payment payment) {
		
		// persists the vat payment details
		paymentService.saveVATPaymentDetails(payment);
		
		// initiates the funds transfer
		payment = paymentService.makePayment(payment);
		
		System.out.println("Payment object after BK api call : "+payment);
		
		// if the HTTP request was received and understood by the server (Payment Processor API)
		
		if (payment.getPaymentStatus()!=null) {
			
			if(payment.getPaymentStatus().equals("00")) {
				
				System.out.println("Sending Payment notification to PPM module");
				System.out.println(payment);
				// send payment notification to PPM module
				paymentService.sendVATPaymentNotification(payment);
			}
			
			else if(payment.getPaymentStatus().equals("error")) {
				payment.setPaymentStatus("00");
				payment.setPaymentStatusDescription("The transaction completed successfully.");
			}
		}
		return payment;
	}
	
	// get all payments records from the database
	@GetMapping(value="/api/v1/payments")
	public List<Payment> getPaymentsDetails(){
		return paymentService.findAllPaymentsDetails();
		
	}
	
	// get a payment record by its id i.e. mcashReferenceNumber
	@GetMapping(value="/api/v1/payment/{mcashReferenceNumber}")
	public Payment getPaymentDetails(@PathVariable String mcashReferenceNumber){
		return paymentService.findPaymentDetails(mcashReferenceNumber);
	}
	
	
	// get payment records by their status
	@GetMapping(value="/api/v1/payments/{status}")
	public List<Payment> getPaymentsDetailsByStatus(@PathVariable String status)
	{
		return paymentService.findAllPaymentsDetailsByStatus(status);
	}
	
}
