# Use Tomcat as the base image
FROM tomcat:9.0-jdk17

# Remove the default Tomcat webapps
RUN rm -rf /Program Files/Apache Software Foundation/Tomcat 9.0/webapps/*

# Copy your WAR file into the Tomcat webapps directory
COPY target/nestNavigator-0.0.1-SNAPSHOT.war /Program Files/Apache Software Foundation/Tomcat 9.0/webapps/ROOT.war

# Expose the Tomcat port
EXPOSE 8010

# Start Tomcat
CMD ["catalina.sh", "run"]
