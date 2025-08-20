FROM openjdk:17-jdk

COPY target/userservice-0.0.1-SNAPSHOT.jar .

EXPOSE 8082

ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar userservice-0.0.1-SNAPSHOT.jar"]
