Feature: NrlsAdapter

@mytag
Scenario Outline: Check API response from NRLS Adapter giving an NHS Number 
	Given I have a <endPoint> and <nhsNumber> 
	When I call the the API
	Then reponse will match the expected <responseCode> and <responseMessage>
	Examples: 
	| endPoint | nhsNumber  | responseCode | responseMessage     |
	| pointers | 9462205957 | 200          | None                |
	| pointers | 9462206031 | 200          | None                |
	| pointers | 9462205965 | 200          | None                |
	| pointers | 9462205655 | 400          | 400 Invalid NHS Number        |
	| pointers | 9462205671 | 400          | 400 Invalid Parameter         |
	| pointers | 9462205833 | 400          | 400 Missing Or Invalid Header |
	| pointers | 9462205701 | 404          | 404 No Record Found           |
	| pointers | 9462205841 | 415          | 415 Unsupported Media Type    |
	| count	   | 9462205922 | 200          | None                          |
	| count	   | 9462205981 | 200          | None                          |
	| count	   | 9462205655 | 400          | 400 Invalid NHS Number        |
	| count	   | 9462205671 | 400          | 400 Invalid Parameter         |
	| count	   | 9462205833 | 400          | 400 Missing Or Invalid Header |
	| count	   | 9462205701 | 404          | 404 No Record Found           |
	| count	   | 9462205841 | 415          | 415 Unsupported Media Type    |