FROM openjdk:17

WORKDIR /app

COPY /target/rampup-0.0.1-SNAPSHOT.jar .

ENV DBHOST=rampup-db

EXPOSE 8080

CMD [ "java", "-jar", "rampup-0.0.1-SNAPSHOT.jar" ]