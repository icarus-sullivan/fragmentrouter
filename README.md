# FragmentRouter v1.0.4

### What is it?
FragmentRouter is a simpler way to handle Fragment Navigation in Android projects. It operates like a Undo-Redo implementation making use of default Java Stacks to handle the navigation, but still relies on FragmentManager under the hood.

### How do I use it?
FragmentRouter currently uses jitpack.io for its use in Android Projects. To include it in your project add the following to your _projects build.gradle_.

```
allprojects {
    repositories {
        jcenter()
        maven {
            url "https://jitpack.io"
        }
    }
}
```

Then within your apps build.gradle add the following dependency.

```
dependencies {
    compile 'com.github.icarus-sullivan:fragmentrouter:1.0.4'
}
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
