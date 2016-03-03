# fragmentrouter

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
    compile 'com.github.icarus-sullivan:fragmentrouter:+'
}
```

The repo is open, feel free to contribute. 
