# Associate Android Developer Certification Study Guide

[Get info on certification](https://developers.google.com/training/certification/associate-android-developer/#info)

## Testing and Debugging

Writing tests to verify that the application's logic and user interface are performing as expected, and executing those tests using the developer tools. Candidates should be able to analyze application crashes, and find common bugs such as layout errors and memory leaks. This includes working with the debuggers to step through application code and verify expected behavior.

### Write and execute local JVM unit tests

If your unit test has no dependencies or only has simple dependencies on Android, you should run your test on a local development machine. This testing approach is efficient because it helps you avoid the overhead of loading the target app and unit test code onto a physical device or emulator every time your test is run. Consequently, the execution time for running your unit test is greatly reduced. With this approach, you normally use a mocking framework, like Mockito, to fulfill any dependency relationships.

[Cultured Commit](https://github.com/amohnacs15/Cultured/commit/c2bf1bdd46a1b6cef93a249a14c116cbb7a63d59#commitcomment-23188288)

[Google docs reference](https://developer.android.com/training/testing/unit-testing/local-unit-tests.html#setup)
