FROM scratch

WORKDIR /app
COPY target/application .

CMD ["/app/application"]



