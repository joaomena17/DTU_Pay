FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/src/
COPY target/AccountManagement-1.0.0.jar /usr/src/
COPY target/lib /usr/src/lib
CMD java -Xmx32m -jar AccountManagement-1.0.0.jar
