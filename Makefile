OPTS=-g

all: Branch_Server.class topology.class BankGUI/build/classes/bank/ATMGUI.class

Branch_Server.class: Branch_Server.java
	javac $(OPTS) Branch_Server.java

topology.class: topology.java Branch_Server.class
	javac $(OPTS) topology.java

BankGUI/build/classes/bank/ATMGUI.class: BankGUI/src/bank/ATMGUI.java BankGUI/src/bank/ATMGUI.form
	cd bank
	ant
	cd ..
