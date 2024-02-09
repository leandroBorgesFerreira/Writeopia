FROM openjdk:17

ARG WRITEOPIA_CLOUD_ID

ENV WRITEOPIA_CLOUD_ID $WRITEOPIA_CLOUD_ID

COPY ./build/libs/api_editor_socket-all.jar api_editor.jar 
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api_editor.jar"]
