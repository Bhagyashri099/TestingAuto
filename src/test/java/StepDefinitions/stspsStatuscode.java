package StepDefinitions;

import io.restassured.RestAssured;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
// 1. ADD THESE MISSING IMPORTS
import java.util.logging.Level;
import io.cucumber.java.Before;
import java.util.logging.Logger;

import org.testng.Assert;

import DataSetup.ConfigReader;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsString;


public class stspsStatuscode {

	@Before
	public void setup() {
		
	    // This runs before every scenario to ensure a clean state
        request = given()
                .relaxedHTTPSValidation()
                
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)")
                .header("Accept", "application/json")
                .contentType("application/json")
                .log().all();

	}
	static {
        // This will stop the "DEBUG" logs from filling your console
        Logger.getLogger("org.apache.http").setLevel(Level.OFF);
        Logger.getLogger("io.restassured").setLevel(Level.OFF);
    }
    private String endpoint;
    private Response response;
    private RequestSpecification request = RestAssured.given();
   // private Response response;
    private String token;

    @Given("the API endpoint is {string}")
    public void setEndpoint(String url) {
        this.endpoint = url;
    }
    @When("I send a POST with broken body")
    public void sendBrokenBody() {
    	String body = "{\"name\": \"test\", \"job\": \"QA\" "; 

   	    // 2. Execute the Request
   	    response = given()
   	            
   	            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
   	            .contentType("application/json")
   	            // INVALID KEY to trigger 401
   	           .header("x-api-key", "reqres_221c6616f47b4adcb5ae03fd7efb2416") 
   	            .body(body)
   	        .when()
   	            .post(endpoint);
    }
    
    @When("I send a POST request with INVALID API Key")
    public void i_send_a_POST_request_with_invalid_api_key() {
    	String body = "{\"name\": \"test\", \"job\": \"QA\" "; 
//    	 Map<String, Object> body = new HashMap<>();
//    	    body.put("name","test}" );
//    	    body.put("job", "QA Automation");

    	    // 2. Execute the Request
    	    response = given()
    	            .relaxedHTTPSValidation()
    	            // STEALTH HEADERS to avoid 403
    	            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
    	            .header("X-Reqres-Env", "prod")
    	            .header("Accept", "application/json")
    	            .contentType("application/json")
    	            // INVALID KEY to trigger 401
    	           //.header("x-api-key", "reqres_221c6616f47b4adcb5ae03fd7efb2416") 
    	            .body(body)
    	        .when()
    	            .post(endpoint);

    	    // 3. Log the response to see the error message
    	    System.out.println("Status Code: " + response.getStatusCode());
    	    System.out.println("Error Body: " + response.asString());
    }
   
    @When("I send a GET request")
    public void sendGet() {
    	response = request.when().get(endpoint);   
    	}

//    @When("I send a POST request")
//    public void sendPOST() {
//    	// int rowNum = Integer.parseInt(rowNumStr);
//    	Map<String, Object> testData = new HashMap<>();
//        testData.put("userId", 12); 
//        testData.put("id", 101);
//        testData.put("title", "Testing");
//        testData.put("body", "Testing user");
//
//        // 2. Execute Request (Using the endpoint from @Given)
//        response = given()
//            .header("User-Agent", "Mozilla/5.0")
//            .contentType("application/json")
//            .body(testData)
//        .when()
//            .post(endpoint) ;  
//        //           
//       }
    
    //3rd
//    @Given("I have a {string} token {string}")
//    public void setToken(String type, String tokenValue) {
//        this.token = tokenValue;
  //s  }

//    @When("I send a POST request to {string} with an invalid integer for {string}")
//    public void sendInvalidPostRequest(String endpoint, String fieldName) {
//        Map<String, Object> badBody = new HashMap<>();
//        badBody.put("name", "Test User");
//        badBody.put("email", "error" + System.currentTimeMillis() + "@test.com");
//        badBody.put(fieldName, 12345); // TRICK: Sending Integer instead of String "male"
//        badBody.put("status", "active");
//
//        response = given()
//                .header("Authorization", "Bearer " + token)
//                .contentType("application/json")
//                .body(badBody)
//            .when()
//                .post(endpoint);
//    }


    @Then("the response status code should be {int}")
    public void verifyStatus(int expectedStatus) {
        int actualStatus = response.getStatusCode();
        String contentType = response.getContentType();
System.out.println(actualStatus);
        if (actualStatus != expectedStatus) {
            System.out.println("FAILED! Content-Type received: " + contentType);
            if (contentType.contains("html")) {
                System.out.println("ERROR: You hit a web page, not an API endpoint. Check your URL.");
            }
            Assert.assertEquals(actualStatus, expectedStatus, "Status code mismatch! URL was: " + endpoint);
            
        }
    }
    @When("I send a POST request with an invalid API key")
    public void sendPostWithInvalidKey() {
        // A dummy body is required for POST requests
    	Map<String, String> body = new HashMap<>();
        body.put("name", "Test");

        response = given()
                .relaxedHTTPSValidation()
                // USE THIS EXACT LONG USER-AGENT TO AVOID 403
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36")
                .header("X-Reqres-Env", "prod")
                //.header("x-api-key", "wrong_key_123") 
                .contentType("application/json")
                .body(body)
            .when()
                .post(endpoint); 

    }
    @When("I send a POST request using data")
    public void i_send_a_post_request_using_data() throws IOException {
        	ConfigReader.loadProperties();
        	String baseUri = ConfigReader.properties.getProperty("baseUri");
            String token = ConfigReader.properties.getProperty("token");

        	//int rowNum = Integer.parseInt(rowNumStr);
       // Map<String, String> testData = apiLogic.getDataByRow(rowNum);
        	Map<String, String> testData = new HashMap<>();
           
       testData.put("name","test");
       testData.put("job","test");
       testData.put("gender", "male"); 
       testData.put("status", "active");
        testData.put("email", "user_" + System.currentTimeMillis() + "@example.com");
        
        if(testData.containsKey("FirstName")) {
            testData.put("name", testData.get("FirstName")); 
        }

        response = given()
            .baseUri(baseUri)
           // .header("x-api-key", token) 
            .header("Authorization", "Bearer " + token) 
            .contentType("application/json")
            .body(testData)
        .when()
            .post("/posts")
        .then()
            .log().all()
            .extract().response();}

    @Then("the response should contain {string}")
    public void verifyErrorContent(String expectedError) {
        response.then().body("error", org.hamcrest.Matchers.contains(expectedError));
    }


}
