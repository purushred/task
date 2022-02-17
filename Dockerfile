FROM arm64v8/openjdk:11-jre
WORKDIR /opt/app
ARG JAR_FILE=target/task-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
