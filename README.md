# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server
[Presentation mode](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm65TTuYhB6gatLlqOIELEsJpEuGFocuQACiu70XA9QwEB5Fge8MCcuENQALJsR0obUW6naYaUFjoo6zoEjhLLlJ63pBiRI6jFRTKiZGdEAJIAHJkPR4SsexMDTrO6AwHp9Q1KZQbCRp0pJpeMF4UG5loNJibJpBJbITy2a5pg-4gtBJRXAMpGjLsXxmY2zbjq2fTfleIWFNkPYwP2g69BFqn3NWfSxXO0VLpwq7eH4gReCgFnIb4zDHukmSYOlF5FNQ17SIx9H1PRzQtA+qhPt0RXoO2v5nECJajR5wVsuJMDwfY9Wxv6q3uRhcpYbJInyUYKDcJkq0Bm5cVoOpZoRoUloyN1vW2XWZ3cbxAnTvZl1iVtCpKiqmGdsF33KkFPmpe1YUwIqQM-p2rVgH2A5Dn0ozqMA5LjFMZA+OGABmP1hEt5K2vVSVmGVngVRukK2ru0IwAA4qOrKNaeLXnswoXXnTPX9fYo4jadc7jWyAMPTOZ3A1NE0LcgsQM8jsLrWdm0wRqcmkjA5JgMdM0XTRWnlAZTEsYJeXPfxoswAA6iwhn0aL7kwAAvKWM0HK6jmheCJu3KOnnbTIavuot0Jy2oKk+2p7169ddGG8xrG86M2nSLMJ6ZAadyzDoCCgA2GdkRro66aOZsCYnhjW7bMBp-qkWGM7gF13sMA1OEQo5yAed107pZAW7gefS5lQ9OXyep01td5VnHdd1PhejMXox7NMLir40fv-SD5Q07LjMBQgzAVCPo5j9XE-52+7e5xfwHzygi8oMvq8uOvc1OWD17H0nKdn6eN-T9fOusxy4PyfmvIW79Ybw2yhUPo5dVDoyPqPH+Nd-5X07mgkBo4wEv1Jiucm65AjYB8FAbA3B4C6kyPTEuzNmqw3milcoN4Gg8z5sEAW6AhxYKXhAv8W97ZnWAUXX2b9nJOnlDARSmRQ6wlDqhDEytxH+3durTW2sOHnSjppGOBturGyAqXC2ldwh2xmj3HorstEezEV7ICG9sK7XVjyDoYAZEmW4SgXW2j2TlD0gZIylldLWTvjAWEyDx5-yAXfB+mIYAADUFC7kpPRMgoSehIC-LMRueVl6lg8XsLx1iwZe2QfY7yktyhwEoSgORahcx5JPtICCktB7g36Mg9Gi4IGnCgZlBGOUOkThJsucqhCAiWAOvBZIMAABSEAeTUNGIEbOudWY5HZmI68VRKR3haOXfmj05xDjIcACZUA4AQHglAIR39kodQmiLGa74c5nIuVcm5KBk4S1TAwoeAAreZaAZFzP8igNECj7E7Qcqoik6jDnoEKeafWDEjbGW7jxc204rY2xMQIuc5jLEqIjJ7CRdi-oOOhUHTWMjkGIquj4lF8df7pyiSsjB6KXohOMXbZB5jkH90ccSmxEjy7xi2qrQV5Q-BaGkaOMJjTZgmROa8y50A6W0XKJSKo0gFD3XLgxViPRlWUDedAPYN9zF92xVXXlDd+VlL4RU2ZgLak5gPt8qCHM0yQ1+ilGGbNoF4NGZVAIXhTldi9LAYA2AyGEQIIkFIE81nmF+eDCoXUmK9X6sYXhk1UwKW4HgRkegDCwmxKI4pEiQCFqgMW-QKAy2QoDpK6uxAUDSAAEKNqJRq26ma7YYoEjcWY04PlWKRSS2CEMfoOrzcCQGKo35eoXd0tKAa+lOCDUAA)

