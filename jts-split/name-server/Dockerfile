FROM fedora
MAINTAINER Gytis Trikleris <gytis@redhat.com>
WORKDIR /home

ENV NAME_SERVER_PORT 9999
ENV JACORB_VERSION 2.3.1
ENV JACORB_HOME /home/jacorb-$JACORB_VERSION
ENV PATH $PATH:$JACORB_HOME/bin

RUN yum install -y ant wget unzip \
    && wget http://www.jacorb.org/releases/$JACORB_VERSION/jacorb-$JACORB_VERSION-bin.zip \
    && unzip jacorb-$JACORB_VERSION-bin.zip \
    && rm jacorb-$JACORB_VERSION-bin.zip \
    && ant scripts -f $JACORB_HOME/build.xml

COPY docker-entrypoint.sh /home/docker-entrypoint.sh

ENTRYPOINT ["/home/docker-entrypoint.sh"]
CMD ns -Djacorb.log.default.verbosity=3 -DOAPort=$NAME_SERVER_PORT -Djacorb.naming.ior_filename=/home/NS_Ref
EXPOSE 9999