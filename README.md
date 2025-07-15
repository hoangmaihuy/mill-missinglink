# mill-missinglink

A Mill plugin for [missinglink](https://github.com/spotify/missinglink), ported from [sbt-missinglink](https://github.com/scalacenter/sbt-missinglink)

## Usage

Add the following lines in `build.mill`:

```scala
//| mvnDeps:
//| - io.github.hoangmaihuy::mill-missinglink::<latest-version>
```

Extends `MissinglinkCheckModule`

```scala
import io.github.hoangmaihuy.missinglink.*

object example extends MissinglinkCheckModule
```

Then, run `missinglinkCheck` command

```bash 
> mill example.missinglinkCheck
```

Currently, `missinglinkCheck` only checks conflicts in **Runtime** scope.

### Do not fail on conflicts

By default, the plugin fails the build if any conflicts are found.
It can be disabled by the `missinglinkFailOnConflicts` setting:

```scala
object example extends MissinglinkCheckModule {
  override def missinglinkFailOnConflicts = false
}
```

### Ignore conflicts in certain packages

Conflicts can be ignored based on the package name of the class that has the conflict.
There are separate configuration options for ignoring conflicts on the "source" side of the conflict and the "destination" side of the conflict.
Packages on the source side can be ignored with `missinglinkIgnoreSourcePackages` and packages on the destination side can be ignored with `missinglinkIgnoreDestinationPackages`:

```scala
object example extends MissinglinkCheckModule {
  override def missinglinkIgnoreDestinationPackages = Seq(IgnoredPackage("com.google.common"))
  override def missinglinkIgnoreSourcePackages = Seq(IgnoredPackage("com.example"))
}
```

By default, all subpackages of the specified package are also ignored, but this can be disabled by the `ignoreSubpackages` field: `IgnoredPackage("test", ignoreSubpackages = false)`.

### Excluding some dependencies from the analysis

You can exclude certain dependencies using `DependencyFilter`:

```scala
object example extends MissinglinkCheckModule {
  override def missinglinkExcludedDependencies = Seq(DependencyFilter(organization = "com.google.guava"))
  override def missinglinkExcludedDependencies = Seq(DependencyFilter(organization = "ch.qos.logback", name = "logback-core"))
}
```

## More information

This plugin was ported from ported from [sbt-missinglink](https://github.com/scalacenter/sbt-missinglink). Core functions were copied from `sbt-missinglink` with some modifications to work with Mill.

You can find more information about the problem statement, caveats and
limitations, etc. in the upstream project
[missinglink](https://github.com/spotify/missinglink).

## Licenses

This software is released under the Apache License 2.0. More information in the file LICENSE distributed with this project.
