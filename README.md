# Servlets API

A REST api made in java using only servlets components and JDBC. 

## Running The Application

For running this API, its necessary to have docker engine and 
docker compose pre-installed on your machine.

Just run this command on the root folder:
```
    docker compose up -d
```

This will start the services (nginx, backend and database). The server will 
start listening http requests on http://localhost (default 80 port).

## API Endpoints
- GET http://localhost/users?page=number&itemsPerPage=number
- POST http://localhost/users
- PUT http://localhost/users/:id
- DELETE http://localhost/users/:id

## Body requests

POST http://localhost/users
```json
{
  "username": "antoniovictor123",
  "email": "antoniovictor123@example.com",
  "password": "securepassword123"
}
```
<hr />

PUT http://localhost/users/:id
```json
{
  "username": "antoniovictor123",
  "email": "antoniovictor123@example.com",
  "password": "securepassword123"
}
```