[Editable](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm65TTuYhB6gatLlqOIELEsJpEuGFocuQACiu70XA9QwEB5Fge8MCcuENQALJsR0obUW6naYaUFjoo6zoEjhLLlJ63pBiRI6jFRTKiZGdEAJIAHJkPR4SsexMDTrO6AwHp9Q1KZQbCRp0pJpeMF4UG5loNJibJpBJbITy2a5pg-4gtBJRXAMpGjLsXxmY2zbjq2fTfleIWFNkPYwP2g69BFqn3NWfSxXO0VLpwq7eH4gReCgFnIb4zDHukmSYOlF5FNQ17SIx9H1PRzQtA+qhPt0RXoO2v5nECJajR5wVsuJMDwfY9Wxv6q3uRhcpYbJInyUYKDcJkq0Bm5cVoOpZoRoUloyN1vW2XWZ3cbxAnTvZl1iVtCpKiqmGdsF33KkFPmpe1YUwIqQM-p2rVgH2A5Dn0ozqMA5LjFMZA+OGABmP1hEt5K2vVSVmGVngVRukK2ru0IwAA4qOrKNaeLXnswoXXnTPX9fYo4jadc7jWyAMPTOZ3A1NE0LcgsQM8jsLrWdm0wRqcmkjA5JgMdM0XTRWnlAZTEsYJeXPfxoswAA6iwhn0aL7kwAAvKWM0HK6jmheCJu3KOnnbTIavuot0Jy2oKk+2p7169ddGG8xrG86M2nSLMJ6ZAadyzDoCCgA2GdkRro66aOZsCYnhjW7bMBp-qkWGM7gF13sMA1OEQo5yAed107pZAW7gefS5lQ9OXyep01td5VnHdd1PhejMXox7NMLir40fv-SD5Q07LjMBQgzAVCPo5j9XE-52+7e5xfwHzygi8oMvq8uOvc1OWD17H0nKdn6eN-T9fOusxy4PyfmvIW79Ybw2yhUPo5dVDoyPqPH+Nd-5X07mgkBo4wEv1Jiucm65AjYB8FAbA3B4C6kyPTEuzNmqw3milcoN4Gg8z5sEAW6AhxYKXhAv8W97ZnWAUXX2b9nJOnlDARSmRQ6wlDqhDEytxH+3durTW2sOHnSjppGOBturGyAqXC2ldwh2xmj3HorstEezEV7ICG9sK7XVjyDoYAZEmW4SgXW2j2TlD0gZIylldLWTvjAWEyDx5-yAXfB+mIYAADUFC7kpPRMgoSehIC-LMRueVl6lg8XsLx1iwZe2QfY7yktyhwEoSgORahcx5JPtICCktB7g36Mg9Gi4IGnCgZlBGOUOkThJsucqhCAiWAOvBZIMAABSEAeTUNGIEbOudWY5HZmI68VRKR3haOXfmj05xDjIcACZUA4AQHglAIR39kodQmiLGa74c5nIuVcm5KBk4S1TAwoeAAreZaAZFzP8igNECj7E7Qcqoik6jDnoEKeafWDEjbGW7jxc204rY2xMQIuc5jLEqIjJ7CRdi-oOOhUHTWMjkGIquj4lF8df7pyiSsjB6KXohOMXbZB5jkH90ccSmxEjy7xi2qrQV5Q-BaGkaOMJjTZgmROa8y50A6W0XKJSKo0gFD3XLgxViPRlWUDedAPYN9zF92xVXXlDd+VlL4RU2ZgLak5gPt8qCHM0yQ1+ilGGbNoF4NGZVAIXhTldi9LAYA2AyGEQIIkFIE81nmF+eDCoXUmK9X6sYXhk1UwKW4HgRkegDCwmxKI4pEiQCFqgMW-QKAy2QoDpK6uxAUDSAAEKNqJRq26ma7YYoEjcWY04PlWKRSS2CEMfoOrzcCQGKo35eoXd0tKAa+lOCDUAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
