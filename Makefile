OPTS=-g

all: Account.class Branch.class Branch_Server.class Branch_Server_Process.class topology.class ATMGUI.class Snapshot.class

clean:
	rm -f *.class
	rm -f *.log
	rm -f *.log.*

Account.class: Account.java
	javac $(OPTS) Account.java

Branch.class: Branch.java
	javac $(OPTS) Branch.java

Branch_Server.class: Branch_Server.java Branch_Server_Process.class Branch.class Snapshot.class
	javac $(OPTS) Branch_Server.java

Branch_Server_Process.class: Branch_Server_Process.java Branch.class
	javac $(OPTS) Branch_Server_Process.java

ATMGUI.class: BankGUI/src/ATMGUI.java BankGUI/src/ATMGUI.form
	ant -f BankGUI/build.xml
	cp BankGUI/build/classes/ATMGUI*.class ./

Snapshot.class: Snapshot.java Account.class
	javac $(OPTS) Snapshot.java

topology.class: topology.java Branch_Server.class Branch.class
	javac $(OPTS) topology.java


test: UnitTest.java all
	javac $(OPTS) UnitTest.java
	java UnitTest
