Feature: test
Scenario Outline: Create and Verify Device Object
  Given I have the API base URI "https://api.restful-api.dev"
  When I send a POST request to "/objects" with name "<name>" and job "<job>"
  And I send a GET request for the same ID to "/objects"
  Then the status code should be 200
  And the response should contain the name "<name>"

  Examples:

    | name             | job          |
    | Google Pixel 9   | QA Testing   |