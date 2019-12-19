/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mrseay
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/* this class contains A simple Swing UI.
* which has one start button and one txt console
*/
public class UI extends JFrame {

   static Integer portNumber = null;
   JPanel mainPanel;
   JScrollPane scrollPane;
   JButton btnStartServer;
   JTextArea textArea_1;
   public UI() {
       mainPanel = new JPanel();
       scrollPane = new JScrollPane();
       btnStartServer = new JButton("Start Server");
       btnStartServer.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent arg0) {
               startServer();
           }
       });
       getContentPane().add(btnStartServer, BorderLayout.SOUTH);
      
       textArea_1 = new JTextArea();
       textArea_1.setBackground(Color.BLACK);
       textArea_1.setForeground(new Color(255, 255, 255));
       textArea_1.setRows(30);
       textArea_1.setColumns(30);
       getContentPane().add(textArea_1, BorderLayout.CENTER);
       textArea_1.setBorder(BorderFactory.createLoweredBevelBorder());
       textArea_1.setEditable(false);
      
       this.setTitle("Simple Web Server");
       this.addWindowListener(new java.awt.event.WindowAdapter() {
           public void windowClosing(WindowEvent e) {
               close(e);
           }
       });
       scrollPane.setViewportView(textArea_1);
       mainPanel.add(scrollPane);
       this.getContentPane().add(mainPanel, BorderLayout.EAST);
       this.setVisible(true);
       this.setSize(350,300);
       this.setResizable(false);
       this.validate();
      
   }
   private void startServer() {
       new WebServer(portNumber.intValue(), this);
   }
   void close(WindowEvent event) {
       System.exit(1);
   }
   public void displayMessage(String s) {
       textArea_1.append(s);
   }
   public static void main(String[] args) {
       portNumber = new Integer(8080);
       UI ui = new UI();
   }
}