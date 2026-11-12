@echo off

REM Copyright 2020, Gerwin Klein, Régis Décamps, Steve Rowe
REM SPDX-License-Identifier: BSD-3-Clause
REM
REM Please adjust JFLEX_HOME and JFLEX_VERSION to suit your needs
REM (please do not add a trailing backslash)

if not defined JFLEX_HOME set JFLEX_HOME=C:\JFLEX
if not defined JFLEX_VERSION set JFLEX_VERSION=1.10.0-SNAPSHOT

REM Use JAVA_HOME if set, otherwise plain java in the PATH
set JFLEX_JAVA=java
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" set JFLEX_JAVA="%JAVA_HOME%\bin\java.exe"

%JFLEX_JAVA% -Xmx128m -jar "%JFLEX_HOME%\lib\jflex-full-%JFLEX_VERSION%.jar" %*
