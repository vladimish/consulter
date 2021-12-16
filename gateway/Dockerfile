FROM openjdk:17-jdk-oraclelinux7
ADD . /src
WORKDIR /src
RUN ./gradlew build -x test
EXPOSE 8081
ENTRYPOINT ["java","-jar","build/libs/gateway-0.0.1-SNAPSHOT.jar"]