## Authentication with generating tokens with JWT
```
This project was created to demostrated how to build a basic project that simule a login and register User generating a token to access to protected routes.
```

## Application properties
```
My recomendation for connect to the DB is that you create a DB in a cloud service. NEON is cloud-native Postgres solution designed for modern applications.
```
## Endpoints
```
To register users
POST http://localhost:8080/auth/register

{
    "username": "username",
    "password": "password",
    "firstname": "firstname",
    "lastname": "lastname",
    "country": "country"    
}
```
```
To login users
POST http://localhost:8080/auth/login

Request Body:
{
    "username": "username",
    "password": "password",
}
```
```
To access an a private route
POST http://localhost:8080/api/admin/value

You should put the token in the section of Authorization and select Bearer Token (e.g, using Postman)
```


