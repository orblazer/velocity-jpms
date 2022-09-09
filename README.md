# Velocity JPMS (Java Platform Module System or Jigsaw)

This repo is a proof of concept for implementing JPMS on [Velocity](https://velocitypowered.com/) plugins.

## Current State (`BROKEN`)

This is currently broken due to the fatjar for [`app`](/app), so that generate following error because the [`api`](/api)
is not considerate as a module.

```log
[ERROR]: Couldn't load plugins
java.lang.module.FindException: Module org.example.jpms.api not found, required by org.example.plugin
        at java.lang.module.Resolver.findFail(Resolver.java:893) ~[?:?]
        at java.lang.module.Resolver.resolve(Resolver.java:192) ~[?:?]
        at java.lang.module.Resolver.resolve(Resolver.java:141) ~[?:?]
        at java.lang.module.Configuration.resolveAndBind(Configuration.java:492) ~[?:?]
        at java.lang.module.Configuration.resolveAndBind(Configuration.java:298) ~[?:?]
        at org.example.jpms.app.plugin.PluginManager.loadPlugins(PluginManager.java:59) ~[app-1.0-SNAPSHOT-all.jar:1.0-SNAPSHOT]
        at org.example.jpms.app.App.loadPlugins(App.java:72) ~[app-1.0-SNAPSHOT-all.jar:1.0-SNAPSHOT]
        at org.example.jpms.app.App.start(App.java:43) ~[app-1.0-SNAPSHOT-all.jar:1.0-SNAPSHOT]
        at org.example.jpms.app.Bootstrap.main(Bootstrap.java:20) ~[app-1.0-SNAPSHOT-all.jar:1.0-SNAPSHOT]
```

For fix that issue we need to use `named module` for [`app`](/app) but that add some difficulties for run the server.
The following list explain what it's possible for achieve that.

1. Have a launcher jar that download/extract all needed libraries/modules (like [PaperClip](https://github.com/PaperMC/Paperclip))

    **PS**: without that we need copy all required libraries into an folder and run the app with something like that :

    ```sh
    java -p libs:. -m org.example.jpms
    ```

2. Keep the fatjar but download/extract api into an folder and load it into plugin `ModuleLayer`.
