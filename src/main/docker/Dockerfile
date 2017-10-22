#available outside openshift, on open internet
FROM registry.access.redhat.com/jboss-webserver-3/webserver31-tomcat8-openshift

#available in openshift pipeline, above FROM is replaced when using dockerStrategy in BuildConfig
#FROM webserver31-tomcat8

# must use root because we are changing permissions and installing files
USER root

# configure tomcat
WORKDIR /opt/webserver
COPY setenv.sh bin/
RUN rm -Rf webapps/*; \
    mkdir -p conf/Catalina/localhost; \
    chmod 770 conf/Catalina/localhost; \
    chmod 770 bin/setenv.sh

WORKDIR /usr/local/src
COPY ROOT.xml ROOT.xml

# above provides the configured platform
# the war is copied later -- TBD
# COPY jdbc-query.war /opt/webserver/webapps/ROOT.war

USER jboss
CMD /opt/webserver/bin/launch.sh
