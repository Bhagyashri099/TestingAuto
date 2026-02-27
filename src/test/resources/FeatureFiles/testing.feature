Feature: User Management API
   @not @skip_setup1
  Scenario Outline: Create multiple users and log results to Excel
    Given I have the Excel sheet ready at row "<rowNum>"
    When I send a POST request using data from Excel row "<rowNum>"
    Then the status code should be 201
    When I send a GET request using the extracted ID
    Then the response should contain the name "John"
    
    Examples:
    |rowNum|
    |1|