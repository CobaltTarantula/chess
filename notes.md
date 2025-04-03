# My notes
## Make sure to commit regularly!
## Basic Git Commands
* ```git status``` will tell you what parts of your code haven't been pushed yet.
* ```git add FILENAME``` stages file changes
  * ```git add .``` stages all changes
* ```git commit -m "MESSAGE"``` commits staged changes with a description of changes
* ```git log``` shows history of commits
  * ```q``` exits the log
  * each commit has its own unique identifier
  * when working with commit id often the first few digits of the id is all that is needed to reference it
* ```git checkout .``` go back to previous version, or retrieve previous versions of files from the repository
  * if you use ```IDENTIFIER``` instead of ```.``` then that git commit is the version that will be restored
## .gitignore
* we want core files in the repository AKA **source files**
* other files not part of the **source files** AKA **derived files** we do __NOT__ want to put into the repository
* to avoid having to sift through whether files are source or derived, we use .gitignore
* whatever we put in .gitignore is ignored when commiting
* ```!``` makes git __NOT__ ignore whatever follows
## Renaming, Moving, and Deleting Files
* when renaming or moving files, instead of using ```mv```, use ```git mv```
* when deleting files, instead of using ```rm```, use ```git rm```
## IntelliJ Git Integration
* he talks about how to use git through the ide of IntelliJ
* seems similar to how I've used git in VS code
## Git Tips
* Review before commit
* Commit often
* Beware large binary files
* Use standard commit msg scheme
* use .gitignore
## GitHub Overview
* I already have an account: *CobaltTarantula*
* gives other people access to repositories
* better backup of the code through remote system
* run ```git clone URL``` to create a local copy of repository on computer
* ```git pull``` brings down all the changes that others have made to the repository
* ```git push``` sends changes up to repository
* **merge conflict** when trying to push conflicting code, the code push is prevented until a pull is enacted
## GitHub Set Up
* when making a repository gotta determine: name, description, public vs. private
* **Personal Access Token**: Profile -> Settings -> Developer Settings -> Personal Access Tokens -> Classic
  * I have it saved in my class folder
## GitHub Demo
* if adding files, after adding the files first ```git add``` then ```git push```
* can decide to ```git commit``` locally first and wait on ```git push```
## SQL in Java
### Database Access from Java
1. Load database driver 
2. Open database connection 
3. Start a transaction
4. Execute queries and/or updates
5. Commit or Rollback transaction
6. Close database connection
### Making JDBC Driver available to project
3 different ways:
1. **Add dependency from File/Project Structure**
2. Create maven project and add dependency to pom.xml file
3. Create gradle project and add dependency to build.gradle file
### Load Database Driver
* old way was try catch block
* now there are classes that do it
### Open database connection/start transaction
```
import java.sql.*;
//...
String connectionURL = "jdbc:mysql://localhost:3306/BookClub?" +
"user=jerod&password=mypassword"; // storing password in here is sus

Connection connection = null; // try with resources
try(Connection c = DriverManager.getConnection(connectionURL){
    connection = c;
    
    //start transaction
    connection.setAutoCommit(false);
  } catch(SQLException ex){
      //ERROR(rollback)
  }
}
```
* make sure to close AFTER committing or rolling back the transaction
  * or open connection in try-with-resources statement
