FROM java:alpine
ARG JAR_FILE
ADD target/${JAR_FILE} beedo.jar
EXPOSE 8020
ENTRYPOINT ["java","-jar","/beedo.jar","--spring.profiles.active=prod"]