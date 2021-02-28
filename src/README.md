# Big data entity resolution - 2021

## Building
1. Install JDK8; confirmed to work with (and developed with) OpenJDK13
2. (Windows) Set system variable JAVA_HOME to java installation folder without \bin\ at the end
3. (Windows) Add %JAVA_HOME%\bin to system variable Paths
4. Go to .\src\ folder
5. Run <code>javac Project.java</code>
6. Project builds in the same directory. The built project can be used with <code>java Project &lt;path to csv file&gt; &lt;path to csv file&gt; &lt;jaccard limit to discard values. attribute clustering will halve this&gt;</code>

With example data from the src directory: <code>java Project ..\DataSet\Dataset1.csv ..\DataSet\Dataset2.csv 0.3</code>
