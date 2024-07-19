SET WTKDIR=c:/wtk22
SET CP=%WTKDIR%/lib/cldcapi10.jar;%WTKDIR%/lib/midpapi20.jar;./lib/nds.jar
del .\output\*.* /Q
del .\output\openroguelike\*.* /Q
"c:/Program Files (x86)/Java/jdk1.5.0_06/bin/javac.exe" -source 1.3 -target 1.1 -bootclasspath %CP% .\src\main\java\openroguelike\*.java .\src\main\java\*.java
%WTKDIR%/bin/preverify -classpath %CP% .\src\main\java
del .\src\main\java\openroguelike\*.class /Q
del .\src\main\java\*.class /Q