### Execute Queries
```
List<Book> books = new ArrayList<>();
String sql = "select id, title, author, genre, category_id from book";

try(PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()){
    while(rs.next()) { // order based on sql query order
        int id = rs.getInt(1); // mind the 1
        String title = rs.getString(2);
        String author = rs.getString(3);
        String genre = rs.getString(4);
        int categoryId = rs.getString(5);
        //convert from relational model to object oriented model
        books.add(new Book(id, title, author, genre, categoryId));
    }
} catch(SQLException ex){
    //ERROR
}
```
* object relational mappers do the same thing but beyond scope of class
### Insert, Update, Delete
```
// values not in query to avoid hacking
String sql = "update book " + "set title = ?, author = ?, genre = ?, category_id = ?" + "where id = ?";

try(PreparedStatement stmt = connection.prepareStatement(sql)){
    // assume we have a reference named 'book' to an object with needed values
    stmt.setString(1, book.getTitle());
    stmt.setString(2, book.getAuthor());
    stmt.setString(3, book.getGenre());
    stmt.setInt(4, book.getCategoryId());
    stmt.setInt(5, book.getId());
    
    if(stmt.executeUpdate() == 1){
        System.out.println("Updated book " + book.getId());
    } else {
        System.out.println("Failed to update book " + book.getId());
    }
} catch(SQLException ex) {
    // ERROR
}
```
### Commit/Rollback
```
Connection connection = null;
try(Connection c = DriverManager.getConnection(connectionURL)){
    connection = c;
    //...
    connection.commit()
} catch(SQLException ex) {
    if(connection != null) {
        connection.rollback();
    }
    throw ex;
}
```
### Usernames, Passwords and Database Permissions
* Put usernames and passwords in configuration file that is gitignored
* setup connectionURL so it gets user and pass from configuration file
### Assembly
* JDBC in code that creates the database
## REMEMBER TO CALL THE THING THAT MAKES THE DATABASE **IN** THE CODE
## Client HTTP and Logging
### Get
* don't need to accept headers or determining content type because the server will only serve json strings
* only check authTokens
### Post
* nearly identical to Get, but ```connection.setDoOutput(true);```
* try with resources block -> write request body to OutputStream
### GET request/response Steps
1. Client: Create URL instance
2. Client: Open connection (url.openConnection()), set read timeout, set request method to GET, connect
3. Server: Mapped HTTP handler function is called with request and response objects
4. Server: Process request and return response
5. Client: Get Response code, get input stream
6. Client: Read and process input
### POST request/response Steps
1. Client: Create URL instance
2. Client: Open connection (url.openConnection()), set read timeout, set request method to Post, setDoOutput(true), connect
3. Client: Get output stream (connection.getOutputStream())
4. Client: Write request body to output stream
5. Server: Mapped HTTP handler function is called with request and response objects
6. Server: Process request and return response (including reading request body JSON and converting to request object)
7. Client: Get Response code, get input stream
8. Client: Read and process response
### Dependencies
* UI Client depends on UI ChessBoard and Server Facade
* Server Facade depends on Client communicator
* Client communicator depends on Internet (HTTP)
## Logging
* better way to print errors besides system out printlines
* Java has built-in support for logging
* Logs contain messages that provide information to
 * Software developers (debugging)
 * Sys admins
 * Customer support agents
* Programs send log messages to "loggers"
 * There can be one or more
* Each message has a "level"
 * SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST
* never remove debugging logs
### Loggers
* loggers have a method for each message level
 * severe, warning, info, config, fine, finer, finest
* configure to include or omit messages based on level
 * ```Logger.setLevel(level)method``` - ALL, OFF, or lowest level
### Handlers
* each logger has one or more "handlers" associated
* **ConsoleHandler**: sends messages to console
* **FileHandler**: sends messages to file
* **SocketHandler**: sends messages to network socket
* can be configured like loggers
### Formatters
* Each handler has a **formatter** which defines format used to encode messages
* **SimpleFormatter**
* **XMLFormatter**
### Logger Configuration
* configuration class with logger instance inside
## Websocket
* label method with ```@WebSocket```
* server requests similar to normal but call ```webSocket``` method with suffix
* Spark.get...
* ```onMessage``` method labeled with ```@OnWebSocketMessage``` with parameters ```Session session, String message```
* ```session.getRemote().sendString("WebSocket response: " + message);```
* Have a map of all the sessions that I get with the keys = GameID
### WebSocket client
* library will handle all the low-level stuff we don't want to worry about
* Install **glassfish.tyrus.bundles.standalone.client 1.15**
```java
public class WSClient extends Endpoint {
    private Session session;

    public WSClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }
    
  public void send(String msg) throws Exception {this.sessiongetBasicRemote().sendText(msg);}  
  public void onOpen(Session session, EndpointConfig endpointConfig) {}
}
```
## Chess Phase 6
Menu options:
1. Redraw Board
   * should have passed in board or matrix of pieces
   * pass in additional matrix to show where to highlight
