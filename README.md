# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server
[Presentation mode](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm65TTuYhB6gatLlqOIELEsJpEuGFocuQACiu70XA9QwEB5Fge8MCcuENQALJsR0obUW6naYaUFjoo6zoEjhLLlJ63pBiRI6jFRTKiZGdEAJIAHJkPR4SsexMDTrO6AwHp9Q1KZQbCRp0pJpeMF4UG5loNJibJpBJbITy2a5pg-4gtBJRXAMpGjLsXxmY2zbjq2fTfleIWFNkPYwP2g69BFqn3NWfSxXO0VLpwq7eH4gReCgFnIb4zDHukmSYOlF5FNQ17SIx9H1PRzQtA+qhPt0RXoO2v5nECJajR5wVsuJMDwfY9Wxv6q3uRhcpYbJInyTA5JgKtAZuXFaDqWaEaFJaDFMSxtl1qd3G8QJ04wAA6iwhn0fdM6PQAvKWM0HK6jmheCP3xltGpyaSRgoNwmRHTN500Vp5RdUxvU-e5T38T99kXWJW0KkqKqYZ2wUk8qQU+al7VhTAirUz+natWAfYDkOfSjOowDkuMUxkD44YAGak2ES3kra9VJWYZWeBVG6Qrau7QjAADio6so1p4teezChde6s9f19ijiNJ1zuNbKU9jp001NE0LcgsSazzsLradm0wdDu2wwdSOW+gKOaVddEGbdrEzbjL12SD5pOfT4MzZ520yDD7qLdCCjKm7aiwiHjlh+UEfMaxZs8zH+1awTqNg-K1c86nFO0+Uquu1rAUIMwFQ9BXKDadIswnpkBp3LMOgIKADZj2RjcoLpo57NMLir40DupkTHVpr3-eD8PTX6pFwFClPIAz8fsz94vozL6vLjryzidsxz2UVH0-eqALu+jvvMAj0fPKE8z4XyAfPG+KA75rzliuBW65AjYB8FAbA3B4C6kyBrUcKRD56xyAbZyDMbwNFNubYIQdny9Gvkva23lHauQenOK+o4IEbyggQ8GilMh51hHnVCGJvZOkTDtBy-sKSBwYcHWuod2Ql26ndaOPE8bTikaDdhDcU7k2wn7TOnCUDcKoWpFRCdi6WX0oZcuzCsFWRsp-IxEZ66wXnoPZuidbZwHQXo0cXdmB91-tICCjst4M36HvaQAtFw0NOC-TKnMcqhPCW2Zc5V4EBEsPDeCyQYAACkIA8kwaMQIk9p64PMPNFK5RqiUjvC0fuFsJEUJ6Cg4AaSoBwAgPBKATDRiD2StvG2rc7aMJgE0lpbSOldIHtIVhHYHHlAAFa5LQNwnJ-kUBon4S44RF0yRiOUsjOxtFZGR0EnlKur0PpfUGRZAGPQgYHMTgtICmz07aJ2WAfRfjC7GJkTdMu-9D6zzfKfaegLDCKIEv3d6n1wjfVCTAG5oTgYZyCeDfukMfZaJEZnPwWguGjlhKE2YJkRmUDGdAL5l0fmUiqNIBQWNIUGVYo0qeoz2nQD2KC+FpYgJ7ChZcuFCK-G9MEVhWhqZygrKWV4tQgU5oPPKYzUmkS0r61fjA5JlUAheGaV2L0sBgDYBQYRAgiRsG6zZmU7eFSMY9T6q0YwNC-wDJANwPAjI9AGALtMp2xMYAur1e6-QKAvWaK2eGBSrqoAGlUF6+OlLrowr4jUAAat9YAyoTlQA0HGlFDcmZkyhpi7ZfrI15xjRSw5ER6JJtTaZDNtic3ypcoq5mhaw24RLXqz2c5y33JMYmlNaaM0zWzciptIqqYFoxWK4Ek7vVBJLPm5VXY8FqqSUAA)

