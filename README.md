# Gaming Dashboard
Implementation of a module(sub-part of a gaming service) that keeps track of the all-time top scores.
## Table of Contents
- [Functional Requirements](#functional-requirements)
- [API Spec](#api-spec)
- [High Level System Design](#high-level-system-design)
- [Working Code Snippets](#working-code-snippets)

### Functional Requirements
 
There are 2 services-
1. Game service (which has the game-related logic)
  - As the focus is not on the game service I have added a dummy logic for the game service. 
  - API endpoint is created for this service where a user will provide their username and name(optional) and in the response will receive a score.
2. Top score service
  - This service will provide the top 5 scorers at any time. User ID, score, and name. (N=5 is config driven)
  - API endpoint is created for this service to get the top score.


  ### API Spec
####   POST /play-game
  <img width="817" alt="Screenshot 2024-07-12 at 8 28 02 PM" src="https://github.com/user-attachments/assets/203d1ef6-c7e6-4a7c-9783-db0f9135e299"> 

####   GET /top-score
  <img width="817" alt="Screenshot 2024-07-12 at 8 28 44 PM" src="https://github.com/user-attachments/assets/51627fe1-5aea-4915-a907-bb88c594ce4c">

### High Level System Design

<img width="845" alt="Screenshot 2024-07-12 at 8 31 56 PM" src="https://github.com/user-attachments/assets/1291b8c5-24e3-4515-951e-3d95b5e4bdd3">

Database: MySQL <br>
Caching capability: Max Heap<br>
Message queue: Kafka (Consumer and Producer)<br>
Coding language and framework: Java & Springboot<br>

### Working Code Snippets
![Screenshot 2024-07-11 at 8 37 58 AM](https://github.com/user-attachments/assets/641927c8-81f1-4891-9c8b-3644d3340ed5)

![Screenshot 2024-07-11 at 9 03 57 AM](https://github.com/user-attachments/assets/76cb7cf8-ef16-48a2-a813-ba862b40f5b4)

### Improvements


- In place of a heap, we can have a key: value store just like a cache. There should be a service that is scheduled to run after every fixed duration, fetches the top 5 scores from the DB, and updates the cache.
- Rather than userId being the primary key for the db we should have an id (gets auto incremented) and that should be the primary key. 

