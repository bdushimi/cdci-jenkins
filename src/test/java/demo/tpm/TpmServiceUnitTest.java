package demo.tpm;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import demo.tpm.payment.PaymentService;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TpmServiceUnitTest {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void checkString() {
		String str = "Junit is working fine";
	    assertEquals("Junit is working fine",str);
	}
}
