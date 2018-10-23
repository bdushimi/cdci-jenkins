package demo.tpm.payment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import demo.tpm.ScheduledTasks;
import demo.tpm.utility.BKresponse;
import demo.tpm.utility.NameSpaceResolver;


@Service
public class PaymentService {

	
	private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
	
	@Autowired
	private PaymentRepository paymentRepository;
	
	public List<Payment> findAllPaymentsDetailsByStatus(String status) {
		return paymentRepository.findByPaymentStatus(status);
	}
	
	protected List<Payment> findAllPaymentsDetails(){
		return paymentRepository.findAll();
	}
	
	protected Payment findPaymentDetails(String mcashReferenceNumber)
	{
		Optional<Payment> paymentOptional;
		paymentOptional = paymentRepository.findById(mcashReferenceNumber);
		
		if(paymentOptional.isPresent())
			return paymentOptional.get();
		return null;
	}
	
	protected Payment saveVATPaymentDetails(Payment payment) {
		payment.setPaymentStatus("pending");
		payment.setPaymentStatusDescription("Waiting BANK notification");
		return paymentRepository.save(payment);
	}
	
	public Payment makePayment(Payment payment) {
		
		BKresponse bkResponse;
		
		//make funds transfer using BK test API
		Map<String, String> response= performBankFundTransfer(payment);
		
		String responseXML = response.get("responseXML");
		   
		String responseHttpCode = response.get("httpCode");
		
		//String responseHttpCode = "200";
		
		System.out.println("BK API Test.."+responseHttpCode);
		
		
		// HTTP request was received and understood by the server 
		if(responseHttpCode.equals("200")) {
			
			try {
				
				bkResponse = parseBKresponse(responseXML);
				payment.setPaymentStatus(bkResponse.getResponseCode());
				payment.setPaymentStatusDescription(bkResponse.getResponseMessage());
				
				if(bkResponse.getResponseCode() !=null)
					if(bkResponse.getResponseCode().equals("00"))
						payment.setBankReferenceNumber(bkResponse.getTransactionNumber());
					else if (bkResponse.getResponseCode().equals("08")) {
						
						log.error("Payment transaction error : "+bkResponse.getResponseMessage());
						log.info("Simulate Success Response from BK API");
						
						payment.setPaymentStatus("00");
						payment.setPaymentStatusDescription("The transaction completed successfully.");
						payment.setBankReferenceNumber(""+generateRandomNumber());
					}
				
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			// generate random number
			// Generate random integers in range 0 to 999 

			
		// if the BK test API is down
		}else if (responseHttpCode.equals("error")) {
			
			log.error("Connection to BK API .... ERROR ");
			log.info("Simulate Success Response from BK API");
			
			payment.setPaymentStatus("00");
			payment.setPaymentStatusDescription("The transaction completed successfully.");
			payment.setBankReferenceNumber(""+generateRandomNumber());
		}
		
		return payment;
	}
	
	public void deletePayment(String invoiceNumber) {
		//paymentRepository.deleteById(invoiceNumber);
	}
	
	public List<Payment> findPaymentByStatus(boolean paymentStatus){
		//return paymentRepository.findByPaymentStatus(paymentStatus);
		return new ArrayList();
	}

	private Map<String, String> performBankFundTransfer(Payment payment) {
	
	Map<String, String> responseMap = new HashMap<String, String>();	
	
	String xmlString = 
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+ "<ns2:FundsTransferRequest xmlns:ns2=\"http://www.developer.bk.rw/serviceprovider/frontend/client\">"
				+ "<ns2:debitAccount>"+payment.getDebitAccount()+"</ns2:debitAccount>"
				+ "<ns2:creditAccount>"+payment.getCreditAccount()+"</ns2:creditAccount>"
				+ "<ns2:amount>"+ payment.getPaymentAmount() +"</ns2:amount>"
				+ "<ns2:debitCurrency>RWF</ns2:debitCurrency>"
				+ "<ns2:description>"+ payment.getInvoiceNumber() +"</ns2:description>"
				+ "<ns2:providerTransactionID>"+ payment.getMcashReferenceNumber() +"</ns2:providerTransactionID>"
				+ "<ns2:creditCurrency>RWF</ns2:creditCurrency>"
				+ "<ns2:requestTimeStamp>"+ payment.getPaymentDatetime() +"</ns2:requestTimeStamp>"
			+ "</ns2:FundsTransferRequest>";

	 String serverResponse = "";
	 
     try{
         RestTemplate restTemplate =  new RestTemplate();
         
         //Create a list for the message converters
         List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
         
         //Add the String Message converter
         messageConverters.add(new StringHttpMessageConverter());
         
         //Add the message converters to the restTemplate
         restTemplate.setMessageConverters(messageConverters);
         HttpHeaders headers = createHttpHeaders("myapNKxovQXNEVzlbGTTndGN", "-kATDbrfGfOCUssnJXOhtmXlHKDWGfVDoull");
         
         HttpEntity<String> request = new HttpEntity<String>(xmlString, headers);
         final ResponseEntity<String> response = restTemplate.postForEntity("http://156.38.10.43:2160/FundsTransfer/2.0", request, String.class);
         
         //serverResponse = response.getBody();
         
         responseMap.put("responseXML", response.getBody());
         responseMap.put("httpCode", response.getStatusCode().toString());
         responseMap.put("httpCodeValue", ""+response.getStatusCodeValue()+"");
         
     }catch (Exception ex){
         serverResponse = ex.getMessage();
         responseMap.put("response", ex.getMessage());
         responseMap.put("httpCode", "error");
         responseMap.put("httpCodeValue", "error");
     }
     return responseMap;
		 
	}
	
	/*
	private String perfomCyclosPayment(Payment payment) {
		
		String res = null;
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CyclosSoapClientConfig.class);
        
        
        // Construct CyclosPaymentEntity
        CyclosPaymentEntity cyclosPaymentEntity = new CyclosPaymentEntity(new Long(51), payment.getPaymentAmount(), "", "MuhireInn", payment.getInvoiceNumber());
        
        CyclosClient cyclosClient = context.getBean(CyclosClient.class);
        try {
			res = cyclosClient.makePayment(cyclosPaymentEntity);
		} catch (JSONException e) {
			e.printStackTrace();
		}finally {
			System.out.println(res);
			return res;
		}
        
	}*/

	private HttpHeaders createHttpHeaders(String user, String password)
	{
	    String notEncoded = user + ":" + password;
	    String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(notEncoded.getBytes());
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_XML);
	    headers.add("Authorization", encodedAuth);
	    return headers;
	}

