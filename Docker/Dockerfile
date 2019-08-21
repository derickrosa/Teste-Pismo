FROM tomcat:8-alpine

ENV JAVA_OPTS "-Djava.awt.headless=true -Xms512m -Xmx1g -XX:MaxPermSize=256m -XX:+UseConcMarkSweepGC"

RUN apk add --no-cache tzdata
ENV TZ America/Sao_Paulo

COPY testePismo_old-0.1.war /usr/local/tomcat/webapps/testepismo.war

CMD ["catalina.sh", "run"]