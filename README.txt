# java-cucumber-selenium

**Resources**
- [Cucumber-JVM](https://cucumber.io/docs/reference/jvm)
- [Cucumber-JVM API](http://cucumber.github.io/api/cucumber/jvm/javadoc/)
- [Zephyr-API](https://getzephyr.docs.apiary.io/)

### Setup

Install [IntelliJ](https://www.jetbrains.com/idea/download)
 - Install cucumber plugin
 - Preferences => Plugins => Cucumber for Java

**Environment variables**

Open a terminal and proceed with the following:
```
$ open ~/.bash_profile
```

Set environment variables
```
export JAVA_HOME=$(/usr/libexec/java_home)
```

Save changes, close profile, close and reopen terminal.

**Homebrew**

```
$ ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

After installation:
```
$ brew doctor
Your system is ready to brew
```

**Node**
```
$ brew install node
```

**Git**
```
$ brew install git
```


Running tests

**IntelliJ**

Create run configuration. This will allow you to run Scenarios by right clicking them and selecting run in IntelliJ.
- Run => Edit Configurations
- Create new Cucumber Java run configuration
    - Main class: cucumber.api.cli.Main
    - Glue: cucumber.steps cucumber.support
    - Feature or folder path: /path/to/features

Update jira's execution results
```
$ ./gradlew -DcucumberReportLocation="../target/cucumber-report/cucumber.json" -Dportal="portalName" -Ddevice="deviceName" -Dusername="jiraUsername" -Dpassword="jiraPassword" clean test --tests *runner.updateExecutionResults -i

```

Sync zephyr versions with zephyr's Master version
```
$ ./gradlew -DversionToCopy="MasterVersion"  -Dportal="PortalName" -Ddevices="devices" -Dusername="jiraUsername" -Dpassword='jira.password' clean test --tests *runner.updateZephyrVersions -i

```


Add steps from cucumber feature files to Zephyr test cases
```
$ ./gradlew -DgherkinFileLocation="featureFileLocation" -Dusername='jiraUsername' -Dpassword='jiraPassword' clean test --tests *runner.updateTestCases -i
```

