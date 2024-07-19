# Gaming Dashboard
Implementation of a module(sub-part of a gaming service) that keeps track of the all-time top scores.
## Table of Contents
- [Functional Requirements](#functional-requirements)
- [API Spec](#api-spec)
- [High Level System Design](#high-level-system-design)
- [Working Code Snippets](#working-code-snippets)
- [Improvements](#improvements)

### Functional Requirements
 
There are 2 services-
1. Game service (which has the game-related logic)
  - As the focus is not on the game service I have added a dummy logic for the game service. 
  - There 2 API endpoints associated with this service
      - create-player -> User will provide userID and name(optional). Validations have been added for the provided userId.
      - play-game -> User will provide their userId and in the response will receive a score.
2. Top score service
  - This service will provide the top 5 scorers at any time. User ID, score, and name. (N=5 is config driven)
  - API endpoint is created for this service to get the top score.


  ### API Spec
####   POST /play-game
<img width="817" alt="Screenshot 2024-07-19 at 6 03 03 AM" src="https://github.com/user-attachments/assets/d1d1018b-63ea-421e-b60e-b1d81899e522">

  
#### POST /create-player
<img width="816" alt="Screenshot 2024-07-19 at 6 03 20 AM" src="https://github.com/user-attachments/assets/1b172bf0-71df-4479-980d-44ba23e7245a">


####   GET /top-score
  <img width="817" alt="Screenshot 2024-07-12 at 8 28 44 PM" src="https://github.com/user-attachments/assets/51627fe1-5aea-4915-a907-bb88c594ce4c">

<br>

### High Level System Design

<img width="941" alt="Screenshot 2024-07-19 at 5 55 50 AM" src="https://github.com/user-attachments/assets/c97c618a-78de-41ef-8ba4-b427f5aa1363">


Database: MySQL <br>
Caching capability: Max Heap<br>
Message queue: Kafka (Consumer and Producer)<br>
Coding language and framework: Java & Springboot<br>

### Working Code Snippets

#### /create-player
<img width="1039" alt="Screenshot 2024-07-19 at 6 28 50 AM" src="https://github.com/user-attachments/assets/f2893bd6-1aef-4b78-b85e-2ddd6ebcd420">


<br>
If the userId is empty
<img width="1035" alt="Screenshot 2024-07-19 at 6 26 49 AM" src="https://github.com/user-attachments/assets/770b4268-cffe-4cd6-ab81-c6ac264b2987">

#### /play-game
<img width="1037" alt="Screenshot 2024-07-19 at 6 25 39 AM" src="https://github.com/user-attachments/assets/d5e22814-1176-4123-a685-bc5f7905d7d4">

<br>
Invalid UserId
<img width="1028" alt="Screenshot 2024-07-19 at 6 21 53 AM" src="https://github.com/user-attachments/assets/39f31d9b-63c8-44f1-a02b-a6718f9f6279">


#### /top-score
![Screenshot 2024-07-11 at 9 03 57 AM](https://github.com/user-attachments/assets/76cb7cf8-ef16-48a2-a813-ba862b40f5b4)

### Improvements


- In place of a heap, we can have a key: value store just like a cache. There should be a service that is scheduled to run after every fixed duration, fetches the top 5 scores from the DB, and updates the cache.
- Rather than userId being the primary key for the db we should have an id (gets auto incremented) and that should be the primary key. 
- Player creation service/functionality that will create a new player as well as validate whether the user has provided a valid user id and name.

