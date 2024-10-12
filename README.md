# HeavenLibrary
HeavenLibrary is a Minecraft Java server plugin library that aims to make it simple to extend into any Minecraft Java server platform.

## Pre-release Artifacts
You can quickly download an artifact from a commit if you don't know how to compile the source code. Check it out [here](https://github.com/RFSMassacre/HeavenLibrary/actions/workflows/maven-publish.yml?query=branch%3Amaster+is%3Asuccess)!

## Contributing
I'm always looking for others to add more features or implementations to other Minecraft Java server platforms. Pull requests and forks are welcome!

## Usage
### Configuration
For the configuration file, just use the Configuration file for the respective platform.

If a value is null in the live file, it will take the value from the default file.

```Java
PaperConfiguration config = new PaperConfiguration(this, "", "config.yml");
```