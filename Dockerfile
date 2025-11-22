FROM eclipse-temurin:21-ubi9-minimal
COPY target/dynamic-webscraper*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]