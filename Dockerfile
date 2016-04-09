FROM ubuntu

MAINTAINER mailto.woden@gmail.com

RUN apt-get update
RUN apt-get install default-jdk git maven
RUN git clone https://github.com/thaliproject/Tor_Onion_Proxy_Library.git torproxy

RUN cd torproxy/universal
RUN chmod +x gradlew
RUN ./gradlew install

RUN cd ../java
RUN chmod +x gradlew
RUN ./gradlew install
RUN ./gradlew test