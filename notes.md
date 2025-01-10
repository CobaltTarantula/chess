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