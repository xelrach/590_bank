OPTS=-g

all: Branch_Server.class topology.class ATMGUI.class

Branch_Server.class: Branch_Server.java
	javac $(OPTS) Branch_Server.java

topology.class: topology.java Branch_Server.class
	javac $(OPTS) topology.java

ATMGUI.class: BankGUI/src/ATMGUI.java BankGUI/src/ATMGUI.form
	ant -f BankGUI/build.xml
	cp BankGUI/build/classes/ATMGUI*.class ./
