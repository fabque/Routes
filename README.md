# Routes
Calculate the best route between stations

This REST API use Java 22 and Spring boot and Maven

Running command
```
mvn spring-boot:run
```

## Considerations
You can populate Stations and Routes usings its endpoints to create new ones.
On the other hand if you want an small example example you can use the following endpoint 
```
http://localhost:8080/routes/initdb
```

## Build image and run local
Using dockerFile 
Build image using this command
```
docker build -t springio/routes .
```

Run docker image using this command
```
docker run -it -p 8080:8080 springio/routes
```

### Documentation  
You can access at this link
```
http://localhost:8080/api-docs
```