2. Make Move
   * End game with checkmate
   * Shouldn't be possible after resignation or checkmate
   * observers can't
   * Promotion message with syntax for pawn promotion in back row
   * notifications of moves, check, and checkmate (victory message)
   * no highlights necessary
3. Highlight Legal Moves
   * Highlight currPos of piece and in another color highlight possible positions to move to
4. Resign
   * Concede -> don't leave game
   * "You have successfully resigned from the game"
   * Can still access board but can't play
5. Help
   * string of whatever help text desired
6. Leave Game
   * Don't necessarily resign
   * Could allow others to join in your place
   * If resigned, say "You resigned, can't leave resigned game"
* all players should be notified when players or observers enter/leave game
* WebSocketCommunicator shouldn't refer directly to ChessClient
  * create interface ServerMessageObserver that ChessClient implements (```notify``` method) with ```ServerMessage message``` as param
  * WebSocketCommunicator references ServerMessageObserver
  * indirect reference to ChessClient 
### Chicken-and-Egg Problem with Json/Gson
* Gson standard type adapter 
* deserializer
  * ```implements JsonDeserializer<Class>```
  ``` java
  public Class deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonException {
    Stuff
  }
  ```
* runtime type adapter
### Websocket Request Handler
* check slides
### ServerMessage (from server)
* three message types
  * **NOTIFICATION**
  * **ERROR**
  * **LOAD_GAME**
* use deserializer or whatever used for type adapter
### Chess Client
* check slides
## Computer Security
* computer systems should be security-focused when designed
* **bad actors** AKA hackers want to:
  * access data
    * at rest
    * active
  * access computers
    * Inject viruses, Launch attacks from someone else’s machines 
    * Reconfigure or disable systems, Destroy data 
    * Monitor activity
  * disable a system so that it can't be used
    * DDoS (distributed denial of service) attacks
### Cryptographic (One-Way) Hash Functions
* One-Way
* Deterministic
* Fixed-Size
* Pseudo-Random
### Cryptographic Hash Algorithms
* Cracked
  * MD-5
  * SHA-1
    * used by git commits
* Uncracked/Modern
  * SHA-2 (supersedes MD-5 and SHA-1)
  * SHA-3
### Password hashing
* methods to mitigate attacks
  * **add salt** - append to password before hashing, then store that into the database
  * **hash passwords** using cryptographic hash function that is expensive to compute - makes it harder to attack
    * Argon2
    * scrypt
    * bcrypt
### Encryption/Decryption
* **Encryption:** process of encoding data so that only authorized can read
* **Decryption:** process of decoding data back to og form
* **Plaintext:** data to be protected
* **Ciphertext:** encrypted form of data
* **Key:** info piece used as input to crypto algorithm to encode or decode
* **Key Size:** number of bits in key
### Encryption Algorithms
* Categories
  1. **Symmetric Key:** Secret Key algorithms
     * Same key used for encryption and decryption
  2. **Asymmetric Key:** Public Key algorithms
     * different keys for encryption and decryption
     * mathematical relationship (generated together)
     * encrypt with **public key** and person with matching **private key** can decrypt it
### Public Key Encryption
* Disadvantages of public key encryption
  * can only encrypt small amount of data
  * much slower than symmetric key encryption
  * private keys need to be securely stored and never shared
* Why use?
  * Secure symmetric key exchange
  * digital signatures
#### _**public key cryptography is one of the most important inventions in the history of computers**_
### Secure Key Exchange
* basically using public keys and sending and encrypting and decrypting, both sides get what they need
### HTTPS/TLS Server Authentication
* Client to verify id of Server:
  * **Public Key Certificates:** Digital Certificates = Public Key + Key Owner's ID info
  * during HTTPS/TLS Handshake, Server sends its public key to Client in public form
