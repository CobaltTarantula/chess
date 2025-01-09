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