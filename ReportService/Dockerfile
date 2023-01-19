FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/src/
COPY target/lib /usr/src/lib
COPY target/ReportService-1.0.0.jar /usr/src/
CMD java -Xmx32m -jar ReportService-1.0.0.jar
