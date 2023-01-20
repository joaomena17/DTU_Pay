echo "Build Dockerize and Run DTU-pay"

mvn -f ./messaging-utilities-3.3/ clean install
sleep 2
mvn -f ./AccountManagement/ clean install
sleep 2
mvn -f ./Facade/facadeservice/ clean install
sleep 2
mvn -f ./PaymentService/ clean install
sleep 2
mvn -f ./ReportService/ clean install
sleep 2
mvn -f ./TokenMicroservice/ clean install
sleep 2

docker-compose -f ./docker-compose.yml build
sleep 2
docker-compose -f ./docker-compose.yml up
