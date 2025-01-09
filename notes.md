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
* ```git checkout .``` go back to previous version
  * if you use ```IDENTIFIER``` instead of ```.``` then that git commit is the version that will be restored