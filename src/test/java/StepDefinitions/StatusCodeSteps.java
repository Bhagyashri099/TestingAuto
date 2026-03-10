package StepDefinitions;

import java.util.HashMap;
import java.util.Map;
// 1. ADD THESE MISSING IMPORTS
import java.util.logging.Level;
import java.util.logging.Logger;

import org.testng.Assert;
import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsString;

public class StatusCodeSteps {
	
	public void setup() {
	    // This runs before every scenario to ensure a clean state
	    //request = RestAssured.given().log().all();
	}
    
    // Static block to suppress unnecessary debug logs
//	static {
//        // This will stop the "DEBUG" logs from filling your console
//        Logger.getLogger("org.apache.http").setLevel(Level.OFF);
//        Logger.getLogger("io.restassured").setLevel(Level.OFF);
//    }
//    private String endpoint;
//    private Response response;
//    private RequestSpecification request = RestAssured.given();
//   // private Response response;
//    private String token;
//
//    @Given("the API endpoint is {string}")
//    public void setEndpoint(String url) {
//        this.endpoint = url;
//    }
//
//   
//    @When("I send a GET request")
//    public void sendGet() {
//        response = request.get(endpoint);
//    }
//
//    @When("I send a POST request")
//    public void sendPOST() {
//    	// int rowNum = Integer.parseInt(rowNumStr);
//    	response = request.get(endpoint);
//       HashMap<String, String> testData = new HashMap<>();
//
//       //GoRest mandatory fields
//     testData.put("userId", "12"); 
//      testData.put("id", "active");
//       testData.put("title", "Testing");
//       testData.put("body", "Testing user");
//       
//       // FIX: Map your Excel "FirstName" to the "name" field GoRest expects
//      
//       response = given()
//           //.baseUri(baseUri)
//           //.header("Authorization", "Bearer " + token) 
//           .contentType("application/json")
//           .body(testData)
//       .when()
//           .post("/users")
//       .then()
//           .log().all()
//           .extract().response();
//////           
//       }
//    
//    //3rd
//    @Given("I have a {string} token {string}")
//    public void setToken(String type, String tokenValue) {
//        this.token = tokenValue;
//    }
//
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
//
//
//    @Then("the response status code should be {int}")
//    public void verifyStatus(int expectedStatus) {
//        int actualStatus = response.getStatusCode();
//        String contentType = response.getContentType();
//System.out.println(actualStatus);
//        if (actualStatus != expectedStatus) {
//            System.out.println("FAILED! Content-Type received: " + contentType);
//            if (contentType.contains("html")) {
//                System.out.println("ERROR: You hit a web page, not an API endpoint. Check your URL.");
//            }
//            Assert.assertEquals(actualStatus, expectedStatus, "Status code mismatch! URL was: " + endpoint);
//            
//        }
//    }
//   
    
}
