#available outside openshift, on open internet
FROM registry.access.redhat.com/jboss-webserver-3/webserver31-tomcat8-openshift

#available in openshift pipeline, above FROM is replaced when using dockerStrategy in BuildConfig
#FROM webserver31-tomcat8

USER root

# configure tomcat
RUN rm -Rf /opt/webserver/webapps/*
COPY ROOT.xml /opt/webserver/conf/Catalina/localhost/
COPY setenv.sh /opt/webserver/bin

# update to oracle java
WORKDIR /tmp
RUN curl -LO http://download.oracle.com/otn-pub/java/jdk/8u131-b11/d54c1d3a095b4ff2b6607d096fa80163/jdk-8u131-linux-x64.rpm -H --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie"; \
    rpm --quiet -U jdk-8u131-linux-x64.rpm; \
    rm -f jdk-8u131-linux-x64.rpm;

COPY jdbc-query.war /opt/webserver/webapps/ROOT.war

CMD /opt/webserver/bin/launch.sh
