# Web-Quiz-Engine
A Spring multi-user web service for creating and solving quizzes.

### Technology stack: ###

1) Spring Boot, Data, Security, MVC
2) Hibernate, H2 Database
3) Apache Tomcat
4) Rest API
5) Thymeleaf
6) JSON
7) Project Lombok

The server side of the application for creating and solving a quiz with registration, authentication and user authorization. 
The user can upload and delete their questions, solve any questions available in the database. The history of completed quizzes is saved in the database.
At the current stage, data exchange takes place via JSON. A user interface can be connected if necessary.

Application work on http://localhost:8889  
Username and password for the administrator are set in the application configuration:  
Username: admin   
Password: password

### The following endpoints exist: ###

### User controller ###

**POST /api/register**  
(All users permission) Receives a JSON object with two fields: email (string), and password (string). 
If a user with a specified email does not exist, the program saves (registers) the user in a database and responds with 200 (Ok). 
If a user is already in the database, respond with the 400 (Bad Request) status code. 
Both fields are required and must be valid: email should contain @ and . symbols, password should contain at least 5 characters and shouldn't be blank.
If the fields do not meet these restrictions, the service should respond with 400 (Bad Request).  
JSON Example:  
```json
{
  "email": "test@gmail.com",
  "password": "secret"
}
```

**GET /api/{id}**  
(All authenticated permission) Response JSON witn the User from database by ID.
If the User is not found in the database, respond with the 404 (Not Found) status code. 

**DELETE /api/deleteuser/{id}**  
(Only Admin role permission) Removes the user and all his Questions from the database, respond with the 200 (OK) status code. 
If a user with a specified id does not exist, respond with the 404 (Not Found) status code. 
If the request was made by a non-Admin role, respond with the 403 (Forbidden) status code.

### Question controller ###

**POST /api/quizzes**  
(All authenticated permission) Saves the new recipe to the database, respond with new JSON with Question and id in database.  
JSON Example:  
```json
{
  "title": "Coffee drinks",
  "text": "Select only coffee drinks.",
  "options": ["Americano","Tea","Cappuccino","Sprite"],
  "answer": [0,2]
}
```
Sample response:  
```json
{
  "id": 1,
  "title": "Coffee drinks",
  "text": "Select only coffee drinks.",
  "options": ["Americano","Tea","Cappuccino","Sprite"]
}
```

**GET /api/quizzes/{id}**  
(All authenticated permission) Response JSON witn the Question from database without answers. 
If the question is not found in the database, respond with the 404 (Not Found) status code.  
Sample response:  
```json
{
  "id": 1,
  "title": "Coffee drinks",
  "text": "Select only coffee drinks.",
  "options": ["Americano","Tea","Cappuccino","Sprite"]
}
```

**DELETE /api/quizzes/{id}**  
(Only authenticated user, owner of this question) Delete the recipe from the database, respond with the 204 (No Content) status code.
If the question is not found in the database, respond with the 404 (Not Found) status code.
If the user is not the author of the question, respond with the 403 (Forbidden) status code.

**GET /api/quizzes**  
(All authenticated permission) Response JSON with all Questions from database paginated.
The default number of questions is 10.  
The page number can be set using the parameter "page".  
The number of questions per page can be asked using the parameter "pageSize".  
For example: /api/quizzes?page=1&pageSize=2

**POST /api/quizzes/{id}/solve**  
For solwing Question. Takes a JSON with list of answers and check it.  
The resolved issue is saved to the database with the user.  
JSON with answers example:  
```json
{
  "answer": [0,2]
}
```
Sample response if answer correct:  
```json
{
  "success": true, "feedback": "Congratulations, you're right!"
}
```
Sample response if answer wrong:   
```json
{
  "success": false, "feedback": "Wrong answer! Please, try again."
}
```

**GET /api/quizzes/completed**
(Only authenticated user) Response JSON with resolved Questions for this User from database paginated.  
The default number of questions is 10.  
The page number can be set using the parameter "page".  
The number of questions per page can be asked using the parameter "pageSize".  
For example: /api/quizzes/completed?page=0&pageSize=10  
