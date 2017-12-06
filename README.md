# PLD_Agile

[![Build Status](https://travis-ci.org/SacreeBandeDePote/PLD_Agile.svg?branch=BranchSaveTest)](https://travis-ci.org/SacreeBandeDePote/PLD_Agile)
[![codecov](https://codecov.io/gh/SacreeBandeDePote/PLD_Agile/branch/master/graph/badge.svg)](https://codecov.io/gh/SacreeBandeDePote/PLD_Agile)

## Usage

### Pre-Requisites

To launch the project you will need:
- [Java SE Development Kit 8u152](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Apache Maven 3.3.9](https://maven.apache.org/install.html)


### Getting the sources

You can download the lastest realase [here](https://github.com/SacreeBandeDePote/PLD_Agile/releases/latest/)

### Launch

Make sure of your configuration by excecuting the following command:
`mvn --version`

First you need to compile the project, execute the following command:
`cd agile/ && mvn compile`

If you are inside the `agile` directory:
`mvn compile`

Execute the follwing command to launch the project
`cd agile/ && mvn exec:java`

If you are inside the `agile` directory:
`mvn exec:java`

## Testing

Excute the following commant to perform tests with JUnit4:
`cd agile/ && mvn test`

If you are inside the `agile` directory:
`mvn test`
