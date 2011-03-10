OPTS=-g

all: Account.class Branch_Server.class topology.class ATMGUI.class

Account.class: Account.java
	javac $(OPTS) Account.java

Branch.class: Branch.java
	javac $(OPTS) Branch.java

Branch_Server.class: Branch_Server.java Branch.class Account.class
	javac $(OPTS) Branch_Server.java

topology.class: topology.java Branch_Server.class Branch.class
	javac $(OPTS) topology.java

ATMGUI.class: BankGUI/src/ATMGUI.java BankGUI/src/ATMGUI.form
	ant -f BankGUI/build.xml
	cp BankGUI/build/classes/ATMGUI*.class ./
