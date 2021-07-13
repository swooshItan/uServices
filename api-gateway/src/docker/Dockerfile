# FROM openjdk:8-jdk-alpine
FROM openjdk:8

# USER root

ENV APP_PATH=/usr/src/app

# Copy files...
COPY ./context/@project.artifactId@-@project.version@.jar $APP_PATH/@project.artifactId@-@project.version@.jar
COPY ./context/startServer.sh $APP_PATH/startServer.sh
COPY ./context/config $APP_PATH/config

# Modify permissions...
RUN chmod -R g+rx $APP_PATH

WORKDIR $APP_PATH

CMD ["./startServer.sh"]

# USER nonroot
