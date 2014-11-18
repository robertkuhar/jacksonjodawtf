JacksonJodaWTF
=========

The org.joda.time.DateTime object deserialized from JSON through the Jackson JodaModule() is not .equals to the org.joda.time.DateTime that produced the original JSON.  This feels like a bug to me.

The Gradle Wrapper is installed so to build simply......

    $ ./gradlew
    :clean
    :compileJava UP-TO-DATE
    :processResources UP-TO-DATE
    :classes UP-TO-DATE
    :jar
    :assemble
    :compileTestJava
    :processTestResources UP-TO-DATE
    :testClasses
    :test
    
    Gradle test > jodathroughjacksonwtf.JodaThroughJacksonTest.iso8601ThroughJoda FAILED
        java.lang.AssertionError at JodaThroughJacksonTest.java:71
    
    2 tests completed, 1 failed
    :test FAILED
    
    FAILURE: Build failed with an exception.
    
    * What went wrong:
    Execution failed for task ':test'.
    > There were failing tests. See the report at: file:///Users/robert.kuhar/work/jacksonjodawtf/build/reports/tests/index.html
    
    * Try:
    Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output.
    
    BUILD FAILED
    
    Total time: 4.305 secs

What's up with that?