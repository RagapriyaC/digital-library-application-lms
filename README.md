# Digital Library Application
# Prerequisites:
# Application Required :
1. Eclipse IDE 
2. Postman
3. MySQL
# Database Configuration:
Open application.properties in Spring Boot project and ensure the database configuration matches the provided details or modify the details based on your Mysql database Username and password.
1. spring.datasource.url=jdbc:mysql://localhost:3306/library_system
2. spring.datasource.username=root
3. spring.datasource.password=Raga1
4. Make sure MySQL is running, and the database library_system exists.
# Running the Application:
1. Download the project from github and import as existing maven project.
2. Do maven build and check for any compilation errors.
3. Right-click on the main class (the one with the main method) and select "Run As" > "Java application”
4. The Spring Boot application will start, and you should see log messages indicating that the application has started successfully.
# Interacting with API Endpoints:
Use Postman to interact with the provided RESTful endpoints.
# Book Management Endpoints :
# Data Validation for Book :
Please provide proper test data which passes all attribute validation as given below
1. Book Title : cannot be empty/null and should have at least 2 characters
2. Author Name : cannot be empty/null and should have at least 3 characters
3. Publication year : cannot be null , must be greater than or equal to 1000 and less than or equal to 2024
4. ISBN : cannot be empty/null and must be a 13-digit number

# GET All Books:
Endpoint: GET http://localhost:8080/library/api/books
Retrieve a list of all books.
# GET Book by ID:
Endpoint: GET http://localhost:8080/library/api/books/{id}
Retrieve details of a specific book by ID.
# POST Add Book:
Endpoint: POST http://localhost:8080/library/api/books
Add a new book to the library.
# PUT Update Book:
Endpoint: PUT http://localhost:8080/library/api/books/{id}
Update an existing book's information.
# DELETE Remove Book:
Endpoint: DELETE http://localhost:8080/library/api/books/{id}
Remove a book from the library.
# Patron Management Endpoints:
# Data Validation for Patron :
Please provide proper test data which passes all attribute validation as given below
1. Patron Name : cannot be empty/null and should have at least 3 characters
2. Contact Number : cannot be empty/null and should have at least 9 characters
# GET All Patrons:
Endpoint: GET http://localhost:8080/library/api/patrons
Retrieve a list of all patrons.
# GET Patron by ID:
Endpoint: GET http://localhost:8080/library/api/patrons/{id}
Retrieve details of a specific patron by ID.
# POST Add Patron:
Endpoint: POST http://localhost:8080/library/api/patrons
Add a new patron to the system.
# PUT Update Patron:
Endpoint: PUT http://localhost:8080/library/api/patrons/{id}
Update an existing patron's information.
# DELETE Remove Patron:
Endpoint: DELETE http://localhost:8080/library/api/patrons/{id}
Remove a patron from the system.
Borrowing Endpoints:
# POST Borrow Book:
Endpoint: POST http://localhost:8080/library/api/borrow/{bookId}/patron/{patronId}
Allow a patron to borrow a book.
# PUT Return Book:
Endpoint: PUT http://localhost:8080/library/api/return/{bookId}/patron/{patronId}
Record the return of a borrowed book by a patron.
# JUnit and Code Coverage
To ensure the quality of the application code, JUnit tests have been implemented for various components including entities, services and controllers. Code coverage reports are generated using the JaCoCo plugin and checked locally.
Jacoco report can be seen in below path using browser:
file://Your-project-directory/library-management-system/target/site/jacoco/index.html

1. Overall code coverage achieved: 98.6 %
2. Code coverage for Java main components: 99.4%

![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/9473b55d-5976-4166-af01-4aba181208cd)

![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/d22f30da-fee0-44ef-8e7b-d3231fea3a6d)


# Testing the APIs:
1. Open Postman.
2. Use the provided endpoints to send requests and interact with the application.
3. For example, you can send a GET request to http://localhost:8080/library/api/books to retrieve a list of all books.
   
# Testing screenshots having request and response:
# Add a Book :
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/bb94548b-4679-4423-8689-79889bc4c2c0)
# Update a book by ID:
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/a6c48e29-7969-4247-b7ae-bf78ec5dda2b)
# Retrieve a book by ID :
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/8574e80e-f40c-4583-9ed8-13a1e62ad966)
# Retrieve all books :
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/a2a5312d-e7ed-488c-b51a-53c06da46cb2)
# Delete a book by ID :
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/9ca428dd-f1fb-4e89-b39f-1edcf1a61dc4)
# Data validation while adding a book:
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/b3bf683d-06d5-4ab2-a1c1-9148119850b7)
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/208f20c6-117d-450a-b49e-d93e9d9a16ed)
# Patron
# Adding a Patron:
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/79109311-b758-4419-accd-a5dbd88b5ec8)
# Retrieve all Patrons:
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/9fe2a7d1-cc08-4496-8c96-25131231451b)
# Update a Patron By Id:
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/17f06523-b631-4b3b-a5ea-a32e58626ab8)
# Retrieve a Patron By Id:
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/0026a366-f71f-4e8b-9f68-04a31a842f9c)
# Delete a Patron By Id :
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/f86be1bf-ca62-4a28-baa3-7c941cbddcf5)
# Data validation while adding a Patron:
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/eeb2f230-f547-4e6b-9de3-09d700c510a1)
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/ada8480b-5dcd-4a8c-b638-2172ab511698)
# Borrow / Return Records :
# Borrow a book :
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/19dbdeb5-6c86-4cb2-aef7-96796e98108c)
# Response if book is already borrowed and not returned by Patron :
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/a43e076d-95e1-400b-82d9-da6df6db421e)
# Return a book :
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/12e95093-dff8-4bb6-8318-0ed85ee46c4e)
# Response if book is not borrowed and trying to return :
![image](https://github.com/RagapriyaC/library-management-system/assets/157902475/62418fc8-75b9-4be8-9209-81a007bc1711)

# Front End UI :
# URL to launch Digital Library HomePage:
http://localhost:8080/library

# HomePage:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/9703ea9b-c5e1-4bb2-8dee-df5c70d5f9fa)

# Dashboard:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/bffc6327-7912-427a-9cae-dc166c2f4002)

# View Book Details:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/ba2444dc-99b5-4072-8ce4-393432c9adb8)

# Add new Book Form:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/07705ab1-84f1-4add-8aee-4a4cf6413a7c)

# Edit Existing Book Form:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/3ffff7d5-1486-4eea-9e1d-dd239b3c215d)

# View specific Book Details page:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/329da3c2-2eb0-4703-8c9a-f8b139970c47)

# View Patron Details:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/4f270a84-7c0f-4b4e-be31-dfeee6d91a5b)

# Add new Patron Form:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/6ece63e3-28b0-43aa-827f-354094268982)

# Edit Existing Patron Form:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/1ed47714-b4cd-4537-bb94-3d848be8bffa)

# View specific Patron Details page:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/adfb3e6c-32e9-4a8c-8ba9-9e27255894ca)

# View Borrowing record details:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/49c16026-e547-4e06-a6be-86d9b1930065)

# Borrow Book :
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/7c6ae56e-7745-4d44-89b5-839e56662e3d)

# Return Book :
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/b5f27a9b-9e91-4317-8a38-5142353e6e5c)

# Page when Borrow book is success:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/4049d773-ec7c-47d4-b2e3-bfc4abea543e)

# Page when return book is success:
![image](https://github.com/RagapriyaC/digital-library-application/assets/157902475/bf30a64d-a007-47d1-b2d5-63d3109fae3c)
