OPTS=-g

all: Account.class Branch_Server.class Branch_Server_Process.class GUI_Server_Process.class topology.class ATMGUI.class

clean:
	rm *.class
	rm *.log

Account.class: Account.java
	javac $(OPTS) Account.java

Branch.class: Branch.java
	javac $(OPTS) Branch.java

Branch_Server_Process.class: Branch_Server_Process.java Branch.class
	javac $(OPTS) Branch_Server_Process.java

ATMGUI.class: BankGUI/src/ATMGUI.java BankGUI/src/ATMGUI.form
	ant -f BankGUI/build.xml
	cp BankGUI/build/classes/ATMGUI*.class ./

GUI_Server_Process.class: GUI_Server_Process.java ATMGUI.class
	javac $(OPTS) GUI_Server_Process.java

Branch_Server.class: Branch_Server.java Branch_Server_Process.class Branch.class
	javac $(OPTS) Branch_Server.java

topology.class: topology.java Branch_Server.class Branch.class
	javac $(OPTS) topology.java
