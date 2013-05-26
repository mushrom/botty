all: javafiles

javafiles:
	javac *.java

test:
	java BotMain

clean:
	rm *.class

.PHONY: all
