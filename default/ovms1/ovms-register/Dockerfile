FROM pig4cloud/java:8-jre

MAINTAINER wangiegie@gmail.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /ovms-register

WORKDIR /ovms-register

EXPOSE 8848

ADD ./target/ovms-register.jar ./


CMD sleep 60;java -Xms128m -Xmx256m -Djava.security.egd=file:/dev/./urandom -jar ovms-register.jar


