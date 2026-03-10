Feature: API Error Status Code Validation

  Scenario: 404 Not Found - Missing Resource
    Given the API endpoint is "https://jsonplaceholder.typicode.com/users/5678900000"
    When I send a GET request
    Then the response status code should be 404

Scenario: Server encounters an unhandled exception
    Given the API endpoint is "https://gorest.co.in/public/v2/users"
When I send a POST request
    Then the response status code should be 500
    
   Scenario: Create user with invalid data type to trigger 400 error
   Given the API endpoint is "https://gorest.co.in/public/v2/users"
    And I have a "Bearer" token "0dcda9cb3cb7843a5f22e3ce861d388bd56823616c08dc8bf3a720799d8b02e5"
    When I send a POST request to "/users" with an invalid integer for "gender"
    Then the response status code should be 400
    