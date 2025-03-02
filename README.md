# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Server
[Presentation mode](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm6ZIUgatLlqOJpEuGFocuQACiu6UXA9QwEBIELEsMCcuENQALIMR0oakW6naYeUjEwB+SyOs6l4weUJRIAAZpYyGIWgKDJCJoGfkxYHvDAqT6KkGFylhklOvKMDkspUDekGhEjqMBkwRqOEsuUnpWX6Nm3MRvFMvxkYUQAkgAcmQlHhPRwnTrO6AwEF9Q1DA07eWaEbQSZsEJUGUVoOJibJpBJaKWg2a5pg-4gqlAEDERoy7F8kWNs246tn035XuVhTZD2MD9oOvRVbZ9zVn09VzrVS6cKu3h+IEXgoNFyG+Mwx7pJkmCdReRTUNe0jUZR9SUc0LQPqoT7dCN6Dtr+ZxAiW53ZWVbKCTA8H2Itsb+u9WX2Wljl8c5ZkUu9AaZQ1aAkT50p+eUIU0XRGV1qDrHsVx04wAA6iwoWUfDM6IwAvKWd0HK6kOpeCOPxoZv0Q6SRgoNwmRA3d4PJeR5Q7TR+041lSOcTjSVkUmxnk4qyo5VheU3Wmosqg9QubSUCpKiqP6dutYB9gOQ59KM6jAOS4xTGQPjhrJythC95K2otLVmBNnhTRukK2ru0IwAA4qOrLLaea3nswFVphU7t7Yd9ijmdINzpdbJleUd2lflV1PcgsSe7rsKfaD30STITm0+SYBM1H6As4LhSWlRsP0XdvMo0GAu+WTpkJ5h1PJXB0IKMq6dqLCZe+RXFEw7R9Hh7rddmV7jek8Lpnj2o4udnHe7Qr3qjFQgzAVD0C-+dIswnpkBp3LMOgIKADYn6OswL4Fo57NMLjP40idSwJbXlDve8HzAR-6tVYCQoL4gCvoA2+o576jEfs-Fwr9Vby3VprXqFQ+gL1UIbb+o596HxWgAgaZ8QFgIIVPUYUCUAwJfnbFcDt1yBGwD4KA2BuDwF1JkD2o4Uh4L9jkAOxlry1AaGHCOwQS7Pl6HfB+MdJapnjmIiBZDRxv1TI9QyLk2EoF7rCXuqEMQ50TASfO7oAZF2sszGe5ooZV1HtzRGbE+aJRJpY5u6VW5U2wn9WmrlMhaMkXZCxEYh7lCCiFMKpCUDkJioFOK4SNBOJSnPdKP8l7yxXnADROi1C5lLD-CC795Y-iuGg7B0hDaLmkacJB3UtZ9R-mUtsy5Jp0ICJYem8FVIACkIA8g4aMQI59L48PMKoraQcqiUjvC0BekcEZziHMw4ArSoBwAgPBKACiUD71aqM2OSc5GzPQO+C+SyVlrI2fvZRUFEnlAAFbdLQForpPJdHoRSYYzxxjC7FwOWDAJbNrFwyApPVGGMsa2LnDAAmPQiZ-IKWo7iA03l5w+XhMAviSkD0hkEgF9F-7XzfMAy++LDD2K4gvdGmNwjYx-pCnJJTiZGI-lJcJlMHIeJpsYvwWgfGjlhD-WYwkFknNWdATFljsWUiqNIBQXNyUhXoj0IVlBTnQD2MS2lgFAF7ApWCmlULckpJkcCcoTyHmjk3nmJOTLFYwBlhUjq-tkHUKadNAIXhFldi9LAYA2BmGEHiIkLhvt1YjJtRUDme0DqtGMNIv8eyYAgG4HgRkegDD90uR2FxLkk1QBTfoFA6a27so7gmnNBpVDpvif8qlHEagADVsbAGVAiqAcTGVwuZXaot7yOXZq9evSt7bsU1vrY25t6DYVZttcrJF8S+14CznOCtYrAnsnKCOhtCVm13TbR861IsZ1FqNSWO1ctA5KzFggypjrqlOGdUAA)

