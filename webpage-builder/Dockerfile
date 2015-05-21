FROM fedora:latest

RUN yum -y update && yum clean all
RUN yum -y install \
    nodejs\
    autoconf \
    automake \
    bison \
    bzip2 \
    gcc-c++ \
    git \
    glibc-devel \
    glibc-headers \
    libffi-devel \
    libtool \
    libxml2-devel \
    libxslt-devel \
    libyaml-devel \
    make \
    openssl-devel \
    patch \
    patch \
    readline-devel \
    sqlite-devel \
    which \
    zlib-devel \
&& yum clean all

RUN useradd -m awestruct
USER awestruct

WORKDIR /home/narayana.io

ENV RUBY_VERSION 1.9.3
ENV BUNDLER_VERSION 1.7.4

RUN gpg2 --keyserver hkp://keys.gnupg.net --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3
RUN curl -sSL https://get.rvm.io | bash -s stable --ruby=$RUBY_VERSION
RUN bash -l -c "rvm use $RUBY_VERSION"
RUN bash -l -c "rvm cleanup all"
RUN bash -l -c "gem install bundler -v $BUNDLER_VERSION --no-rdoc --no-ri"

COPY docker-entrypoint.sh /home/docker-entrypoint.sh

ENTRYPOINT ["/home/docker-entrypoint.sh"]
