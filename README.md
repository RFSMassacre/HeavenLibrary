# HeavenLibrary
HeavenLibrary is a Minecraft Java server plugin library that aims to make it simple to extend into any Minecraft Java server platform.

## Pre-release Artifacts
You can quickly download an artifact from a commit if you don't know how to compile the source code. Check it out [here](https://github.com/RFSMassacre/HeavenLibrary/actions/workflows/maven-publish.yml?query=branch%3Amaster+is%3Asuccess)!

## Contributing
I'm always looking for others to add more features or implementations to other Minecraft Java server platforms. Pull requests and forks are welcome!

## Usage
Everything is aimed to be as easy to extend for any platform. Right now the only platforms available are **Paper** and **Velocity**. I dropped support for **Bungeecord** and **Waterfall** since they are deprecated.

**Make sure to use the proper class for your platform.**

### File Data
- **Paper**
```Java
PaperConfiguration config = new PaperConfiguration(this, "", "config.yml");
PaperLocale locale = new PaperLocale(this, "", "locale.yml");
```
- **Velocity**
```Java
VelocityConfiguration config = new VelocityConfiguration(this, "", "config.yml");
VelocityLocale locale = new PaperLocale(this, "", "locale.yml");
```
All the configuration files need to be provided in the `resources` folder in your plugin's jar. If it does not exist, it will give an error on startup.

### Simple Commands
If you intend to use a no-argument command, feel free to use a SimpleCommand for your platform.
- **Paper**
```Java
public class ExampleSimpleCommand extends SimplePaperCommand
{
    public ExampleSimpleCommand()
    {
        super(ExamplePlugin.getInstance(), "example");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        //Do something.
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args)
    {
        //Make sure to actually give the auto-completed arguments you need.
        return Collections.emptyList();
    }
}
```
- **Velocity**
```Java
public class ExampleSimpleCommand extends SimpleVelocityCommand
{
    public ExampleSimpleCommand()
    {
        super(ExamplePlugin.getInstance(), "example");
    }

    @Override
    public void execute(Invocation invocation)
    {
        CommandSource source = invocation.source();
        String[] arguments = invocation.arguments();
        //Do something.
    }

    @Override
    public List<String> suggest(Invocation invocation)
    {
        //Make sure to actually give the auto-completed arguments you need.
        return Collections.emptyList();
    }
}
```
This expects you to make your plugin class's instance for the platform you need it for, but you can easily just have it pass the instance as a parameter.

### Complex Commands
If you need each argument to do a LOT of code, then extend from the Command of your platform. It extends SimpleCommand and implements some things already for you.

It handles permissions already for you and will already execute the first SubCommand when no argument is given.
- **Paper**
```Java
public class ExampleCommand extends PaperCommand
{
    public ExampleCommand()
    {
        super(ExamplePlugin.getInstance(), "example");

        addSubCommand(new ExampleSubCommand());
    }
    
    private class ExampleSubCommand extends PaperSubCommand
    {
        public ExampleSubCommand()
        {
            super("command");
        }
        
        @Override
        protected void onRun(CommandSender sender, String[] args)
        {
            //Do something.
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, String[] args)
        {
            //Make sure to actually give the auto-completed arguments you need.
            return Collections.emptyList();
        }
    }
}
```
- **Velocity**
```Java
public class ExampleCommand extends VelocityCommand
{
    public ExampleCommand()
    {
        super(HeavenChat.getInstance(), "example");

        addSubCommand(new ExampleSubCommand());
    }
    
    private class ExampleSubCommand extends VelocitySubCommand
    {
        public CurrentListCommand()
        {
            super("command");
        }

        @Override
        protected void onRun(CommandSource sender, String[] args)
        {
            //Do something.
        }

        @Override
        public List<String> onTabComplete(CommandSource sender, String[] args)
        {
            //Make sure to actually give the auto-completed arguments you need.
            return Collections.emptyList();
        }
    }
}
```