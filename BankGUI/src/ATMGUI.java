/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ATMGUI.java
 *
 * Created on Feb 4, 2011, 11:55:23 AM
 */

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javax.swing.JOptionPane;
/**
 *
 * @author Hui Guo
 */
public class ATMGUI extends javax.swing.JFrame {

    /** Creates new form ATMGUI */

    public static String GUI_Id="";
    public static String IPadd="";
    public static String port02="";
    public static int guiListenPort;

    public GUIServer GS;
    //public static int MsgID=1000;

    public static boolean isAccount(String str)
    {
        Pattern pattern=Pattern.compile("[0-9][0-9]\\.[0-9][0-9][0-9][0-9][0-9]");
        return pattern.matcher(str).matches();
    }

    public static boolean isDouble(String str)
    {
        //Pattern pattern=
        //        Pattern.compile("^[-+]?(\\d+(\\.\\d\\*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?[dD]?$ ");
        //return pattern.matcher(str).matches();
        try
        {
            double temp=Double.parseDouble(str);
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }

    public ATMGUI() {
        initComponents();
        if (!IPadd.equals(""))
            IPTxt.setText(IPadd);
        if (!GUI_Id.equals(""))
            PortText01.setText(GUI_Id);
        if (!port02.equals(""))
            PortText02.setText(port02);
        int port=Integer.parseInt(PortText02.getText());
        GS=new GUIServer(port);
        (new Thread(GS)).start();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        WelcomeLbl = new javax.swing.JLabel();
        AvLbl = new javax.swing.JLabel();
        DepositBtn = new javax.swing.JButton();
        WithdrawBtn = new javax.swing.JButton();
        QueryBtn = new javax.swing.JButton();
        TransferBtn = new javax.swing.JButton();
        acnt01 = new javax.swing.JTextField();
        acnt02 = new javax.swing.JTextField();
        acnt03 = new javax.swing.JTextField();
        acnt04 = new javax.swing.JTextField();
        acnt05 = new javax.swing.JTextField();
        acntlbl01 = new javax.swing.JLabel();
        acntlbl02 = new javax.swing.JLabel();
        acntlbl03 = new javax.swing.JLabel();
        acntlbl04 = new javax.swing.JLabel();
        acntlbl05 = new javax.swing.JLabel();
        amount01 = new javax.swing.JTextField();
        amount02 = new javax.swing.JTextField();
        amount04 = new javax.swing.JTextField();
        amtlbl01 = new javax.swing.JLabel();
        amtlbl02 = new javax.swing.JLabel();
        amtlbl04 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        PortLbl01 = new javax.swing.JLabel();
        PortText01 = new javax.swing.JTextField();
        IPLbl = new javax.swing.JLabel();
        IPTxt = new javax.swing.JTextField();
        IPLbl02 = new javax.swing.JLabel();
        PortText02 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btnFail = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(" Bank of COMP590");

        WelcomeLbl.setFont(new java.awt.Font("Tahoma", 0, 18));
        WelcomeLbl.setForeground(new java.awt.Color(0, 153, 255));
        WelcomeLbl.setText("Welcome to Bank of COMP590!");

        AvLbl.setFont(new java.awt.Font("Tahoma", 0, 14));
        AvLbl.setForeground(new java.awt.Color(51, 0, 255));
        AvLbl.setText("Available actions:");

        DepositBtn.setText("Deposit");
        DepositBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DepositBtnActionPerformed(evt);
            }
        });

        WithdrawBtn.setText("Withdraw");
        WithdrawBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WithdrawBtnActionPerformed(evt);
            }
        });

        QueryBtn.setText("Query");
        QueryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QueryBtnActionPerformed(evt);
            }
        });

        TransferBtn.setText("Transfer");
        TransferBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TransferBtnActionPerformed(evt);
            }
        });

        acnt01.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        acntlbl01.setText("acnt num:");

        acntlbl02.setText("acnt num:");

        acntlbl03.setText("acnt num:");

        acntlbl04.setText("src acnt num:");

        acntlbl05.setText("dst acnt num:");

        amtlbl01.setText("amount:");

        amtlbl02.setText("amount:");

        amtlbl04.setText("amount:");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        PortLbl01.setText("GUI ID:");

        PortText01.setText("00");

        IPLbl.setText("Server IP:");

        IPTxt.setText("127.0.1.3");

        IPLbl02.setText("Server Port:");

        PortText02.setText("1234");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PortText01, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PortLbl01)
                    .addComponent(IPLbl)
                    .addComponent(IPTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(IPLbl02)
                    .addComponent(PortText02, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PortLbl01)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PortText01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(IPLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(IPTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(IPLbl02)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PortText02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnFail.setText("FAIL");
        btnFail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnFail, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnFail)
                .addContainerGap(115, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(AvLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(WelcomeLbl)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(DepositBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TransferBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(QueryBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(WithdrawBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(acntlbl02, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(acntlbl03, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(acntlbl04, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(acntlbl01, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(acnt03, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(acnt02, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(acnt01, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(acnt04, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(acntlbl05, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(acnt05, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(amount04)
                                .addComponent(amount02)
                                .addComponent(amount01, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(amtlbl01)
                            .addComponent(amtlbl02)
                            .addComponent(amtlbl04))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(WelcomeLbl)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(AvLbl)
                                .addGap(18, 18, 18)
                                .addComponent(acntlbl01))
                            .addComponent(amtlbl01))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(DepositBtn)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(acnt01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(amount01, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(WithdrawBtn)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(acntlbl02)
                                    .addComponent(amtlbl02))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(acnt02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(amount02, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(6, 6, 6)
                        .addComponent(acntlbl03)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(QueryBtn)
                            .addComponent(acnt03, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(acntlbl04)
                            .addComponent(amtlbl04))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TransferBtn)
                            .addComponent(acnt04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(amount04, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(acntlbl05)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(acnt05, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(31, 31, 31))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void DepositBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DepositBtnActionPerformed
        if (!isAccount(acnt01.getText()))
        {
            JOptionPane.showMessageDialog(null, "Invalid Acount Number!", "Oops", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isDouble(amount01.getText()))
        {
            JOptionPane.showMessageDialog(null, "Invalid Amount Number!", "Oops", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String msgtemp="c";
        //MsgID=(int)(Math.random()*10000);
        //msgtemp+=String.format("%04d", MsgID);
        msgtemp+=" d ";
        msgtemp+=acnt01.getText();
        msgtemp+=" ";
        msgtemp+=amount01.getText();
        byte[] msg=msgtemp.getBytes();
        try{
            DatagramSocket socket =new DatagramSocket();
            InetAddress address=InetAddress.getByName(IPTxt.getText());
            int port =Integer.parseInt(PortText02.getText());
            DatagramPacket packet=new DatagramPacket(msg,msg.length,address, port);
            socket.send(packet);

            //packet=new DatagramPacket(msg, msg.length);
            //socket.receive(packet);

            String received =new String(packet.getData(),0,packet.getLength());

            socket.close();
            JOptionPane.showMessageDialog(null, received,"Deliverd",JOptionPane.INFORMATION_MESSAGE);
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Can't connect to server!","Oops",JOptionPane.ERROR_MESSAGE);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_DepositBtnActionPerformed

    private void WithdrawBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WithdrawBtnActionPerformed
        if (!isAccount(acnt02.getText()))
        {
            JOptionPane.showMessageDialog(null, "Invalid Acount Number!", "Oops", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isDouble(amount02.getText()))
        {
            JOptionPane.showMessageDialog(null, "Invalid Amount Number!", "Oops", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String msgtemp="c";
        //MsgID=(int)(Math.random()*10000);
        //msgtemp+=String.format("%04d", MsgID);
        msgtemp+=" w ";
        msgtemp+=acnt02.getText();
        msgtemp+=" ";
        msgtemp+=amount02.getText();
        byte[] msg=msgtemp.getBytes();
        try{
            DatagramSocket socket =new DatagramSocket();
            InetAddress address=InetAddress.getByName(IPTxt.getText());
            int port =Integer.parseInt(PortText02.getText());
            DatagramPacket packet=new DatagramPacket(msg,msg.length,address, port);
            socket.send(packet);

            //packet=new DatagramPacket(msg, msg.length);
            //socket.receive(packet);

            String received =new String(packet.getData(),0,packet.getLength());

            socket.close();
            JOptionPane.showMessageDialog(null, received,"Deliverd",JOptionPane.INFORMATION_MESSAGE);
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Can't connect to server!","Oops",JOptionPane.ERROR_MESSAGE);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_WithdrawBtnActionPerformed

    private void TransferBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TransferBtnActionPerformed
        if (!isAccount(acnt04.getText()))
        {
            JOptionPane.showMessageDialog(null, "Invalid Source Account!", "Oops", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isAccount(acnt05.getText()))
        {
            JOptionPane.showMessageDialog(null, "Invalid Destination Account!", "Oops", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isDouble(amount04.getText()))
        {
            JOptionPane.showMessageDialog(null, "Invalid Amount Number!", "Oops", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String msgtemp="c";
        //MsgID=(int)(Math.random()*10000);
        //msgtemp+=String.format("%04d", MsgID);
        msgtemp+=" t ";
        msgtemp+=acnt04.getText();
        msgtemp+=" ";
        msgtemp+=acnt05.getText();
        msgtemp+=" ";
        msgtemp+=amount04.getText();
        byte[] msg=msgtemp.getBytes();
        try{
            DatagramSocket socket =new DatagramSocket();
            InetAddress address=InetAddress.getByName(IPTxt.getText());
            int port =Integer.parseInt(PortText02.getText());
            DatagramPacket packet=new DatagramPacket(msg,msg.length,address, port);
            socket.send(packet);

            //packet=new DatagramPacket(msg, msg.length);
            //socket.receive(packet);

            String received =new String(packet.getData(),0,packet.getLength());

            socket.close();
            JOptionPane.showMessageDialog(null, received,"Deliverd",JOptionPane.INFORMATION_MESSAGE);
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Can't connect to server!","Oops",JOptionPane.ERROR_MESSAGE);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_TransferBtnActionPerformed

    private void QueryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QueryBtnActionPerformed
        // TODO add your handling code here:
        if (!isAccount(acnt03.getText()))
        {
            JOptionPane.showMessageDialog(null, "Invalid Acount Number!", "Oops", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String msgtemp="c";
        //MsgID=(int)(Math.random()*10000);
        //msgtemp+=String.format("%04d", MsgID);
        msgtemp+=" q ";
        msgtemp+=acnt03.getText();
        //msgtemp+=" ";
        //msgtemp+=amount01.getText();
        byte[] msg=msgtemp.getBytes();
        try{
            DatagramSocket socket =new DatagramSocket();
            InetAddress address=InetAddress.getByName(IPTxt.getText());
            int port =Integer.parseInt(PortText02.getText());
            DatagramPacket packet=new DatagramPacket(msg,msg.length,address, port);
            socket.send(packet);

            //packet=new DatagramPacket(msg, msg.length);
            //socket.receive(packet);

            String received =new String(packet.getData(),0,packet.getLength());

            String[] respns=received.split(" ");
            if (respns.length!=4)
                JOptionPane.showMessageDialog(null, "Invalid Response Message!","Oops",JOptionPane.ERROR_MESSAGE);
            else
            {
                if ((respns[0].equals("s")) && (respns[1].equals("q")) &&(respns[2].equals(acnt03.getText())))
                   JOptionPane.showMessageDialog(null, "Your account has a balance of "+respns[3],
                           "Result",JOptionPane.INFORMATION_MESSAGE);
                else
                    JOptionPane.showMessageDialog(null, "Invalid Response Message!","Oops",JOptionPane.ERROR_MESSAGE);
            }
            socket.close();
            //JOptionPane.showMessageDialog(null, received,"Deliverd",JOptionPane.INFORMATION_MESSAGE);
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Can't connect to server!","Oops",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_QueryBtnActionPerformed

    private void btnFailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFailActionPerformed
        // TODO add your handling code here:

        String msgtemp="c";
        msgtemp+=" f ";

        byte[] msg=msgtemp.getBytes();
        try{
            DatagramSocket socket =new DatagramSocket();
            InetAddress address=InetAddress.getByName(IPTxt.getText());
            int port =Integer.parseInt(PortText02.getText());
            DatagramPacket packet=new DatagramPacket(msg,msg.length,address, port);
            socket.send(packet);

            //packet=new DatagramPacket(msg, msg.length);
            //socket.receive(packet);

            String received =new String(packet.getData(),0,packet.getLength());

            socket.close();
            //JOptionPane.showMessageDialog(null, received,"Deliverd",JOptionPane.INFORMATION_MESSAGE);

            try{
                Thread.sleep(4000);
            }
            catch(Exception e){
            }
            //sleep some time for the "unstable" period
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "Can't connect to server!","Oops",JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnFailActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
	GUI_Id = args[0];
	IPadd = args[1];
	port02 = args[2];
	guiListenPort = Integer.parseInt(args[3]);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ATMGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel AvLbl;
    private javax.swing.JButton DepositBtn;
    private javax.swing.JLabel IPLbl;
    private javax.swing.JLabel IPLbl02;
    private javax.swing.JTextField IPTxt;
    private javax.swing.JLabel PortLbl01;
    private javax.swing.JTextField PortText01;
    private javax.swing.JTextField PortText02;
    private javax.swing.JButton QueryBtn;
    private javax.swing.JButton TransferBtn;
    private javax.swing.JLabel WelcomeLbl;
    private javax.swing.JButton WithdrawBtn;
    private javax.swing.JTextField acnt01;
    private javax.swing.JTextField acnt02;
    private javax.swing.JTextField acnt03;
    private javax.swing.JTextField acnt04;
    private javax.swing.JTextField acnt05;
    private javax.swing.JLabel acntlbl01;
    private javax.swing.JLabel acntlbl02;
    private javax.swing.JLabel acntlbl03;
    private javax.swing.JLabel acntlbl04;
    private javax.swing.JLabel acntlbl05;
    private javax.swing.JTextField amount01;
    private javax.swing.JTextField amount02;
    private javax.swing.JTextField amount04;
    private javax.swing.JLabel amtlbl01;
    private javax.swing.JLabel amtlbl02;
    private javax.swing.JLabel amtlbl04;
    private javax.swing.JButton btnFail;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables


    //receiving: s c 4444 127.0.0.1
    //ip address is not required
    public void ProcessPacket(String packet){
        packet = packet.trim();
        System.out.println(packet);
        String[] items = packet.split(" ");
        if (items.length < 3) return;
        if (!items[0].equals("s")) return;
        if (items[1].equals("c")){
            try{
                int cport = Integer.parseInt(items[2]);
                PortText02.setText(items[2]);
            }
            catch(Exception e){
                return;
            }
            if (items.length>=4)
            {
                try{
                    InetAddress iaddress=InetAddress.getByName(items[3]);
                    IPTxt.setText(items[3]);
                }
                catch(Exception e){
                }
            }
        }
    }

    class GUIServer implements Runnable{
        protected boolean running=true;
        protected DatagramSocket socket = null;
        public int port;

        public GUIServer(int port)
        {
            this.port=port;
        }

        //public void StopThread()
        //{
        //    running=false;
        //}

        public void run(){
            try{
                socket=new DatagramSocket(guiListenPort);
            }catch(Exception e) {
                running=false;	}
            System.out.println("GUI server thread starts at port: "+String.format("%d", port));
            while(running){
                try{
                    int iport=Integer.parseInt(PortText02.getText());
                    if (port!=iport)
                    {
                        port=iport;
                        System.out.println("GUI server thread starts at port: "+String.format("%d", port));
                        socket=new DatagramSocket(port);
                    }
                }
                catch(Exception e) {
		    System.out.println(e);
                    running=false;
                    continue;
                }
                //System.out.println("check."+String.format("%d", port));
                try{
                    byte[] buf = new byte[256];
                    // byte[] inbuf = new byte[256];
                    // receive request
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    socket.setSoTimeout(1000);
                    socket.receive(packet);
                    System.out.println("received something.");

                    String input = new String(buf);

                    ProcessPacket(input);
                    //snapans.setText(ProcessSnap(input));
                }
                catch(IOException e){
                    //running = false;
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
            System.out.println("GUI server thread closed.");
            socket.close();
        }
    }
}
