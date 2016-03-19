rm target/* -r
mvn compile assembly:single
cp target/server_hnachkin-1.0-jar-with-dependencies.jar .
