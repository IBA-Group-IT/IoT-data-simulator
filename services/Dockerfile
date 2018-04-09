FROM gradle:4.0-jdk8-alpine as app-builder

USER root
RUN mkdir -p /usr/services
WORKDIR /usr/services

COPY src ./src
COPY build.gradle .
RUN gradle build


# Final image with built spring boot jar and java 9 jre
FROM openjdk:8-jre-alpine

COPY --from=app-builder /usr/services/build/libs/services-0.0.1-SNAPSHOT.jar . 
ENV JAVA_OPTS=""
EXPOSE 8083

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar ./services-0.0.1-SNAPSHOT.jar"]