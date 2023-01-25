# helidon-loom-frontend-mp

Minimal Helidon MP project suitable to start from scratch.

## Build and run


With JDK17+
```bash
mvn package
java -jar target/helidon-loom-frontend-mp.jar
```

## Exercise the application
```
curl -X GET http://localhost:8080/simple-greet
{"message":"Hello World!"}
```



## Building the Docker Image
```
docker build -t helidon-loom-frontend-mp .
```

## Running the Docker Image

```
docker run --rm -p 8080:8080 helidon-loom-frontend-mp:latest
```

Exercise the application as described above.
                                
