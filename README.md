StockTrackerServer - Backend for my StockTracker Android app

------------------------------------------------------------------------------------------
For this project l have used MarketStack API to get my data. You can check their API here:
- https://marketstack.com/documentation

What is used in this project:
1. Libraries
  - OpenFeign clients to access remote API and export data to JSONs
  - Spring Batch for saving a lot of records fast
  - Spring Data JPA
  - PostgreSQL DB
  - JUnit for testing
  - Testcontainer for testing
  - Custom exception handling
  - Dockerization
  - Kubernetes secret and configmap
  - Spring profiles
2. Good practices
  - Controller / Service / Repository defined structures with interface implementations
  - DTO structures
  - Custom configurators for remote API data to suit my Android app better
  - Documentation for almost every class and function

To implement in future:
- Pagination
- Swagger UI integration
- Spring Security
- JWT / OAuth2 authorization
- Multi modular approach for remote API and my API structures
- To use real time data API

VM options command before running the server:
-Dspring.datasource.password=... -Dspring.datasource.username=... -DapiKey=...

------------------------------------------------------------------------------------------
Android app repository:
https://github.com/MartinKoleff/StockTracker

Diagram for DB relations:
![databases relations diagram](https://github.com/MartinKoleff/StockTrackerServer/assets/52703399/6fb0fa6a-341b-4461-aa66-fe55e7defc83)


