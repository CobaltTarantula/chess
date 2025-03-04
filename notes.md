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