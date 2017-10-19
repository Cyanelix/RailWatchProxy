FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD /target/proxy.jar proxy.jar
EXPOSE 8080
RUN sh -c 'touch /proxy.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /proxy.jar" ]
