package RunnerClasss;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features= {"src/test/resources/FeatureFiles"},
glue= {"steps", "StepDefinitions"},
plugin= {"pretty", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
		"json:target/cucumber/cucumber.json",
	    "junit:target/cucumber/cucumber-junit.xml"}
)

 public class TestRunner extends AbstractTestNGCucumberTests{



}


