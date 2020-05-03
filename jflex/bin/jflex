#! /bin/bash 
#
#     JFlex start script
#
# if Java is not in your binary path, you need to supply its
# location in this script. The script automatically finds 
# JFLEX_HOME when called directly, via binary path, or symbolic
# link. 
#
# Site wide installation: simply make a symlink from e.g.
# /usr/bin/jflex to this script at its original position
#
#===================================================================
#
# configurables:

# path to the java interpreter
# TODO: Use JAVA_HOME
JAVA=java

# Version of JFlex to execute
JFLEX_VERSION=1.8.2

# end configurables
#
#===================================================================
#

# calculate true location

PRG="$0"

# If PRG is a symlink, trace it to the real home directory

while [ -L "$PRG" ]
do
    newprg=$(ls -l ${PRG})
    newprg=${newprg##*-> }
    [ ${newprg} = ${newprg#/} ] && newprg=${PRG%/*}/${newprg}
    PRG="$newprg"
done

PRG=${PRG%/*}
JFLEX_HOME=${PRG}/.. 

# --------------------------------------------------------------------

$JAVA -Xmx128m -jar "$JFLEX_HOME"/lib/jflex-full-${JFLEX_VERSION}.jar $@
