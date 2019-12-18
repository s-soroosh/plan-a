FROM oracle/graalvm-ce:19.3.0-java11 as builder
RUN gu install native-image

RUN curl -fsSL https://archive.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz | tar xzf - -C /usr/share \
  && mv /usr/share/apache-maven-3.6.3 /usr/share/maven \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven

WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY ./src /build/src
RUN mvn package

FROM scratch

WORKDIR /app
COPY --from=builder /build/target/application .
CMD ["/app/application"]



