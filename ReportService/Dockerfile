FROM adoptopenjdk:11-jre-hotspot
COPY target/ReportService-1.0.0.jar /usr/src/
WORKDIR /usr/src/
CMD java -Xmx32m -jar ReportService-1.0.0.jar
