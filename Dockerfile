FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD target/proxy.jar proxy.jar
RUN sh -c 'touch /proxy.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /proxy.jar" ]
