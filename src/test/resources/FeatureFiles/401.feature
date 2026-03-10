Feature: test
    
    @skip_setup1
  Scenario: Create multiple users and log results to Excel
    the API endpoint is "https://reqres.in/api/collections/users"
    When I send a POST request using data
   Then the response status code should be 401
    
    
   
