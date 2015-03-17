FROM fedora
MAINTAINER Gytis Trikleris <gytis@redhat.com>
WORKDIR /home

ENV NARAYANA_PORT 4710
ENV NARAYANA_VERSION 5.0.5.Final-SNAPSHOT
ENV NARAYANA_HOME /home/narayana

RUN yum install -y java wget unzip \
    && wget https://www.dropbox.com/s/4eiunuuyqinoo4t/narayana-full-5.0.5.Final-SNAPSHOT-bin.zip \
    && unzip narayana-full-$NARAYANA_VERSION-bin.zip \
    && mv /home/narayana-full-$NARAYANA_VERSION $NARAYANA_HOME \
    && rm narayana-full-$NARAYANA_VERSION-bin.zip

ENV JAVA_HOME /etc/alternatives/jre

COPY docker-entrypoint.sh /home/docker-entrypoint.sh

ENTRYPOINT ["/home/docker-entrypoint.sh"]
CMD source $NARAYANA_HOME/jts-jacorb-setup-env.sh; \
    java -Dcom.arjuna.orbportability.common.OrbPortabilityEnvironmentBean.bindMechanism=NAME_SERVICE \
        -DObjectStoreEnvironmentBean.objectStoreDir=/home/tx-object-store \
        $NARAYANA_OPTS \
        -DOAPort=$NARAYANA_PORT \
        -Djacorb.log.default.verbosity=3 \
        com.arjuna.ats.jts.TransactionServer -recovery -ORBInitRef.NameService=$NAME_SERVER_URL
EXPOSE 4710
