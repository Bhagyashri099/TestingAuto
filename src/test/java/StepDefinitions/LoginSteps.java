package StepDefinitions;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

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


public class LoginSteps 
{
	LoginPage login;
	WebDriver driver;
	PDDocument document;
	 PostRequest apiLogic = new PostRequest(); 
	    Response response;
	    String generatedId;

	private static final Logger log = LogManager.getLogger(LoggerClass.class);

	@Given("user opens the url")
	public void user_opens_the_url() throws InterruptedException 
	{
		this.driver = Hooks.driver; 
		login=new LoginPage(driver);
		

	}

	@Given("User is on the ixigo homepage")
	public void navigateToIxigo() throws InterruptedException, IOException {
		//	driver.get("https://www.ixigo.com/flights");
		//		driver.manage().window().maximize();
		if (this.driver == null) {
	        this.driver = Hooks.driver;
	    }
		Thread.sleep(3000);
		login=new LoginPage(driver);
		login.click_login_signup_button();
		
	}
	@When("user enters mob_no <mob_no> and click continue")
	public void user_enters_mob_no_mob_no_and_click_continue() throws IOException, InterruptedException {
		login.enter_username_mob_no();
		login.click_continue_for_login();
	}

	@When("User enters {string} as source and {string} as destination")
	public void enterCities(String src, String dest) throws InterruptedException {
		login.enterSource(src);
		login.enterDestination(dest);
	}

	@And("User clicks on the search button")
	public void performSearch() {
		login.clickSearch();
	}

	@Then("Flight search results should be displayed")
	public void verifyResults() throws InterruptedException {
		Thread.sleep(5000);
		Assert.assertTrue(driver.getCurrentUrl().contains("search"));
		//driver.quit();
	}

	@When("user clicks on BookBtn")
	public void user_clicks_on_book_btn() throws InterruptedException {
		login.click_on_bookBtn();    
		login.click_on_continueBtn();
		//login.selectFreeCancellation();
	}
	



//Read PDF

    @Given("user has the PDF file {string}")
    public void loadPDF(String fileName) throws IOException {
    	log.info("--- Starting PDF Validation Process ---");
    	
    	document = PDDocument.load(new File("src/test/resources/" + fileName)); // Load PDF
    }

    @When("user validates all pages of the PDF")
    public void validateDetails() throws IOException {
        int pageCount = document.getNumberOfPages(); // Get page count
     
        for (int i = 1; i <= pageCount; i++) {
        	log.info("Validating Page: " + i);
            CustomPDFStripper stripper = new CustomPDFStripper();
            stripper.setStartPage(i);
            stripper.setEndPage(i);
            String pageText = stripper.getText(document);
           
            // logic to compare pageText with Excel row 'i'
        }
   }
    @Then("all text, font types, and colors should match the test data sheet")
    public void validate_pdf_against_excel() throws IOException {
        for (int i = 1; i <= document.getNumberOfPages(); i++) {
            Map<String, String> expected = excelData.get(i - 1);
            
         String expectedText = expected.get("ExpectedText");
        
        
            CustomPDFStripper stripper = new CustomPDFStripper();
            stripper.setStartPage(i);
            stripper.setEndPage(i);

            // It "pumps" the data into variables
            String fullPageText = stripper.getText(document); 

            // 2. Now the variable is no longer empty
            String actualFont = stripper.getLastFontName();
            
            String actualColor = stripper.getLastColor();
           //logs to see validation
            
            System.out.println("--- Validating Page " + i + " ---");
            System.out.println("Actual Text: " + fullPageText.trim());
            System.out.println("Actual Color: " + actualColor);
            System.out.println("Expected Text:" +expectedText);
            
        }
    }
    List<Map<String, String>> excelData;

