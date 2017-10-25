FROM ubuntu:16.04
#FROM repository.cars.com/openjdk-jdk8-alpine33:0.0.1


RUN apt-get update && apt-get install -y \
    build-essential \
    openjdk-8-jdk gradle\
    cmake \
    curl \
    git \
    libcurl3-dev \
    libleptonica-dev \
    liblog4cplus-dev \
    libopencv-dev \
    libtesseract-dev \
    wget

env JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
 # Copy all data
 copy . /srv/license-plate-recognition

 # Setup the build directory
 run mkdir /srv/license-plate-recognition/src/build
 workdir /srv/license-plate-recognition/src/build


 # Setup the compile environment
 run cmake -DCMAKE_INSTALL_PREFIX:PATH=/usr -DCMAKE_INSTALL_SYSCONFDIR:PATH=/etc .. && \
     make -j2 && \
     make install

 workdir /data


ENV DT_AGENT_PATH=
WORKDIR /app
VOLUME /tmp
ADD . /app
RUN chmod +x start.sh &&     /app/gradlew build -x test --stacktrace
EXPOSE 9000
ENTRYPOINT ["./start.sh"]
