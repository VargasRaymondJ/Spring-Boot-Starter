FROM maven:3.5-jdk-8 as BUILD_IMAGE
WORKDIR /api
COPY . /api
RUN mvn -s settings.xml clean package -Dmaven.test.skip=true
EXPOSE 8080
RUN cd target

FROM openjdk:8-jdk-alpine
COPY --from=BUILD_IMAGE /api/target .
EXPOSE 8080
CMD java -jar *.jar
