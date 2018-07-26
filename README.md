# java-jira-execution-updater

**Resources**
- [Zephyr-API](https://getzephyr.docs.apiary.io/)
- [Cucumber-JVM](https://cucumber.io/docs/reference/jvm)
- [Cucumber-JVM API](http://cucumber.github.io/api/cucumber/jvm/javadoc/)

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

**Terminal**

Update Zephyr test execution results
```
$ ./gradlew -DcucumberReportLocation="../target/cucumber-report/cucumber.json" -Dportal="portalName" -Ddevice="deviceName" -Dusername="jiraUsername" -Dpassword="jiraPassword" clean test --tests *runner.updateExecutionResults -i

```

Sync Zephyr versions with Zephyr's Master version
```
$ ./gradlew -DversionToCopy="MasterVersion"  -Dportal="PortalName" -Ddevices="devices" -Dusername="jiraUsername" -Dpassword='jira.password' clean test --tests *runner.updateZephyrVersions -i

```


Add steps from Cucumber feature files to Zephyr test cases
```
$ ./gradlew -DgherkinFileLocation="featureFileLocation" -Dusername='jiraUsername' -Dpassword='jiraPassword' clean test --tests *runner.updateTestCases -i
```

