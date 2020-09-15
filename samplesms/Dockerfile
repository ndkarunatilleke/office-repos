#280 MB Image with Java 11
FROM adoptopenjdk:11-jre-hotspot
#170 MB Image Size with Java 8
#FROM openjdk:8-alpine
#56 MB openjdk8 
#FROM openjdk:8-jre-alpine
EXPOSE 8999
ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/service/service.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/usr/share/service/service.jar"]
