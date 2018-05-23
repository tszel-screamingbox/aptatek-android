# Aptatek-android

### Conventions
  * Use only english notation on properties
  * Avoid member prefixes: ```mTitleTextView -> titleTextView```
  * When binding a view use their type as a postfix: ```TextView titleTextView```
  * Use lambda expressions if possible
  * Use stream API instead of cycles if possible
  * Always format your code and remove unused imports
  * Never use hardcoded values in .xml files
  * Keep the alphatbetic order in ```strings.xml```

### Rules
  * Always check warnings before commit
  * Follow MVP architecture:
    * Android framework related code can't be in the presenters
    * Fragments and Activities are responsible for just UI and interaction handling
  * Commit your work as often as a part of a task is ready
  * When you start a new task create a new branch from Dev
  * Before Pull Request always merge Development to your branch and resolve all conlficts
  * After an accepted PR delete your source branc
