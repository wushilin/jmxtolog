FROM openjdk:latest
COPY jmxtolog-0.0.1-SNAPSHOT /jmxtolog-0.0.1-SNAPSHOT
COPY example/application.properties /application.properties
WORKDIR /jmxtolog-0.0.1-SNAPSHOT/bin
CMD ["./jmxtolog", "--spring.config.location=/application.properties"]