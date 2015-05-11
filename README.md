# maestro

##building

```
mvn clean install
```

##running

```
java -jar target/maestro-1.0-SNAPSHOT.jar /Users/ericcobb/test.json
```

An API Gateway and orchestration layer for services.

##Goals

* easily forward HTTP requests to multiple microservices with little configuration
* provide the ability to add new routes with special routing specified in a custom groovy script, [as described by
netflix here](http://techblog.netflix.com/2013/01/optimizing-netflix-api.html?)
