package RestAssuredTest;

	import io.restassured.RestAssured;
	import java.io.FileInputStream;
	import java.io.IOException;
	import java.util.Properties;

	public class TestSetup {
	    public static void setup() {
	        Properties properties = new Properties();
	        try (FileInputStream input = new FileInputStream("src/test/resources/config.properties")) {
	            // Load the properties file
	            properties.load(input);
	            
	            // Retrieve the value using the key
	            String url = properties.getProperty("base_Url");
	            
	            if (url != null) {
	                // Set RestAssured global baseURI
	                RestAssured.baseURI = url;
	            } else {
	                throw new RuntimeException("base_Url not found in config.properties");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}


