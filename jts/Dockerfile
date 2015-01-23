FROM fedora
MAINTAINER Gytis Trikleris <gytis@redhat.com>
WORKDIR /home

ENV NARAYANA_VERSION 5.0.4.Final
ENV NARAYANA_HOME /home/narayana

RUN yum install -y java wget unzip supervisor \
    && wget http://www.jboss.org/jbosstm/downloads/$NARAYANA_VERSION/binary/narayana-full-$NARAYANA_VERSION-bin.zip \
    && unzip narayana-full-$NARAYANA_VERSION-bin.zip \
    && mv /home/narayana-full-$NARAYANA_VERSION $NARAYANA_HOME \
    && mkdir -p /home/logs

ENV JAVA_HOME /etc/alternatives/jre

COPY supervisord.conf /home/supervisord.conf
COPY docker-entrypoint.sh /home/docker-entrypoint.sh

EXPOSE 9999 9998 4711 4712
CMD ["supervisord"]
ENTRYPOINT ["/home/docker-entrypoint.sh"]
