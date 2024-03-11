FROM gradle:8.6.0-jdk17 AS gradle
EXPOSE 8080
COPY --chown=gradle:gradle . /home/gradle/
WORKDIR /home/gradle/
RUN gradle bootJar
CMD ["java", "-jar", "build/libs/mikrotikParser-1.jar"]