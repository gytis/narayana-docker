FROM fedora
MAINTAINER Gytis Trikleris <gytis@redhat.com>
WORKDIR /home

ENV NARAYANA_PORT 9998
#ENV NARAYANA_VERSION 5.0.4.Final
ENV NARAYANA_VERSION 5.0.5.Final-SNAPSHOT
ENV NARAYANA_HOME /home/narayana

RUN yum install -y java wget unzip \
#    && wget http://www.jboss.org/jbosstm/downloads/$NARAYANA_VERSION/binary/narayana-full-$NARAYANA_VERSION-bin.zip \
    && wget https://www.dropbox.com/s/4eiunuuyqinoo4t/narayana-full-5.0.5.Final-SNAPSHOT-bin.zip \
    && unzip narayana-full-$NARAYANA_VERSION-bin.zip \
    && mv /home/narayana-full-$NARAYANA_VERSION $NARAYANA_HOME \
    && rm narayana-full-$NARAYANA_VERSION-bin.zip

ENV JAVA_HOME /etc/alternatives/jre

COPY docker-entrypoint.sh /home/docker-entrypoint.sh

ENTRYPOINT ["/home/docker-entrypoint.sh"]
CMD source $NARAYANA_HOME/jts-jacorb-setup-env.sh; java com.arjuna.ats.jts.TransactionServer -recovery -ORBInitRef.NameService=$NAME_SERVER_URL
EXPOSE 9998
