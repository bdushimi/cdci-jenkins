package demo.tpm.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends JpaRepository<Payment, String>{
	
	List<Payment> findByPaymentStatus(String status);
}
