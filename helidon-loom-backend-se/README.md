# helidon-loom-backend-se1

Minimal Helidon SE project suitable to start from scratch.

## Build and run


With JDK17+
```bash
mvn package
java -jar target/helidon-loom-backend-se1.jar
```

## Exercise the application
```
curl -X GET http://localhost:8080/simple-greet
{"message":"Hello World!"}
```



## Building the Docker Image
```
docker build -t helidon-loom-backend-se1 .
```

## Running the Docker Image

```
docker run --rm -p 8080:8080 helidon-loom-backend-se1:latest
```

Exercise the application as described above.
                                
