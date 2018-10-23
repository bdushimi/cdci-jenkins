package demo.tpm;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import demo.tpm.payment.Payment;
import demo.tpm.payment.PaymentService;


@Component
public class ScheduledTasks {
	
	
	private List<Payment> listOfPayments;
	
	@Autowired
	private PaymentService paymentService;
	
	private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);


	@Scheduled(cron = "0 * * * * ?")
	public void pushPayment() {
		System.err.println("Push Payment Cron Job run at " + new Date());
		
		listOfPayments = paymentService.findPaymentByStatus(false);
		
		if(!listOfPayments.isEmpty())
			for (Payment payment : listOfPayments) {
				paymentService.makePayment(payment);
				
				log.info("Pushed Payment ", payment);
			}
		
		
	}
}
