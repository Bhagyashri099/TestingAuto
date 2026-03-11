Feature: API Error Status Code Validation

 @skip_setup1
  Scenario: 404 Not Found - Missing Resource
    Given the API endpoint is "https://jsonplaceholder.typicode.com/users/5678900000"
    When I send a GET request
    Then the response status code should be 404
    
    @skip_setup1
    Scenario: 401 unAuthorized error 
    Given the API endpoint is "http://reqres.in/api/users"
    When I send a POST request with INVALID API Key
    Then the response status code should be 401
     
    
    @skip_setup1
     Scenario: 500 Internal server error  
    Given the API endpoint is "https://gorest.co.in/public/v2/users"
    When I send a GET request
    Then the response status code should be 500

