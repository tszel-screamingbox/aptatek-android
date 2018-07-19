# Aptatek-android


### Rules
  * If you need a new third party library don't forget to add [Dependencies](#dependencies) section with it's license reference
  * Follow MVP architecture:
    * Android framework related code can't be in the presenters
    * Fragments and Activities are responsible for just UI and interaction handling
  * Commit your work as often as a part of a task is ready
  * Always check warnings before commit
  * When you start a new task create a new branch from Development
  * Before Pull Request always merge Development to your branch and resolve all conlficts
  * When you receive a feedback to your PR handle it as a high priority task
  * After an accepted PR delete your source branch
  * We use CircleCI for continous integration
  
### Conventions
  * Use only english notation on properties
  * Avoid member prefixes: ```mTitleTextView -> titleTextView```
  * When binding a view use their type as a postfix: ```TextView titleTextView```
  * Use lambda expressions if possible
  * Use stream API instead of cycles if possible
  * Always format your code and remove unused imports
  * Never use hardcoded values in .xml files
  * Keep the alphatbetic order in ```strings.xml```
  * Use styles.xml
  
### Dependencies
  * Mosby [License](https://github.com/sockeqwe/mosby/blob/master/LICENSE)
  * Butterknife [License](https://github.com/JakeWharton/butterknife/blob/master/LICENSE.txt)
  * RxJava [License](https://github.com/ReactiveX/RxJava/blob/2.x/LICENSE)
  * IxJava [License](https://github.com/akarnokd/ixjava/blob/1.x/LICENSE)
  * Dagger2 [License](https://github.com/google/dagger/blob/master/LICENSE.txt)
  * Glide [License](https://github.com/bumptech/glide/blob/master/LICENSE)
  * Crahslytics [License](http://try.crashlytics.com/terms/)
  * Google/Auto [License](https://github.com/google/auto/blob/master/LICENSE.txt)
  * Parceler [License](https://github.com/johncarl81/parceler/blob/master/LICENSE)
  * Leakcanary [License](https://github.com/square/leakcanary/blob/master/LICENSE.txt)
  * Timber [License](https://github.com/JakeWharton/timber/blob/master/LICENSE.txt)
  * Datafactory [License](https://github.com/andygibson/datafactory/blob/master/license.txt)
  * Cwac-saferoom [License](https://github.com/commonsguy/cwac-saferoom/blob/master/LICENSE)
  * ActivityStarter [License](https://github.com/MarcinMoskala/ActivityStarter/blob/master/LICENSE.txt)
  * MpAndroidChart [License](https://github.com/PhilJay/MPAndroidChart/blob/master/LICENSE)
