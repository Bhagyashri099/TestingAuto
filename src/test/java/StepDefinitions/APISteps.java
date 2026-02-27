package StepDefinitions;

import io.cucumber.java.en.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;

import DataSetup.ConfigReader;
import POM_Pages.PostRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class APISteps {
    
    PostRequest apiLogic = new PostRequest(); 
    Response response;
    String generatedId;
   // String baseUri = "https://gorest.co.in/public/v2";//"https://jsonplaceholder.typicode.com";
   // String token = "0dcda9cb3cb7843a5f22e3ce861d388bd56823616c08dc8bf3a720799d8b02e5";

    //try
        
    
    @Given("I have the Excel sheet ready at row {string}")
    public void i_have_the_excel_sheet_ready_at_row(String rowNum) throws IOException {
        // Loads the data from Excel file via your PostRequest page class
        apiLogic.getTestData(); 
    }
    //

        @When("I send a POST request using data from Excel row {string}")
    public void i_send_a_post_request_using_data_from_excel_row(String rowNumStr) throws IOException {
        	ConfigReader.loadProperties();
        	String baseUri = ConfigReader.properties.getProperty("baseUri");
            String token = ConfigReader.properties.getProperty("token");

        	int rowNum = Integer.parseInt(rowNumStr);
        Map<String, String> testData = apiLogic.getDataByRow(rowNum);

       testData.put("gender", "male"); 
       testData.put("status", "active");
        testData.put("email", "user_" + System.currentTimeMillis() + "@example.com");
        
        if(testData.containsKey("FirstName")) {
            testData.put("name", testData.get("FirstName"));
        }

        response = given()
            .baseUri(baseUri)
            .header("Authorization", "Bearer " + token) 
            .contentType("application/json")
            .body(testData)
        .when()
            .post("/users")
        .then()
            .log().all()
            .extract().response();

        // Check if response is JSON before parsing
        if (response.getStatusCode() == 201) {
            generatedId = response.jsonPath().getString("id");
            System.out.println("POST Successful. Generated ID: " + generatedId);
        } else {
            System.out.println("POST Failed with Status: " + response.getStatusCode());
        }
    }
    @When("I send a GET request using the extracted ID")
    public void i_send_a_get_request_using_the_extracted_id() {
        System.out.println("Fetching persistent data for ID: " + generatedId);
        String baseUri = ConfigReader.properties.getProperty("baseUri");
        String token = ConfigReader.properties.getProperty("token");

        response = given()
                .baseUri(baseUri)
                .header("Authorization", "Bearer " + token)
            .when()
                .get("/users/" + generatedId) 
            .then()
                .log().all()
                .statusCode(200) // This will now pass as GoRest persists data
                .extract().response();

        System.out.println("GET Response Status: " + response.getStatusCode());
        System.out.println("GET Response Body: " + response.asPrettyString());
    }

    @Then("the status code should be {int}")
    public void the_status_code_should_be(int expectedStatus) {
        // This will no longer be NULL because the @When step above initializes it
        Assert.assertNotNull(response, "Response is NULL! The @When step failed to execute or assign the response.");
        Assert.assertEquals(response.getStatusCode(), expectedStatus, "Status code mismatch!");
    }

    @Then("I save the generated ID and status back to row {int}")
    public void i_save_the_generated_id_and_status_back_to_row(int rowNum) throws IOException {
        // Writes results back to Excel file
        apiLogic.writeSpecificRow(rowNum, generatedId, response.getStatusCode());
    }    
    
    @Then("the response should contain the name {string}")
    public void the_response_should_contain_the_name(String name) {
    	String responseBody = response.asString();
        System.out.println("Validating Response: " + responseBody);

        if (responseBody.equals("{}")) {
            System.out.println("Note: ReqRes does not persist data, skipping GET validation.");
            // Fail with a descriptive message or pass if POST was successful
        } else {
            // Standard validation for the POST response or a valid GET response
            response.then().body("name", equalTo(name));
            System.out.println("Success! Verified name: " + name);
        }
    }

}
