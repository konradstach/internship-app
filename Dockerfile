FROM openjdk:10-jre-slim
COPY ./target/internship-app-0.0.1-SNAPSHOT.jar /usr/src/app/
WORKDIR /usr/src/app
EXPOSE 8080
CMD ["java", "-jar", "internship-app-0.0.1-SNAPSHOT.jar"]