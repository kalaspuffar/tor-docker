FROM ubuntu

MAINTAINER Daniel Persson (mailto.woden@gmail.com)

RUN apt-get update
RUN apt-get install --assume-yes default-jdk git maven micro-httpd
RUN git clone https://github.com/thaliproject/Tor_Onion_Proxy_Library.git torproxy

WORKDIR /torproxy/universal
RUN chmod +x gradlew
RUN ./gradlew install

WORKDIR /torproxy/java
RUN chmod +x gradlew
RUN ./gradlew install
RUN ./gradlew test

ADD index.html /var/www/index.html
RUN /etc/init.d/openbsd-inetd start
RUN mkdir server
ADD pom.xml server/pom.xml
ADD src server/src

WORKDIR /server
VOLUME ["/server"]
CMD mvn package
EXPOSE 9797 9797
CMD java -jar target/server-0.0.1-jar-with-dependencies.jar