[Editable](https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5ks9K+KDvvorxLAC5wFrKaooOUCAHjysL7oeqLorE2IJoYLphm6ZIUgatLlqOJpEuGFocuQACiu6UXA9QwEBIELEsMCcuENQALIMR0oakW6naYeUjEwB+SyOs6l4weUJRIAAZpYyGIWgKDJCJoGfkxYHvDAqT6KkGFylhklOvKMDkspUDekGhEjqMBkwRqOEsuUnpWX6Nm3MRvFMvxkYUQAkgAcmQlHhPRwnTrO6AwEF9Q1DA07eWaEbQSZsEJUGUVoOJibJpBJaKWg2a5pg-4gqlAEDERoy7F8kWNs246tn035XuVhTZD2MD9oOvRVbZ9zVn09VzrVS6cKu3h+IEXgoNFyG+Mwx7pJkmCdReRTUNe0jUZR9SUc0LQPqoT7dCN6Dtr+ZxAiW53ZWVbKCTA8H2Itsb+u9WX2Wljl8c5ZkUu9AaZQ1aAkT50p+eUIU0XRGV1qDrHsVx04wAA6iwoWUfDM6IwAvKWd0HK6kOpeCOPxoZv0Q6SRgoNwmRA3d4PJeR5Q7TR+041lSOcTjSVkUmxnk4qyo5VheU3Wmosqg9QubSUCpKiqP6dutYB9gOQ59KM6jAOS4xTGQPjhrJythC95K2otLVmBNnhTRukK2ru0IwAA4qOrLLaea3nswFVphU7t7Yd9ijmdINzpdbJleUd2lflV1PcgsSe7rsKfaD30STITm0+SYBM1H6As4LhSWlRsP0XdvMo0GAu+WTpkJ5h1PJXB0IKMq6dqLCZe+RXFEw7R9Hh7rddmV7jek8Lpnj2o4udnHe7Qr3qjFQgzAVD0C-+dIswnpkBp3LMOgIKADYn6OswL4Fo57NMLjP40idSwJbXlDve8HzAR-6tVYCQoL4gCvoA2+o576jEfs-Fwr9Vby3VprXqFQ+gL1UIbb+o596HxWgAgaZ8QFgIIVPUYUCUAwJfnbFcDt1yBGwD4KA2BuDwF1JkD2o4Uh4L9jkAOxlry1AaGHCOwQS7Pl6HfB+MdJapnjmIiBZDRxv1TI9QyLk2EoF7rCXuqEMQ50TASfO7oAZF2sszGe5ooZV1HtzRGbE+aJRJpY5u6VW5U2wn9WmrlMhaMkXZCxEYh7lCCiFMKpCUDkJioFOK4SNBOJSnPdKP8l7yxXnADROi1C5lLD-CC795Y-iuGg7B0hDaLmkacJB3UtZ9R-mUtsy5Jp0ICJYem8FVIACkIA8g4aMQI59L48PMKoraQcqiUjvC0BekcEZziHMw4ArSoBwAgPBKACiUD71aqM2OSc5GzPQO+C+SyVlrI2fvZRUFEnlAAFbdLQForpPJdHoRSYYzxxjC7FwOWDAJbNrFwyApPVGGMsa2LnDAAmPQiZ-IKWo7iA03l5w+XhMAviSkD0hkEgF9F-7XzfMAy++LDD2K4gvdGmNwjYx-pCnJJTiZGI-lJcJlMHIeJpsYvwWgfGjlhD-WYwkFknNWdATFljsWUiqNIBQXNyUhXoj0IVlBTnQD2MS2lgFAF7ApWCmlULckpJkcCcoTyHmjk3nmJOTLFYwBlhUjq-tkHUKadNAIXhFldi9LAYA2BmGEHiIkLhvt1YjJtRUDme0DqtGMNIv8eyYAgG4HgRkegDD90uR2FxLkk1QBTfoFA6a27so7gmnNBpVDpvif8qlHEagADVsbAGVAiqAcTGVwuZXaot7yOXZq9evSt7bsU1vrY25t6DYVZttcrJF8S+14CznOCtYrAnsnKCOhtCVm13TbR861IsZ1FqNSWO1ctA5KzFggypjrqlOGdUAA)

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
