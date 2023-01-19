FROM adoptopenjdk:11-jre-hotspot
COPY target/tokenService-1.0.0.jar /usr/src/
WORKDIR /usr/src/
CMD java -Xmx32m -jar tokenService-1.0.0.jar
