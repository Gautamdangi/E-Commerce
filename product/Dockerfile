From eclipse-temurin:21-jre-jammy
#java 21 runtime base imageg

#workDir inside container
WORKDIR /app

# copy the build jar from target into image
COPY target/*.jar app.jar

# expose sprngboot port
EXPOSE 8080

#command to run the app
ENTRYPOINT ["java","-jar","app.jar"]
