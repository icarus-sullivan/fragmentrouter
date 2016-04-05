# FragmentRouter v1.0.4

### What is it?
FragmentRouter is a simpler way to handle Fragment Navigation in Android projects. It operates like a Undo-Redo implementation making use of default Java Stacks to handle the navigation, but still relies on FragmentManager under the hood.

### How do I include it in my project?
FragmentRouter is hosted on jcenter, make sure that jcenter is in your _project_ build.gradle.

```
allprojects {
    repositories {
        jcenter()
    }
}
```

Then within your apps build.gradle add the following dependency.

```
dependencies {
    compile 'com.github.icarus-sullivan:fragmentrouter:1.0.4'
}
```

If you are using maven you can include the library with the following dependency.

```
<dependency>
  <groupId>com.github.icarus-sullivan</groupId>
  <artifactId>fragmentrouter</artifactId>
  <version>1.0.4</version>
  <type>pom</type>
</dependency>
```

# API Calls

### Configuration
```
public class AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentRouter.init(this);          // FragmentRouter must use an AppCompatActivity
    }

}
```

### Navigation
```
FragmentRouter.navigateTo( int container, Fragment supportFragment );

FragmentRouter.navigateBack( int container );

FragmentRouter.navigateForward( int container );
```

### Listeners 
```
// adding FragmentChangeListener will give you the current fragment
FragmentRouter.addFragmentChangeListener(new FragmentRouter.FragmentChangeListener() {
    @Override
    public void onFragmentChanged(Fragment fragment) {
        Log.d("====>", fragment.toString());
    }
});
```

### Clearing Navigation
```
FragmentRouter.clearBackNav();      // will remove all back fragments

FragmentRouter.clearForwardNav();   // will remove all forward fragments

FragmentRouter.clearAllNav();       // will remove all navigation fragments, except the current fragment
```


The repo is open, feel free to contribute. 
