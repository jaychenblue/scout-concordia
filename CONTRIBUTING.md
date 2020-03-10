# Guidelines for Contributors

Requirements
User stories vs developer stories
User stories are what we will implement. For example, if someone’s working on outdoor directions, and they realize that they need to implement a search bar, they need to create a user story and link it to the corresponding epic. 

Developer stories pertain to documentation. All developer stories are to be put in the developer backlog and have the documentation tag or research tag. 
Epics:
Launching the application
Display campus plan
Multilingual application
Display outdoor directions from point A to point B
Display indoor directions from point A to point B
Google Calendar 
Display the nearest facilities of interest
What to include when creating an issue
Title and ID that match the user story
A detailed description (what this feature will do, who will use it, etc.)
Issues it is related to
Which epic it is part of
Acceptance test
Things that should not happen 
Mockups 
Implementation details (when completed) 

# Testing
Types of tests: 
Acceptance (this is added in the issue when we first create it)
Unit tests - JUnit
Code coverage 
System tests
Bug reports
Unit testing
https://www.guru99.com/unit-testing-guide.html
Code coverage
This will demonstrate the percentage of code that was actually tested. 
https://medium.com/android-testing-daily/code-coverage-dc1e3d0c70
System testing
System testing is black box  testing. 
We will be using espresso.
Types of system testing we will be doing:
Usability testing
Focuses on the user’s ease to use the application
Flexibility in handling controls and ability of the system to meet its objectives

Regression testing
Testing to make sure none of the changes made over the course of the development process have caused new bugs 

# Finding Bugs

How to report bugs
Create an issue 
Enter the title of the bug with an ID
ID should be D-# (make sure it follows the sequence)
Paste this whole block below and add detailed information + screenshots
Add the “bug” tag

# Code Reviews

How to do code reviews
Things we need to look for:
Structure and logic (static analysis)
Style
Performance
Test coverage
Design
Readability 
Functionality 
Scope and size
Make sure the changes are self-contained, do not make a pull request for a whole feature, but rather parts of a feature that build out the feature 

Does this code accomplish the author’s purpose?
Every change should have a purpose, confirm whether or not the changes actually correspond to the user’s intentions (kind of acceptance testing)

Check legibility
Check if the code is easy to read or not 
Check for variables, do they make sense? 
Are there enough comments? 
Are the commits good? 

Check if there are any tests
If there are no tests, ask the dev to write some 


Static analysis:
Done without executing the program
http://www.aanandshekharroy.com/articles/2019-01/static-code-analysis-using-android-studio

# Commit Conventions

What to commit, when to commit, and how to commit 
Commit every time you’re done with a task or a fix:
These are Atomic Commits
This makes it easier to track commits and to revert in case of a bad commit 

Commit messages:
Commit messages should be detailed and include the link the associated issue to it

# Working on Branches

Feature branches
Feature branches are essentially separate branches in the repo used to implement a single feature
If you are going to be working on a new feature or user story, create a new branch
If you are working on fixing a branch, create a new branch using the following convention: bugfix-whatyourefixing 

Pull requests
When creating pull requests, make sure to include as many details as you can.

# Quality Metrics

Quality measurements
SonarQube - https://www.sonarqube.org/
https://docs.sonarqube.org/latest/user-guide/metric-definitions/

Metrics we will measure:
Reliability
Maintainability
Duplications
Size
Complexity
Documentation 
Issues
