FROM openjdk:8


RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app

COPY build/libs/agenda-prod-1.0.jar $PROJECT_HOME/agenda-prod-1.0.jar

WORKDIR $PROJECT_HOME


CMD ["java", "-Dspring.data.mongodb.uri=mongodb://springboot-mongo:27017/springmongo","-Djava.security.egd=file:/dev/./urandom","-jar","./agenda-prod-1.0.jar"]