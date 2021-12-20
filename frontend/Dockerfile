FROM openjdk:17-jdk-oraclelinux7
ADD . /src
WORKDIR /src
RUN ./gradlew build -x test
EXPOSE 8080
ENTRYPOINT ["java","-jar","build/libs/frontend-0.0.1-SNAPSHOT.jar"]