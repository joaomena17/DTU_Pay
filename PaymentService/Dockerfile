FROM adoptopenjdk:11-jre-hotspot
COPY target/paymentService-1.0.0.jar /usr/src/
WORKDIR /usr/src/
CMD java -Xmx32m -jar paymentService-1.0.0.jar
