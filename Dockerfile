FROM oracle/graalvm-ce:19.3.0 as builder
RUN yum install -y maven
RUN gu install native-image

WORKDIR /src
COPY . /src
RUN mvn package


FROM scratch

WORKDIR /app
COPY --from=builder target/application .

CMD ["/app/application"]



