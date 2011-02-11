    Start the GUI program like this:

java bank/ATMGUI
java bank/ATMGUI 192.168.0.1
java bank/ATMGUI 192.168.0.1 1234
java bank/ATMGUI 192.168.0.1 1234 01


    192.168.0.1 is the IP address of the server this GUI is supposed to connect. 1234 is the port number it will use. 01 is the ID of this GUI, which will be 00 if ignored. 


     acnt num only accepts account number like 12.11111(two digits, dot, 5 digits). 

IMPORTANT:

In:
     private void QueryBtnActionPerformed(java.awt.event.ActionEvent evt)                                    
There are two codes like these:

            //packet=new DatagramPacket(msg, msg.length);
            //socket.receive(packet);

delete "//" if you want it to receive messages.
     The same thing is Withdraw, Deposit and Transfer.

Let me if any bug is found. My email address is hguo@email.unc.edu