FROM jboss/wildfly
ADD recipe.war /opt/jboss/wildfly/standalone/deployments/
ADD mysql-module.xml /opt/jboss/wildfly/modules/system/layers/base/com/mysql/main/module.xml
ADD mysql-connector-java-5.1.36-bin.jar /opt/jboss/wildfly/modules/system/layers/base/com/mysql/main/mysql-connector-java.jar
ADD standalone-custom.xml /opt/jboss/wildfly/standalone/configuration/

CMD /opt/jboss/wildfly/bin/standalone.sh -c standalone-custom.xml -b 0.0.0.0 -bmanagement 0.0.0.0

# see https://github.com/projectatomic/docker-image-examples/blob/master/rhel-jbosseap/Dockerfile
# see https://github.com/goldmann/wildfly-docker-configuration/blob/master/custom-file/Dockerfile