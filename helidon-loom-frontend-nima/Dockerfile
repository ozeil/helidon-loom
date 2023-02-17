
# 1st stage, build the app
FROM maven:3.8.7-eclipse-temurin-19 as build

COPY maven-proxy-docker/settings.xml /root/.m2/settings.xml

WORKDIR /helidon

# Create a first layer to cache the "Maven World" in the local repository.
# Incremental docker builds will always resume after that, unless you update
# the pom
ADD pom.xml .
RUN mvn package -Dmaven.test.skip -Declipselink.weave.skip

# Do the Maven build!
# Incremental docker builds will resume here when you change sources
ADD src src
RUN mvn clean package -DskipTests

RUN echo "done!"

# 2nd stage, build the runtime image
FROM eclipse-temurin:19
WORKDIR /helidon

# Copy the binary built in the 1st stage
COPY --from=build /helidon/target/helidon-loom-frontend-nima.jar ./
COPY --from=build /helidon/target/libs ./libs

CMD ["java", "--enable-preview", "-Djdk.tracePinnedThreads=full", "-XX:StartFlightRecording=dumponexit=true,filename=/home/jfr/", "-jar", "helidon-loom-frontend-nima.jar"]

EXPOSE 8080
