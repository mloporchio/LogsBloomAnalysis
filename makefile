#
#	File:		makefile
#	Author:		Matteo Loporchio
#	Thanks to:	https://www.cs.swarthmore.edu/~newhall/unixhelp/javamakefiles.html
#

JC=javac
JFLAGS=-cp "src:lib/gson-2.10.jar"
SOURCE=src
TARGET=bin

default: 
	$(JC) $(JFLAGS) $(SOURCE)/*.java -d $(TARGET)

clean:
	$(RM) $(TARGET)/*.class