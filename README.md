# ZeroToDeploy

This project uses Gradle with the provided wrapper and requires **JDK 21**.

## Building

Use the Gradle wrapper to build the executable jar:

```bash
./gradlew bootJar
```

The jar will be created at `build/libs/ROOT.jar`.

## Running

Run the jar with Java:

```bash
java -jar build/libs/ROOT.jar
```

If you need to run the command with `sudo` and redirect output to a file, wrap it in `sudo bash -c`:

```bash
sudo bash -c 'java -jar build/libs/ROOT.jar > /path/to/app.log 2>&1 &'
```
