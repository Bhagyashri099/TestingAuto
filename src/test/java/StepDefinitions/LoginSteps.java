package StepDefinitions;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import DataSetup.ExcelReader;
import DataSetup.LoggerClass;
import POM_Pages.IxigoHomePage;
import POM_Pages.LoginPage;
import TestSetup.CustomPDFStripper;
import TestSetup.Hooks;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;


public class LoginSteps 
{
	LoginPage login;
	WebDriver driver= Hooks.driver;
	PDDocument document;
	private static final Logger log = LogManager.getLogger(LoggerClass.class);

	@Given("user opens the url")
	public void user_opens_the_url() throws InterruptedException 
	{
		//this.driver = Hooks.driver; 
		login=new LoginPage(driver);
		

	}

	@Given("User is on the ixigo homepage")
	public void navigateToIxigo() throws InterruptedException, IOException {
		//	driver.get("https://www.ixigo.com/flights");
		//		driver.manage().window().maximize();
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
		//login.click_on_continueBtn();
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



}
