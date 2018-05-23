# Aptatek-android

### Conventions
  * Use only english notation on properties
  * Avoid member prefixes
     ```java
mTitleTextView -> titleTextView
```
  * When binding a view use their type as a postfix
  ```java
TextView titleTextView;
```
  * Use lambda expressions if possible
  * Use stream API instead of cycles if possible
  * Always format your code and remove unused imports
  * Never use hardcoded values in .xml files
  * Keep the alphatbeitc order in ```strings.xml```

### Rules
  * Always check warnings before commit
  * Follow MVP architecture:
    * Android framework related code can't be in the presenters
    * Fragments and Activities are responsible for just UI and interaction handling
