FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/src/
COPY target/facadeservice-1.0.0.jar /usr/src/
COPY target/lib /usr/src/lib
CMD java -Xmx32m -jar facadeservice-1.0.0.jar