    @When("test data is loaded from {string}")
    public void test_data_is_loaded_from(String fileName) {
        // Calling existing readSheet method
         excelData = ExcelReader.readSheet(fileName, "Sheet1");
        
        // verify data isn't empty
        if (excelData.isEmpty()) {
            throw new RuntimeException("Excel file is empty or sheet not found: " + fileName);
        }
        System.out.println("Successfully loaded " + excelData.size() + " rows of test data.");
    }
    
    

//API Logic 
//    private String baseUri;
//    @Given("I have the Excel sheet ready at row {string}")
//    public void i_have_the_excel_sheet_ready_at_row(String rowNum) throws IOException {
//        // Loads the data from Excel file via PostRequest page class
//        apiLogic.getTestData(); 
//    }
//    //
//
//        @When("I send a POST request using data from Excel row {string}")
//    public void i_send_a_post_request_using_data_from_excel_row(String rowNumStr) throws IOException, InterruptedException {
//        	ConfigReader.loadProperties();
//        	String baseUri = ConfigReader.properties.getProperty("baseUri");
//            String token = ConfigReader.properties.getProperty("token3");
//          //  String envHeaderValue = ConfigReader.properties.getProperty("env_header");
//
//        	int rowNum = Integer.parseInt(rowNumStr);
//        	Map<String, Object> innerData = new HashMap<>();
//            innerData.put("color", "Cloudy White");
//            innerData.put("capacity", "128 GB");     
//            Map<String, String> testData = apiLogic.getDataByRow(rowNum);
////
////       testData.put("gender", "male"); 
////       testData.put("status", "active");
////        testData.put("email", "user_" + System.currentTimeMillis() + "@example.com");
////        
////        if(testData.containsKey("FirstName")) {
////            testData.put("name", testData.get("FirstName")); 
////        }
//        Map<String, Object> payload = new HashMap<>();
//        payload.put("data", innerData);
//        response = given()
//            .baseUri(baseUri)
//           
//         //   .header("x-api-key", token) 
//            //.header("X-Reqres-Env", envHeaderValue)
//
//           .header("Authorization", "Bearer " + token) 
//            .contentType("application/json")
//            .body(innerData)
//        .when()
//            .post("/objects")
//        .then()
//            .log().all()
//            .extract().response();
//
//        if (response.getStatusCode() == 201) {
//            generatedId = response.jsonPath().getString("id");
//            System.out.println("POST Successful. Generated ID: " + generatedId);
//            Thread.sleep(1000);
//        } else {
//            System.out.println("POST Failed with Status: " + response.getStatusCode());
//        }
//    }
//    @When("I send a GET request using the extracted ID")
//    public void i_send_a_get_request_using_the_extracted_id() {
//        System.out.println("Fetching persistent data for ID: " + generatedId);
//        String baseUri = ConfigReader.properties.getProperty("baseUri");
//        String token = ConfigReader.properties.getProperty("token3");
//
//        response = given()
//                .baseUri(baseUri)
//                .header("x-api-key", token) 
//                //.header("Authorization", "Bearer " + token)
//            .when()
//                .get("/" + generatedId) 
//            .then()
//                .log().all()
//                .statusCode(201) 
//                .extract().response();
//
//        System.out.println("GET Response Status: " + response.getStatusCode());
//        System.out.println("GET Response Body: " + response.asPrettyString());
//    }
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
//        // Writes results back to Excel file
//        apiLogic.writeSpecificRow(rowNum, generatedId, response.getStatusCode(), generatedId);
//    }    
//    
//    @Then("the response should contain the name {string}")
//    public void the_response_should_contain_the_name(String name) {
//    	String responseBody = response.asString();
//        System.out.println("Validating Response: " + responseBody);
//
//        if (responseBody.equals("{}")) {
//            System.out.println("Note: ReqRes does not persist data, skipping GET validation.");
//            // Fail with a descriptive message or pass if POST was successful
//        } else {
//            // Standard validation for the POST response or a valid GET response
//            response.then().body("name", equalTo(name));
//            System.out.println("Success! Verified name: " + name);
//        }
//    }
//
//    
//    //tesr
//    @Given("I have the API base URI {string}")
//    public void setBaseUri(String uri) {
//        this.baseUri = uri;
//    }
//
//    @When("I send a POST request to {string} with name {string} and job {string}")
//    public void sendPostRequest(String endpoint, String name, String job) {
//    	Map<String, Object> innerData = new HashMap<>();
//        innerData.put("color", "white");
//        innerData.put("capacity", "128 GB");
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("name", name);
//        body.put("data", innerData);
//
//        response = given()
//            .baseUri(baseUri)
//            //.contentType(ContentType.JSON)
//            .header("User-Agent", "Mozilla/5.0") // Bypasses bot detection
//            .body(body)
//        .when()
//            .post(endpoint);
//            
//        System.out.println("POST Response: " + response.asPrettyString());
//    
//    }
//
//    @And("I send a GET request for the same ID to {string}")
//    public void sendGetRequest(String endpoint) {
//        response = given()
//            .baseUri(baseUri)
//            .header("User-Agent", "Mozilla/5.0")
//        .when()
//            .get(endpoint + "/" + generatedId); // Results in /objects/uuid
//            
//        response.then().log().all();
//    }

}