[Editable](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm65TTuYhB6gatLlqOIELEsJpEuGFocuQACiu70XA9QwEB5Fge8MCcuENQALJsR0obUW6naYaUFjoo6zoEjhLLlJ63pBiRI6jFRTKiZGdEAJIAHJkPR4SsexMDTrO6AwHp9Q1KZQbCRp0pJpeMF4UG5loNJibJpBJbITy2a5pg-4gtBJRXAMpGjLsXxmY2zbjq2fTfleIWFNkPYwP2g69BFqn3NWfSxXO0VLpwq7eH4gReCgFnIb4zDHukmSYOlF5FNQ17SIx9H1PRzQtA+qhPt0RXoO2v5nECJajR5wVsuJMDwfY9Wxv6q3uRhcpYbJInyTA5JgKtAZuXFaDqWaEaFJaDFMSxtl1qd3G8QJ04wAA6iwhn0fdM6PQAvKWM0HK6jmheCP3xltGpyaSRgoNwmRHTN500Vp5RdUxvU-e5T38T99kXWJW0KkqKqYZ2wUk8qQU+al7VhTAirUz+natWAfYDkOfSjOowDkuMUxkD44YAGak2ES3kra9VJWYZWeBVG6Qrau7QjAADio6so1p4teezChde6s9f19ijiNJ1zuNbKU9jp001NE0LcgsSazzsLradm0wdDu2wwdSOW+gKOaVddEGbdrEzbjL12SD5pOfT4MzZ520yDD7qLdCCjKm7aiwiHjlh+UEfMaxZs8zH+1awTqNg-K1c86nFO0+Uquu1rAUIMwFQ9BXKDadIswnpkBp3LMOgIKADZj2RjcoLpo57NMLir40DupkTHVpr3-eD8PTX6pFwFClPIAz8fsz94vozL6vLjryzidsxz2UVH0-eqALu+jvvMAj0fPKE8z4XyAfPG+KA75rzliuBW65AjYB8FAbA3B4C6kyBrUcKRD56xyAbZyDMbwNFNubYIQdny9Gvkva23lHauQenOK+o4IEbyggQ8GilMh51hHnVCGJvZOkTDtBy-sKSBwYcHWuod2Ql26ndaOPE8bTikaDdhDcU7k2wn7TOnCUDcKoWpFRCdi6WX0oZcuzCsFWRsp-IxEZ66wXnoPZuidbZwHQXo0cXdmB91-tICCjst4M36HvaQAtFw0NOC-TKnMcqhPCW2Zc5V4EBEsPDeCyQYAACkIA8kwaMQIk9p64PMPNFK5RqiUjvC0fuFsJEUJ6Cg4AaSoBwAgPBKATDRiD2StvG2rc7aMJgE0lpbSOldIHtIVhHYHHlAAFa5LQNwnJ-kUBon4S44RF0yRiOUsjOxtFZGR0EnlKur0PpfUGRZAGPQgYHMTgtICmz07aJ2WAfRfjC7GJkTdMu-9D6zzfKfaegLDCKIEv3d6n1wjfVCTAG5oTgYZyCeDfukMfZaJEZnPwWguGjlhKE2YJkRmUDGdAL5l0fmUiqNIBQWNIUGVYo0qeoz2nQD2KC+FpYgJ7ChZcuFCK-G9MEVhWhqZygrKWV4tQgU5oPPKYzUmkS0r61fjA5JlUAheGaV2L0sBgDYBQYRAgiRsG6zZmU7eFSMY9T6q0YwNC-wDJANwPAjI9AGALtMp2xMYAur1e6-QKAvWaK2eGBSrqoAGlUF6+OlLrowr4jUAAat9YAyoTlQA0HGlFDcmZkyhpi7ZfrI15xjRSw5ER6JJtTaZDNtic3ypcoq5mhaw24RLXqz2c5y33JMYmlNaaM0zWzciptIqqYFoxWK4Ek7vVBJLPm5VXY8FqqSUAA)

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
