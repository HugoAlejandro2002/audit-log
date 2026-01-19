# ---------- Build stage (Maven ya incluido) ----------
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml ./
COPY src src

RUN mvn -B -DskipTests package

# ---------- Runtime stage (UBI) ----------
FROM registry.access.redhat.com/ubi9/openjdk-17:1.23
ENV LANGUAGE='en_US:en'
WORKDIR /deployments

COPY --from=build /workspace/target/quarkus-app/lib/ lib/
COPY --from=build /workspace/target/quarkus-app/*.jar ./
COPY --from=build /workspace/target/quarkus-app/app/ app/
COPY --from=build /workspace/target/quarkus-app/quarkus/ quarkus/

EXPOSE 8080
USER 185
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENTRYPOINT ["/opt/jboss/container/java/run/run-java.sh"]