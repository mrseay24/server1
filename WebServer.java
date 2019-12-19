/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mrseay
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer extends Thread {

   private UI message_to; // need UI to display Message
   private int portNumber; //need port to run on localhost

   //Constructor, takes portNumber and UI as input
   public WebServer(int portNumber, UI ui) {
       this.message_to = ui;
       this.portNumber = portNumber;
       this.start();
   }

   // Thread implementation, overriding run method to run server in separate thread
  
   public void run() {
       ServerSocket socket = null;
       sendMessage("Starting Server. . .\n");
       try {
           sendMessage("Checking port " + Integer.toString(portNumber) + ". . . . .\n");
           socket = new ServerSocket(portNumber);
       }
       catch (Exception exception) {
           sendMessage("Error:" + exception.getMessage());
           return;
       }
       sendMessage("Started . . .\n");
       while (true) {
           sendMessage("Ready and awaiting for requests. . .\n");
           try {
               Socket cSocket = socket.accept();
               InetAddress address = cSocket.getInetAddress();
               sendMessage(address.getHostName() + " connected to server.\n");
               BufferedReader reader =new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
               DataOutputStream outputStream =new DataOutputStream(cSocket.getOutputStream());
               serviceRequest(reader, outputStream);
           }
           catch (Exception exception) {
               sendMessage("\nError:" + exception.getMessage());
           }

       }
   }

   private void serviceRequest(BufferedReader reader, DataOutputStream outputStream) {
       boolean isGet=false;
       String path = new String();
       try {
           String str1 = reader.readLine();
           String str2 = new String(str1);
           str1.toUpperCase();
           if (str1.startsWith("GET")) {
               isGet=true;
           }else if (str1.startsWith("HEAD")) { //
           }else {
               try {
                   outputStream.writeBytes(constructHeader(501, 0));
                   outputStream.close();
                   return;
               }
               catch (Exception exception) {
                   sendMessage("Error:" + exception.getMessage());
               }
           }
           int first = 0;
           int last = 0;
           for (int a = 0; a < str2.length(); a++) {
               if (str2.charAt(a) == ' ' && first != 0) {
                   last = a;
                   break;
               }
               if (str2.charAt(a) == ' ' && first == 0) {
                   first = a;
               }
           }
           path = str2.substring(first + 2, last);
       }
       catch (Exception exception) {
           sendMessage("Error:" + exception.getMessage());
       }
       sendMessage("\n Requested file:" + new File(path).getAbsolutePath() + "\n");
       FileInputStream inputStream = null;

       try {
           inputStream = new FileInputStream(path);
       }
       catch (Exception exception) {
           try {
               outputStream.writeBytes(constructHeader(404, 0));
               outputStream.close();
           }
           catch (Exception e) {
               sendMessage("Error: " + e.getMessage());
           }
           sendMessage("Error: " + exception.getMessage());
       }


       try {
           int type = 0;

           if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
               type = 1;
           }else if (path.endsWith(".gif")) {
               type = 2;
           }else if (path.endsWith(".zip") || path.endsWith(".exe")|| path.endsWith(".tar")) {
               type = 3;
           }

           outputStream.writeBytes(constructHeader(200, type));

           if (isGet) {
               while (true) {
                   int b = inputStream.read();
                   if (b == -1) {
                       break;
                   }
                   outputStream.write(b);
               }

           }
           outputStream.close();
           inputStream.close();
       }

       catch (Exception exception) {
           sendMessage("Error: " + exception.getMessage());
       }

   }
   private String constructHeader(int return_code, int type) {
       String header = "HTTP/1.0 ";
       switch (return_code) {
       case 200:
           header = header + "200 OK";
           break;
       case 404:
           header = header + "404 Not Found";
           break;
       }

       header = header + "\r\n";
       header = header + "Connection: close\r\n";
       header = header + "Server: SimpleWebServer v0\r\n";

       switch (type) {
       case 0:
           break;
       case 1:
           header = header + "Content-Type: image/jpeg\r\n";
           break;
       case 2:
           header = header + "Content-Type: image/gif\r\n";
       case 3:
           header = header + "Content-Type: application/x-zip-compressed\r\n";
       default:
           header = header + "Content-Type: text/html\r\n";
           break;
       }

       header = header + "\r\n";
       return header;
   }
   private void sendMessage(String message) {
       message_to.displayMessage(message);
   }
}