From java:8
ARG IMAGE_PROJECT_TAG
ENV SW_AGENT_NAME ${IMAGE_PROJECT_TAG}
WORKDIR ${SW_AGENT_NAME}
RUN bash -c 'touch /app.jar'
ADD dockerdemo.jar /app.jar
EXPOSE 50000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]