* **certificate authorities:** trusted organizations that give certificate files
* to obtain:
  * generate public/private key pair
  * secure private key
  * send public key and id info to certificate authority in **certificate signing request (CSR)**
## Concurrency
### Threads
* programs default to executing main, then terminating upon completing main
* For a program to do multiplethings cocurrently, it must create multiple **threads** of control to represent each action the program is working on
* programs start with **main thread**
 * additional threads can be created as needed
* each thread has its own runtime stack, allowing independent runtime from other stacks
### Parallel vs. Concurrent vs. Sequential Execution
* **concurrently**: multiple tasks + single CPU (core) -> OS swaps which task is executing to allow each to run
* **parallel**: multiple CPUs (cores) -> OS runs tasks at the _same time_
* **sequentially**: each task has to run to completion before another can start
### Thread Pools
* in order to be more efficient, reuse previous threads
* this allows for more tasks to run in the background
* **thread pool**:
 - Create number of threads at initialization before needed, and store in list
 - when run task on another thread
  - if another thread available in pool, run task on one of the available threads
  - if no thread currently available in pool, add task to **task queue**
 - when task finishes running
   - if tasks in queue, remove next in queue and run it on available thread
   - if no tasks in queue, put thread back in pool
* Task submitters -> Task Queue -> Thread Pool
* **Executor Service**: Task Queue & Thread Pool
* **Executors class**: has several methods for creating pre-configured thread pool instances
* Two ways to write executable tasks for **ExecutorService**:
 1. write class implementing **Runnable** interface
    * code goes to **public void run()** method
    * preferred when you don't need to return a result
 3. write class implementing **Callable <V>** interface
    * code goes in **public V call()** method
    * preferred when need to return result
### Race Conditions/Hazards
* correctness of program depends on relative timing of interleaving multiple threads/processes
  * nondeterministic behavior
  * sometimes works or not
  * threads interleave differently each run
* Caused by shared resources accessed concurrently by multiple threads
  * in-memory data structs
  * io
    * files
    * databases
    * sockets
    * terminal/screen
### Thread-safe code
1. **Database transactions**
   * normally, single SQL statements commit immediately
   * sequence of multiple SQL statements executed as atomic unit through **database transactions**
   * serialize transactions that are concurrently executed by different threads/processes
2. **Synchronized methods (Java)**
   * **critical section**: part of code only one thread should run at a time
   * **synchronized** prevents multiple threads from entering the method on the same object at the same time
3. **Synchronized code blocks (Java)**
 * ensure single-threaded access to method on single object
4. **Atomic operations**
   * **synchronized code blocks** protect **critical sections**
   * read or write var vals with one CPU operation -> eliminate use of **synchronized methods** or **synchronized code blocks**
### Race Conditions in Chess
* Spark creates multiple thread objects behind the scenes to handle multiple incoming client reqs/messages concurrently
* websocket library (used by Chess client) creates additional threads to handle WS messages sent by server
* possible race conditions:
  - Multiple users registering concurrently with the same username
  - Multiple users concurrently claiming the same side in the same game
  - Multiple users concurrently joining or leaving a game (even different ones)
### Race Conditions in Chess Server
* Multiple threads writing to same web socket simultaneously
  * use "game locks" so the server processes only one web socket message per game at a time
* Too many reqs/messages at a time takes all of the database connections
  * use "connection pool" to limit how many connections can be open at a time
### Race Conditions in Chess Client
* two threads:
  1. Main - processes user input
  2. Web socket - processes messages from server
* Both threads read/write to local Game object simultaneously
  * synchronize all code reading/writing Game object -> only one thread accesses at a time
* Both threads print output to terminal simultaneously (draw board and print messages)
  * synchronize all code printing output to terminal -> only one thread writes to terminal at a time
## Command-line Builds
