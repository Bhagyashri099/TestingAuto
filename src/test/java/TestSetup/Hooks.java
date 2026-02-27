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
	public void setUp() throws MalformedURLException {
		String env = Authconfig.getExecutionEnv().trim();
		String hubUrl = Authconfig.getRemoteUrl();
		String browser = Authconfig.getBrowser();

		if (env.equalsIgnoreCase("remote")) {
			ChromeOptions options = new ChromeOptions();
			options.setCapability("browserName", browser);

			driver = new RemoteWebDriver(new URL(hubUrl), options);


			// This line executes the script on the remote hub
			//driver = new RemoteWebDriver(new URL(hubUrl), caps);
		} 
		else 

		{
			// This block executes for local testing
			driver = new ChromeDriver(); 
			//System.out.println("local");

		}

			        driver.manage().window().maximize();
			        driver.get(Authconfig.getUrl());
	}   

	@After
	public void tearDown(Scenario scenario) {
	    if (scenario.isFailed()) {
	        // This attaches the screenshot directly to your Extent Report
	        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	        scenario.attach(screenshot, "image/png", "Failed_Screenshot");
	    }
	}
	@After
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}}
	
	


	}