	private BKresponse parseBKresponse(String xmlString) throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		BKresponse bkResponse = new BKresponse();
		String staticPath = "//ns2:FundsTransferResponse/ns2:response/ns2:";
		String temp;
		
		temp=xmlParser(xmlString, staticPath+"referenceNumber");
		bkResponse.setReferenceNumber(temp.substring(1, temp.length()-1));
		
		temp=xmlParser(xmlString, staticPath+"transactionNumber");
		bkResponse.setTransactionNumber(temp.substring(1, temp.length()-1));
		
		temp=xmlParser(xmlString, staticPath+"responseCode");
		bkResponse.setResponseCode(temp.substring(1, temp.length()-1));
		
		temp=xmlParser(xmlString, staticPath+"responseMessage");
		bkResponse.setResponseMessage(temp.substring(1, temp.length()-1));
		
		temp=xmlParser(xmlString, staticPath+"responseTime");
		bkResponse.setResponseTime(temp.substring(1, temp.length()-1));
		
		temp=xmlParser(xmlString, staticPath+"acknowledgeNumber");
		bkResponse.setAcknowledgeNumber(temp.substring(1, temp.length()-1));
		
		temp=xmlParser(xmlString, staticPath+"amountPaid");
		bkResponse.setAmountPaid(temp.substring(1, temp.length()-1));
		
		return bkResponse;
	}
	
 	private String xmlParser(String xmlString, String nodeName) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		//Want to read all book names from XML
		ArrayList<String> bookNames = new ArrayList<String>();
		 
		//Parse XML file
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(
				new ByteArrayInputStream(xmlString.getBytes(Charset.forName("UTF-8"))));
	
		 
		//Get XPath expression
		XPathFactory xpathfactory = XPathFactory.newInstance();
		XPath xpath = xpathfactory.newXPath();
		xpath.setNamespaceContext(new NameSpaceResolver(doc));
		XPathExpression expr = xpath.compile(nodeName+"/text()");
		 
		//Search XPath expression
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		 
		//Iterate over results and fetch book names
		NodeList nodes = (NodeList) result;
		for (int i = 0; i < nodes.getLength(); i++) {
		    bookNames.add(nodes.item(i).getNodeValue());
		}
		 
		//Verify book names
		System.out.println(bookNames);
		return bookNames.toString();
	}
	
 	protected void sendVATPaymentNotification(Payment payment) {
 	
 		
 		 //String PPM_INSTANCE_URL = "http://localhost:8899/api/v1/payments";
 		
 		 //use this online
 		 String PPM_INSTANCE_URL="http://dev.mobivat.com:8080/demo-rpm/api/v1/payments";
 	      
 		  HttpHeaders headers = new HttpHeaders();
 	      headers.setContentType(MediaType.APPLICATION_JSON);
 	      
 	      RestTemplate restTemplate = new RestTemplate();
 	 
 	      // Data attached to the request.
 	      HttpEntity<Payment> requestBody = new HttpEntity<>(payment, headers);
 	      
 	      // Send request with POST method.
 	      ResponseEntity<String> result 
 	             = restTemplate.postForEntity(PPM_INSTANCE_URL, requestBody, String.class);
 	        
 	      System.out.println("Status code:" + result.getStatusCode());
 	 
 	      // Code = 200.
 	      if (result.getStatusCode() == HttpStatus.OK) {
 	        	String thePayment = result.getBody();
 	            System.out.println("(Client Side) Notification sent to PPM: "+ thePayment);
 	       }
 	        
 	}

 	private int generateRandomNumber() {
		int rand_number = ThreadLocalRandom.current().nextInt();
		return Math.abs(rand_number);
 	}
}
