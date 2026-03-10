package TestSetup;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import DataSetup.ConfigReader;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks
{
	public static WebDriver driver;
	ConfigReader Authconfig = new ConfigReader();

	@Before("not @skip_setup1")
	public void setUp(Scenario scenario) throws MalformedURLException {
	    boolean headless = scenario.getSourceTagNames().contains("@headless");
	    String env = Authconfig.getExecutionEnv().trim();
	    
	    // 1. Always create the options object first
	    ChromeOptions options = new ChromeOptions();

	    // 2. Add headless argument to that object if tag is present
	    if (headless) {
	        options.addArguments("--headless=new"); // Use 'new' for modern Chrome versions
	        options.addArguments("--window-size=1920,1080"); // Recommended for headless
	    }

	    if (env.equalsIgnoreCase("remote")) {
	        options.setCapability("browserName", Authconfig.getBrowser());
	        driver = new RemoteWebDriver(new URL(Authconfig.getRemoteUrl()), options);
	    } else {
	        // 3. Pass the 'options' object here!
	        driver = new ChromeDriver(options); 
	    }

	    driver.manage().window().maximize();
	    driver.get(Authconfig.getUrl());
	}


	@After("not @skip_setup1")
	public void tearDown(Scenario scenario) {
	    if (scenario.isFailed()) {
	        // This attaches the screenshot directly to your Extent Report
	        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	        scenario.attach(screenshot, "image/png", "Failed_Screenshot");
	    }
	}
	@After ("not @skip_setup1")
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}}
	
	


	}


