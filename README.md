# java-hash-performance

Quick tool to see if we can isolate some performance differences between different servers

## Root cause
The root cause seems to be the I/O for the nist-data-mirror doesn't use buffered input and is very
slow. There's [issue 115](https://github.com/stevespringett/nist-data-mirror/issues/115) and 
[PR 116](https://github.com/stevespringett/nist-data-mirror/pull/116) open for these. It still showed
a new server disk was configured very differently.

## How to run
Just run the jar with the version of java you would like. For example:
```
java -jar java-hash-performance-0.0.1-SNAPSHOT.jar 
```

## Notes about how to run the mirror software if you need it
```
mkdir ~/Downloads/nist/
cd ~/Downloads/nist/
java -jar ~/.m2/repository/us/springett/nist-data-mirror/1.5.4/nist-data-mirror-1.5.4.jar .
```