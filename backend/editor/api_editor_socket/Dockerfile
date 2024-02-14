FROM openjdk:17

ARG WRITEOPIA_FIREBASE_ID
ENV WRITEOPIA_FIREBASE_ID $WRITEOPIA_FIREBASE_ID

ARG DB_USER
ENV DB_USER $DB_USER

COPY ./build/libs/api_editor_socket-all.jar api_editor.jar 
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api_editor.jar"]
