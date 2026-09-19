<!-- pre53-port-header:start -->
> **NeoForge 1.21.1 · Hex Casting `0.12.0-devel-pre-53` · Branch `pre53`**
>
> Upstream: https://github.com/FallingColors/MoreIotas.git · Commit `0d96e56c3cff5657cdeeb8394ba7b0c9c6a48387`.
>
> Unofficial source port by FluorineUCK; original authorship and licenses retained. See [PORTING.md](PORTING.md).
<!-- pre53-port-header:end -->

# MoreIotas

A minecraft mod which is an addon for [Hex Casting](https://github.com/gamma-delta/HexMod/), adding new types of iotas such as Strings. Intended to be approximately a library mod for other addons to use.

## Branches

* `main`: Active development for Minecraft 1.20.1.
* `1.19.2`: Long-term support for Minecraft 1.19.2.

## Maven

MoreIotas is available on https://maven.hexxy.media. To depend on it, add something like this to your Gradle build script:

```kotlin
repositories {
    maven {
        url = uri("https://maven.hexxy.media")
    }
}

dependencies {
    // common (xplat template)
    modImplementation("ram.talia.moreiotas:moreiotas-common-$minecraftVersion:$moreiotasVersion")

    // common (Architectury)
    modImplementation("ram.talia.moreiotas:moreiotas-fabric-$minecraftVersion:$moreiotasVersion")

    // fabric
    modImplementation("ram.talia.moreiotas:moreiotas-fabric-$minecraftVersion:$moreiotasVersion")

    // forge (ForgeGradle; see https://github.com/FallingColors/MoreIotas/issues/50)
    modImplementation(fg.deobf("ram.talia.moreiotas:moreiotas-forge-$minecraftVersion:$moreiotasVersion") {
        // kotlin
        isTransitive = false
        // groovy
        transitive = false
    })

    // forge (Architectury; see https://github.com/FallingColors/MoreIotas/issues/50)
    modImplementation("ram.talia.moreiotas:moreiotas-forge-$minecraftVersion:$moreiotasVersion") {
        // kotlin
        isTransitive = false
        // groovy
        transitive = false
    }
}
```
