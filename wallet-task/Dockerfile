FROM openjdk:8-jdk-alpine
VOLUME /tmp
VOLUME /etc/localtime:/etc/localtime
COPY  build/libs/cl-task-0.0.1-SNAPSHOT.jar /mnt/cl-task-0.0.1-SNAPSHOT.jar
COPY  src/main/resources/application.yml /mnt/application.yml
ENV PORT 10033
EXPOSE $PORT
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dserver.port=${PORT}","-jar","/mnt/cl-task-0.0.1-SNAPSHOT.jar"]