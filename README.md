# Dining review rest api

My solution for Spring Portfolio Project in Codecademy's Skill Path: [Create REST APIs with Spring and Java](https://www.codecademy.com/learn/paths/create-rest-apis-with-spring-and-java).  
  
I completed this Skill Path to familiarize myself with the Java programming language and expand my toolset for creating and testing Rest APIs.

## Table of Contents
- [Overview](#overview)
- [Installation](#installation)
- [Endpoints](#endpoints)
    - [UserController](#usercontroller)
      - [Create a new user](#create-a-new-user)
      - [Get a user by userName](#get-a-user-by-username)
      - [Update user details](#update-user-details)
      - [Delete a user](#delete-a-user)
    - [RestaurantController](#restaurantcontroller)
      - [Create a new restaurant](#create-a-new-restaurant)
      - [Get all restaurants](#get-all-restaurants)
      - [Get a restaurant by id](#get-a-restaurant-by-id)
      - [Get restaurants by zipcode](#get-restaurants-by-zipcode)
      - [Get restaurants by city](#get-restaurants-by-city)
      - [Get restaurants by state](#get-restaurants-by-state)
      - [Search for restaurants by zipcode and allergy](#search-for-restaurants-by-zipcode-and-allergy)
      - [Update a restaurant](#update-a-restaurant)
      - [Delete a restaurant](#delete-a-restaurant)
    - [ReviewController](#reviewcontroller)
      - [Create a new review](#create-a-new-review)
      - [Get a review by id](#get-a-review-by-id)
      - [Get approved reviews by restaurantName](#get-approved-reviews-by-restaurantname)
      - [Get all reviews by userName](#get-all-reviews-by-username)
      - [Get all reviews with pending status](#get-all-reviews-with-pending-status)
      - [Update a review](#update-a-review)
      - [Delete a review](#delete-a-review)
- [Testing](#testing)

## Overview

RESTful web API with data persistence using Spring and Spring Data JPA.

Provides the backend for a Dining review app that allows users to score and leave commentary on restaurants based on how
compatible they are with peanut, egg, or dairy allergies. Newly created reviews must be accepted or rejected by an admin.

## Installation

- To run project locally on Windows:
  - Have [Maven](https://maven.apache.org/) installed.
  - Clone the repository.
  - Run the command `./mvnw clean install`
  - If no errors, run the command `./mvnw spring-boot:start`
- Dependencies used:
  - spring-boot-starter-data-jpa
  - spring-boot-starter-web
  - spring-boot-starter-test
  - H2 database
  - lombok
  - Junit


## Endpoints

### UserController

#### Create a new user
- **Description:** Create a new user. Username must not already exist. Returns the created user.
- **URL:** `/user`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
  "userName": "string",
  "city": "string",
  "state": "string",
  "zipcode": "string",
  "peanutAllergy": "boolean",
  "eggAllergy": "boolean",
  "dairyAllergy": "boolean"
  }
  - **Responses:** 
    - `201 Created: Returns the newly created user.`
    - `400 Bad Request: Username is taken.`


#### Get a user by userName
- **Description:** Returns a user with a specified userName.
- **URL:** `/user/{userName}`
- **Method:** `GET`
- **Responses:**
  - `200 OK: Returns the user.`
  - `404 Not Found: No user was found with that userName.`


#### Update user details
- **Description:** Updates the user details, excluding the userName, of the user with the specified userName. Any submitted changes to userName are ignored. Only the provided fields are updated.
- **URL:** `/user/{userName}`
- **Method:** `PUT`
- **Request Body:**
  ```json
  {
  "city": "string",
  "state": "string",
  "zipcode": "string",
  "peanutAllergy": "boolean",
  "eggAllergy": "boolean",
  "dairyAllergy": "boolean"
  }
- **Responses:**
  - `200 OK: Returns the updated user.`
  - `404 Not Found: No user was found with that userName.`


#### Delete a user
- **Description:** Deletes user with the specified userName.
- **URL:** `/user/{userName}`
- **Method:** `DELETE`
- **Responses:**
    - `204 No Content: Returns the deleted user.`
    - `404 Not Found: No user was found with that userName.`


### RestaurantController

#### Create a new restaurant
- **Description:** Creates a new restaurant. Ensures the restaurantName is unique for a given zipcode. Returns the created restaurant.
- **URL:** `/restaurant`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
  "name": "string",
  "city": "string",
  "state": "string",
  "zipcode": "string",
  "peanutScore": "float",
  "eggScore": "float",
  "dairyScore": "float"
  }
- **Responses:**
    - `201 Created: Returns the newly created restaurant.`
    - `400 Bad Request: restaurantName is taken for the given zipcode.`

#### Get all restaurants
- **Description:** Returns all restaurants. Returns an empty list if no restaurants exist.
- **URL:** `/restaurants`
- **Method:** `GET`
- **Responses:**
    - `200 OK: Returns a restaurant list.`

#### Get a restaurant by id
- **Description:** Returns a restaurant with the given id.
- **URL:** `/restaurants/{id}`
- **Method:** `GET`
- **Responses:**
    - `200 OK: Returns a restaurant.`
    - `404 Not Found: No restaurant was found with the given id.`

#### Get restaurants by zipcode
- **Description:** Returns a list of restaurants with the given zipcode.
- **URL:** `/restaurants/byzipcode/{zipcode}`
- **Method:** `GET`
- **Responses:**
    - `200 OK: Returns a restaurant list.`

#### Get restaurants by city
- **Description:** Returns a list of restaurants with the given city.
- **URL:** `/restaurants/bycity/{city}`
- **Method:** `GET`
- **Responses:**
    - `200 OK: Returns a restaurant list.`

#### Get restaurants by state
- **Description:** Returns a list of restaurants with the given state.
- **URL:** `/restaurants/bystate/{state}`
- **Method:** `GET`
- **Responses:**
    - `200 OK: Returns a restaurant list.`

#### Search for restaurants by zipcode and allergy
- **Description:** Returns a list of restaurants with the given zipcode and allergy, ordered by allergy score descending.
- **URL:** `/restaurants/search`
- **Method:** `GET`
- **Query Parameters:**
  - zipcode (required): 5 digit zipcode.
  - allergy (required): One of `peanut`, `egg`, or `dairy`.
- **Responses:**
    - `200 OK: Returns a restaurant list.`
    - `400 Bad Request: The zipcode or allergy type was invalid`

#### Update a restaurant
- **Description:** Updates a restaurant's details and recalculates the overall score. Only the provided fields are updated.
- **URL:** `/restaurants/{id}`
- **Method:** `PUT`
- **Request Body:**
  ```json
  {
  "name": "string",
  "city": "string",
  "state": "string",
  "zipcode": "string",
  "peanutScore": "float",
  "eggScore": "float",
  "dairyScore": "float"
  }
- **Responses:**
    - `200 OK: Returns the updated restaurant.`
    - `404 Not Found: No restaurant was found with that id.`


#### Delete a restaurant
- **Description:** Deletes a restaurant with the specified id.
- **URL:** `/restaurant/{id}`
- **Method:** `DELETE`
- **Responses:**
    - `204 No Content: Returns the deleted restaurant.`
    - `404 Not Found: No restaurant was found with that id.`


### ReviewController

#### Create a new review
- **Description:** Creates a new review. Returns the created review.
- **URL:** `/reviews`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
  "restaurantId": "long",
  "userName": "string",
  "peanutScore": "float",
  "eggScore": "float",
  "dairyScore": "float",
  "commentary": "string"
  }
- **Responses:**
    - `201 Created: Returns the newly created review.`

#### Get a review by id
- **Description:** Returns a review with the given id.
- **URL:** `/reviews/{id}`
- **Method:** `GET`
- **Responses:**
    - `200 OK: Returns a review.`
    - `404 Not Found: No review was found with the given id.`

#### Get approved reviews by restaurantName
- **Description:** Returns a list of approved reviews for the given restaurantName.
- **URL:** `/reviews/restaurant/{restaurantName}`
- **Method:** `GET`
- **Responses:**
    - `200 OK: Returns a review list.`
    - `404 Not Found: No approved reviews were found for the given restaurantName.`

#### Get all reviews by userName
- **Description:** Returns a list of all reviews for the given userName.
- **URL:** `/reviews/user/{userName}`
- **Method:** `GET`
- **Responses:**
    - `200 OK: Returns a review list.`
    - `404 Not Found: No reviews were found for the given userName.`

#### Get all reviews with pending status
- **Description:** Returns a list of all reviews with a pending status.
- **URL:** `/reviews/admin/`
- **Method:** `GET`
- **Responses:**
    - `200 OK: Returns a review list.`
    - `404 Not Found: No reviews were found with a pending status.`

#### Update a review
- **Description:** Updates a review's scores and/or commentary. Only the provided fields are updated.
- **URL:** `/reviews/{id}`
- **Method:** `PUT`
- **Request Body:**
  ```json
  {
  "peanutScore": "float",
  "eggScore": "float",
  "dairyScore": "float",
  "commentary": "string"
  }
- **Responses:**
    - `200 OK: Returns the updated review.`
    - `404 Not Found: No review was found with that id.`

#### Update the status of a review
- **Description:** Updates a review's status. Sets the new scores for a restaurant if the review is approved. Status must be `approved` or `rejected`.
- **URL:** `/reviews/admin/{id}`
- **Method:** `PUT`
- **Request Body:**
  ```json
  {
  "status": "approved"
  }
- **Responses:**
    - `200 OK: Returns the updated review.`
    - `404 Not Found: No review was found with that id.`
    - `500 Internal Server Error: An error occured while calculating the new scores for the restaurant.`


#### Delete a review
- **Description:** Deletes a review with the specified id.
- **URL:** `/review/{id}`
- **Method:** `DELETE`
- **Responses:**
    - `204 No Content: Returns the deleted review.`
    - `404 Not Found: No review was found with that id.`


## Testing
JUnit and Mockito were used for Unit testing the repositories and controllers. Testing can be found in `dining-review-api/src/test`.



