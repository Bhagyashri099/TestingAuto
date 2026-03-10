//package StepDefinitions;
//import io.cucumber.java.en.*;
//import static org.hamcrest.Matchers.notNullValue;
//import static org.hamcrest.Matchers.equalTo;
//import static io.restassured.RestAssured.given;
//
//import io.restassured.RestAssured;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//
//import org.testng.Assert;
//import POM_Pages.PostRequest;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//public class APISteps {
////    
//    PostRequest apiLogic = new PostRequest(); 
//    Response response;
//    String generatedId;
//   // String baseUri = "https://gorest.co.in/public/v2";//"https://jsonplaceholder.typicode.com";
//   // String token = "0dcda9cb3cb7843a5f22e3ce861d388bd56823616c08dc8bf3a720799d8b02e5";
//
//    @Given("I have the Excel sheet ready at row {string}")
//    public void i_have_the_excel_sheet_ready_at_row(String rowNum) throws IOException {
//        // Loads the data from your Excel file via your PostRequest page class
//        apiLogic.getTestData(); 
//    }
//
//    /**
//     * FIXED: This step now actually performs the POST request.
//     * It uses data fetched from your Excel utility based on the row number.
//     */
//    @When("I send a POST request using data from Excel row {string}")
//    public void i_send_a_post_request_using_data_from_excel_row(String rowNumStr) throws IOException {
//        int rowNum = Integer.parseInt(rowNumStr);
//        Map<String, String> testData = apiLogic.getDataByRow(rowNum);
//
//        //GoRest mandatory fields
//      testData.put("gender", "male"); 
//       testData.put("status", "active");
//        testData.put("email", "user_" + System.currentTimeMillis() + "@example.com");
//        
//        // FIX: Map your Excel "FirstName" to the "name" field GoRest expects
//        if(testData.containsKey("FirstName")) {
//            testData.put("name", testData.get("FirstName"));
//            
//            apiLogic.writeSpecificRow(
//                    rowNum, 
//                    generatedId, 
//                    response.getStatusCode(), 
//                    response.asPrettyString() // This saves the full JSON
//                );  
//            
//        }
//
//        response = given()
//            .baseUri(baseUri)
//            .header("Authorization", "Bearer " + token) 
//            .contentType("application/json")
//            .body(testData)
//        .when()
//            .post("/users")
//        .then()
//            .log().all()
//            .extract().response();
//
//        // Check if response is JSON before parsing
//        if (response.getStatusCode() == 201) {
//            generatedId = response.jsonPath().getString("id");
//            System.out.println("POST Successful. Generated ID: " + generatedId);
//        } else {
//            System.out.println("POST Failed with Status: " + response.getStatusCode());
//        }
//        
//    }
//    @When("I send a GET request using the extracted ID")
//    public void i_send_a_get_request_using_the_extracted_id() {
//        System.out.println("Fetching persistent data for ID: " + generatedId);
//        
//        response = given()
//                .baseUri(baseUri)
//                .header("Authorization", "Bearer " + token)
//            .when()
//                .get("/users/" + generatedId) 
//            .then()
//                .log().all()
//                .statusCode(200) // This will now pass as GoRest persists data
//                .extract().response();
//  
//        System.out.println("GET Response Status: " + response.getStatusCode());
//        System.out.println("GET Response Body: " + response.asPrettyString());
//    }
//
//
//    @Then("the status code should be {int}")
//    public void the_status_code_should_be(int expectedStatus) {
//        // This will no longer be NULL because the @When step above initializes it
//        Assert.assertNotNull(response, "Response is NULL! The @When step failed to execute or assign the response.");
//        Assert.assertEquals(response.getStatusCode(), expectedStatus, "Status code mismatch!");
//    }
//
//    @Then("I save the generated ID and status back to row {int}")
//    public void i_save_the_generated_id_and_status_back_to_row(int rowNum) throws IOException {
//        // Writes results back to your Excel file
//        apiLogic.writeSpecificRow(rowNum, generatedId, response.getStatusCode(), baseUri);
//        apiLogic.writeSpecificRow(
//                rowNum, 
//                generatedId, 
//                response.getStatusCode(), 
//                response.asPrettyString()
//            );
//    
//    
//    }
//
////    @When("I send a GET request using the extracted ID")
////    public void i_send_a_get_request_using_the_extracted_id() throws InterruptedException {
////    	
////    	System.out.println("Attempting GET for the NEWLY generated ID: " + generatedId);
////        
////    	    response = given()
////    	            .baseUri(baseUri)
////    	            .header("Authorization", "Bearer " + token) // Must use token here too
////    	        .when()
////    	            .get("/users/" + generatedId) 
////    	        .then()
////    	            .log().all()
////    	            .statusCode(200) // This will now PASS because GoRest saves the data
////    	            .extract().response();
////
////        // 3. Print the REAL data returned by the server
////        System.out.println("GET Response Status: " + response.getStatusCode());
////        System.out.println("GET Response Body: " + response.asPrettyString());
////    }
//    
//    
//    @Then("the response should contain the name {string}")
//    public void the_response_should_contain_the_name(String name) {
//    	String responseBody = response.asString();
//        System.out.println("Validating Response: " + responseBody);
//
//        // If GET returned empty {}, we can't validate the name there.
//        // We check if the body is empty or if we are looking at the POST response.
//        if (responseBody.equals("{}")) {
//            System.out.println("Note: ReqRes does not persist data, skipping GET validation.");
//            // Optional: Fail with a descriptive message or pass if POST was successful
//        } else {
//            // Standard validation for the POST response or a valid GET response
//            response.then().body("name", equalTo(name));
//            System.out.println("Success! Verified name: " + name);
//        }
//    }
//
//	static {
//        // This will stop the "DEBUG" logs from filling your console
//        Logger.getLogger("org.apache.http").setLevel(Level.OFF);
//        Logger.getLogger("io.restassured").setLevel(Level.OFF);
//    }
//    private String endpoint;
//    private Response response;
//    private RequestSpecification request = RestAssured.given();
//
//    @Given("the API endpoint is {string}")
//    public void setEndpoint(String url) {
//        this.endpoint = url;
//    }
//
//    @Given("the request payload is malformed")
//    public void setMalformedPayload() {
//        request.header("Content-Type", "application/json");
//        request.body("{ \"invalid\": syntax "); 
//    }
//
//    @Given("no authorization header is provided")
//    public void removeAuthHeader() {
//        request.header("Authorization", "");
//    }
//
//    @When("I send a GET request")
//    public void sendGet() {
//        response = request.get(endpoint);
//    }
//
//    @When("I send a POST request")
//    public void sendPost() {
//        response = request.post(endpoint);
//    }
//
//    @Then("the response status code should be {int}")
//    public void verifyStatus(int expectedStatus) {
//        int actualStatus = response.getStatusCode();
//        String contentType = response.getContentType();
//
//        if (actualStatus != expectedStatus) {
//            System.out.println("FAILED! Content-Type received: " + contentType);
//            if (contentType.contains("html")) {
//                System.out.println("ERROR: You hit a web page, not an API endpoint. Check your URL.");
//            }
//            Assert.assertEquals(actualStatus, expectedStatus, "Status code mismatch! URL was: " + endpoint);
//        }
//    }
//}
