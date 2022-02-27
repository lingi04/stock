FROM adoptopenjdk/openjdk14:jre-14.0.2_12-alpine

# Application
#RUN mkdir -p /var/log/app
#RUN mkdir -p /var/log/app/gc

#VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} /app.jar
#COPY docker-entrypoint.sh /app/docker-entrypoint.sh

ENTRYPOINT ["java", "-jar", "/app.jar"]

