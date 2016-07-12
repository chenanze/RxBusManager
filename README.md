# RxBusManager v1.0.0

### A useful manager for RxBus and RxJava

**Feature:**

- You can manager register and unregister of event handler with the tag which you have define. So you can unity of management for the unregister of event handler anywhere in your code.
- Easy to manager the Observable object (*Developing*)
- All  anonymous class are replace with `lambda` express. I used the library `me.tatarka.retrolambda`. So if you have used the `RxJava` in your work, you know how useful it is !
- You can continuous post event content object of List item one by one with a assign interval time of 300ms, until all item of ArrayList object had been posted.
- Finally it's easy to use.

----

## Install

1. In your top `build.gradle` file add 

   ```gradle
   dependencies {
   	// other ...
       classpath 'me.tatarka:gradle-retrolambda:3.2.5'
   }
   ```

2. In your module `build.gradle` file add

   ```gradle
   apply plugin: 'me.tatarka.retrolambda'

   android {
       compileOptions {
           sourceCompatibility JavaVersion.VERSION_1_8
           targetCompatibility JavaVersion.VERSION_1_8
       }
   }

   compile 'com.chenanze:rxbusmanager:1.0.0'
   ```
## Use 
### RxBus

- **Register event handler with define tag :**

  ```java
  RxBusManager.getInstance().t(TAG).on(C.EVENT_TEST, o -> {
                  Log.d(TAG, (String) o);
                  // something
              }).on(C.EVENT_TEST_1, o -> {
                  Log.d(TAG, (String) o);
                  // something
              })
  ```

- **Post event with define event name :**

  ```java
  RxBusManager.getInstance().post(C.EVENT_TEST, "test");
  ```

- **Unregister event handler with define tag :**

  ```java
  RxBusManager.getInstance().unregister(C.EVENT_TEST, TAG);
  ```

  So that the events which only have the same event name and tag name will be unregister.

- **Unregister the different event handler by the same tag :**

  ```java
  RxBusManager.getInstance().unregister(TAG);
  ```

- **Register event handler with default tag :**

  ```java
  RxBusManager.getInstance().on(C.EVENT_TEST, o -> {
                  Log.d(TAG, (String) o);
                  // something
              });
  ```

- **Unregister event handler with default tag :**

  ```java
  RxBusManager.getInstance().unregister(RxBusManager.DEFAULT_TAG);
  ```


- **Continuous post a List object one by one with assign interval time**

  ```java
  RxBusManager.getInstance().postDatasWithInterval(C.EVENT_RECYCLER_VIEW_ITEM_ANIMATION, datas, 300);
  ```

  That will continuous post the ArrayList item one by one with a assign interval time of 300ms, until all item of ArrayList object had been posted.

  ​


----

### RxJava

- **Please wait next version**


----



## Thanks

- [north2014/T-MVP](https://github.com/north2014/T-MVP)
- [ReactiveX/RxAndroid](https://github.com/ReactiveX/RxAndroid)
- [evant/gradle-retrolambda](https://github.com/evant/gradle-retrolambda)



License
-------

    Copyright 2016 by Mr Funcking License


