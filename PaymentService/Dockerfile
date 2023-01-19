FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/src/
COPY target/paymentService-1.0.0.jar /usr/src/
COPY target/lib /usr/src/lib
CMD java -Xmx32m -jar paymentService-1.0.0.jar
