mvn clean > /dev/null
mvn package > /dev/null
cp ~/web-labs/web-lab3/target/web-lab3.war ~/wildfly-26.1.2.Final/standalone/deployments/
