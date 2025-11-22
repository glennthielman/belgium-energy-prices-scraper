FROM eclipse-temurin:21-ubi9-minimal
COPY dynamic-webscraper*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]