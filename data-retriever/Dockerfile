FROM maven:3.5-jdk-8-alpine

# Required for starting application up.
RUN apk update && apk add bash

RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app

COPY . $PROJECT_HOME

WORKDIR $PROJECT_HOME

RUN mvn install -DskipTests

EXPOSE 8989

CMD ["java", "-Dspring.data.mongodb.uri=mongodb://db:27017/reddit","-Djava.security.egd=file:/dev/./urandom","-jar","./target/reddit-consumer-1.0.jar"]