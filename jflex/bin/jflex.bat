@echo off
REM Please adjust JFLEX_HOME and JFLEX_VERSION to suit your needs
REM (please do not add a trailing backslash)

if not defined JFLEX_HOME set JFLEX_HOME=C:\JFLEX
if not defined JFLEX_VERSION set JFLEX_VERSION=1.7.1-SNAPSHOT

IF "%1"=="" GOTO GUI
java -Xmx128m -jar "%JFLEX_HOME%\lib\jflex-full-%JFLEX_VERSION%.jar" %*
GOTO END

:GUI
java -Xmx128m -jar "%JFLEX_HOME%\lib\jflex-full-%JFLEX_VERSION%.jar" jflex.gui.MainFrame

:END
