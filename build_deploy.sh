echo "Build and deploy DTU-pay"

mvn -f /var/lib/jenkins/workspace/dtupayJenkins/message-utilities-3.3/ clean install
mvn -f /var/lib/jenkins/workspace/dtupayJenkins/AccountManagement/ clean install
mvn -f /var/lib/jenkins/workspace/dtupayJenkins/Facade/ clean install
mvn -f /var/lib/jenkins/workspace/dtupayJenkins/PaymentMicroservice clean install
mvn -f /var/lib/jenkins/workspace/dtupayJenkins/PaymentService clean install
mvn -f /var/lib/jenkins/workspace/dtupayJenkins/ReportService/ clean install
mvn -f /var/lib/jenkins/workspace/dtupayJenkins/TokenMicroservice clean install
mvn -f /var/lib/jenkins/workspace/dtupayJenkins/demo_client clean install

docker-compose -f /var/lib/jenkins/workspace/dtupayJenkins/docker-compose.yml build
docker-compose -f /var/lib/jenkins/workspace/dtupayJenkins/docket-compose.yml up