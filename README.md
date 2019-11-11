# Test container POC

This project is an attempt to use a disposable throwaway instance of a database.

This code uses [Testcontainers](https://www.testcontainers.org/).

## Content

This project tests access to two databases:

- Redis
- MySQL

The code that uses the Redis instance is based on existing [examples][1].  
The code that uses the MySQL instance is based on this [project][2].

## Build

This project uses Maven to build and test programs.

```
mvn compile
```

## Test

```
sudo docker run -it --rm -v $PWD:$PWD -w $PWD -v /var/run/docker.sock:/var/run/docker.sock maven:3 mvn test
```

Since the database instances are dockerized, tests must be launched as `root` 

## License

This little POC is under the [MIT License][3].

---

[1]: https://github.com/testcontainers/testcontainers-java-examples
[2]: https://github.com/phauer/blog-related/tree/master/dont-use-in-memory-databases-tests/db-container-managed-by-the-test
[3]: ./LICENSE.md

