echo "Build and Docker Compose DTU-pay"

mvn -f ./messaging-utilities-3.3/ clean install
mvn -f ./AccountManagement/ clean install
mvn -f ./Facade/facadeservice/ clean install
mvn -f ./PaymentService/ clean install
mvn -f ./ReportService/ clean install
mvn -f ./TokenMicroservice/ clean install
mvn -f ./demo_client/ clean install

docker-compose -f ./docker-compose.yml build
docker-compose -f ./docker-compose.yml up