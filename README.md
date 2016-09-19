[ ![Download](https://api.bintray.com/packages/icarus-sullivan/maven/fragmentrouter/images/download.svg) ](https://bintray.com/icarus-sullivan/maven/fragmentrouter/_latestVersion)

# FragmentRouter

### What is it?
FragmentRouter is a simpler way to handle Fragment Navigation in Android projects. It operates like a Undo-Redo implementation making use of position based traversal to handle the navigation, but still relies on FragmentManager under the hood.

### How do I include it in my project?
Add the follinowing gradle dependency.

```javascript
dependencies {
    compile 'com.github.icarus-sullivan:fragmentrouter:1.0.7'
}
```

If you are using maven you can include the library with the following dependency.

```xml
<dependency>
  <groupId>com.github.icarus-sullivan</groupId>
  <artifactId>fragmentrouter</artifactId>
  <version>1.0.7</version>
  <type>pom</type>
</dependency>
```

# API Calls

### Configuration
*The rest of the examples assume your FragmentRouter is named fooFragmentRouter
```java
public class Foo extends AppCompatActivity {

    // FragmentRoutes work across single Activities
    public static FragmentRouter fooFragmentRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FragmentRouter must use an AppCompatActivity
        fooFragmentRouter = new FragmentRouter( this );
    }

}
```

###  Updating the current Activity

In case your are using a single instance of FragmentRouter, perhaps at the application level, you can now change the AppCompatActivity that FragmentRouter is using.

_example_
```java
public class Foo2Activity extends AppCompatActivity {

    private FragmentRouter fooFragmentRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fooFragmentRouter = App.getFragmentRouter();
        // update activity
        fooFragmentRouter.into( this );

    }

}
```
Now any time you use the router, it will be loading fragments into this activity.

### Custom Animations
FragmentRouter has some default animations for fragment traversal.
* Forward navigation exits left, and enters right
* Backward navigation exits right, and enters left
* NavigateTo navigates in from the bottom, and out through the top
* If the fragment already exists, navigation is intelligent and will navigate depending on its placement from the current fragment
```java
fooFragmentRouter.withAnimations( @AnimRes int navBackIn, @AnimRes int navBackOut,
                               @AnimRes int navForwardIn, @AnimRes int navForwardOut,
                               @AnimRes int navPopIn, @AnimRes int navPopOut ) {
    // sets the animations for back, forward, or navigateTo
}
```

### Navigation
These are most of the APIs you can use to navigate the fragment stack easily.
```java
fooFragmentRouter.navigateTo( int container, Fragment supportFragment );

fooFragmentRouter.navigateBack( int container );

fooFragmentRouter.navigateForward( int container );
```

### Navigation Checks
Not sure if there is a fragment in the front or back? You can quickly check if there is forward or backward navigation with the following APIs.
```java
@Override
public void onBackPressed() {
    if( fooFragmentRouter.canGoBack() ) {
        fooFragmentRouter.navigateBack( R.id.container );
    } else {
        super.onBackPressed();
    }
}

...java

// Likewise you can check for forward traversal
boolean validForward = fooFragmentRouter.canGoForward();
```

### Listeners
Callbacks are common in Android land, so here is one to listen for fragment change states.
```java
// adding FragmentChangeListener will give you the current fragment
fooFragmentRouter.addFragmentChangeListener(new FragmentRouter.FragmentChangeListener() {
    @Override
    public void onFragmentChanged(Fragment fragment) {
        Log.d("====>", fragment.toString());
    }
});

// curious about what fragment you are currently viewing? Use this API

Fragment current = fooFragmentRouter.getCurrent();
```

### Clearing Navigation
```java
FragmentRouter.clearBackNav();      // will remove all back fragments

FragmentRouter.clearForwardNav();   // will remove all forward fragments

FragmentRouter.clearAllNav();       // will remove all navigation fragments, except the current fragment
```


The repo is open, feel free to contribute. 
