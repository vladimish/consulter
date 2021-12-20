FROM openjdk:17-jdk-oraclelinux7
ADD . /src
WORKDIR /src
RUN ./gradlew build -x test
ENTRYPOINT ["java","-jar","build/libs/gateway-0.0.1-SNAPSHOT.jar"]