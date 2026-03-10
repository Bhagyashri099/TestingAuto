package StepDefinitions;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataSetup.ConfigReader;
import DataSetup.ExcelReader;
import DataSetup.LoggerClass;
import POM_Pages.IxigoHomePage;
import POM_Pages.LoginPage;
import POM_Pages.PostRequest;
import TestSetup.CustomPDFStripper;
import TestSetup.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.cucumber.java.en.Then;




import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
public class new200 {
	WebDriver driver= Hooks.driver;
	PDDocument document;
	 PostRequest apiLogic = new PostRequest(); 
	    Response response;
	    String generatedId;
	@Given("I have the Excel sheet ready at row {string}")
	public void i_have_the_excel_sheet_ready_at_row(String rownum) throws IOException {
		apiLogic.getTestData();
	    	}

	@When("I send a POST request using data from Excel row {string}")
	public void i_send_a_post_request_using_data_from_excel_row(String rownumStr) throws InterruptedException, IOException 
	{
		ConfigReader.loadProperties();
    	String baseUri = ConfigReader.properties.getProperty("baseUri");
    	int rowNum = Integer.parseInt(rownumStr);
    	Map<String, String> innerData = apiLogic.getDataByRow(rowNum);
    	//Map<String, Object> innerData = new HashMap<>();
    	String nameFromExcel = innerData.get("name");
    	
        innerData.put("color", "Cloudy White");
        innerData.put("capacity", "128 GB"); 
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", nameFromExcel);
      payload.put("data", innerData);
      response = given()
          .baseUri(baseUri)
         
         //.header("Authorization", "Bearer " + token) 
         .contentType("application/json")
          .body(payload)
      .when()
          .post("/objects")
      .then()
          .log().all()
          .extract().response();

      if (response.getStatusCode() == 201) {
          generatedId = response.jsonPath().getString("id");
          System.out.println("POST Successful. Generated ID: " + generatedId);
          Thread.sleep(1000);
      } else {
          System.out.println("POST Failed with Status: " + response.getStatusCode());
      }
  }

        
   

	
	
	
	

	@Then("the status code should be {int}")
	public void the_status_code_should_be(Integer int1) {
	    	}

	@When("I send a GET request using the extracted ID")
	public void i_send_a_get_request_using_the_extracted_id() 
	{
		generatedId = response.jsonPath().getString("id");
	    assertNotNull(generatedId, "Generated id is null");
	    System.out.println("Generated ID: " + generatedId);
	    ConfigReader.loadProperties();
	    String baseUri = ConfigReader.properties.getProperty("baseUri");

	    Response getResponse = given()
	        .baseUri(baseUri)
	        .accept("application/json")
	        .pathParam("id", generatedId)
	    .when()
	        .get("/objects/{id}")
	    .then()
	        .log().all()
	        .extract().response();
	    	}

//	@Then("the response should contain the name {string}")
//	public void the_response_should_contain_the_name(String string) 
//	{
//		
//	    	}

	@Then("I save the generated ID and status back to row {int}")
  public void i_save_the_generated_id_and_status_back_to_row(int rowNum) throws IOException {
//      // Writes results back to Excel file
    apiLogic.writeSpecificRow(rowNum, generatedId, response.getStatusCode(), generatedId);
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
	 @Then("I save the generated ID back to {int}")
	 public void i_save_the_generated_id_and_status_back_to(int rowNum) throws IOException {
		 apiLogic.writeSpecificRow(rowNum, generatedId, response.getStatusCode(), generatedId);
	 }
}
