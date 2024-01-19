FROM openjdk:17

ARG WRITEOPIA_CLOUD_ID

ENV WRITEOPIA_CLOUD_ID $WRITEOPIA_CLOUD_ID

COPY ./build/libs/documents_spring.jar api_editor_spring.jar 
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api_editor_spring.jar"]
