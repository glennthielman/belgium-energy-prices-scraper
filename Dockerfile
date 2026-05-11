FROM gcr.io/distroless/java21-debian12
COPY dynamic-webscraper*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
