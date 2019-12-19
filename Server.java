/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
import java.net.*;
import java.io.*;

class Server
{
   public static void main(String args[]) throws IOException
   {
       ServerSocket s1=null;
       try
       {
           s1=new ServerSocket(98);
       }
       catch(IOException e)
       {
           System.err.println("Port 98 could not be found");
           System.exit(1);
       }
       Socket c=null;
       int count=0;
       try
       {
           c=s1.accept();
           System.out.println("Connection from "+c);
       }
       catch(IOException e)
       {
           System.out.println("Accept failed");
           System.exit(1);
       }
       PrintWriter out=new PrintWriter(c.getOutputStream(),true);
       BufferedReader in=new BufferedReader(new InputStreamReader(c.getInputStream()));
       String str;
       BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));
       System.out.println("Ready to type now");
      
       while((str=sin.readLine())!=null && count<=10)
       {
           out.println(str);
           count++;
          
       }
  
   str="connection closed";
       out.println(str);
       out.close();
       c.close();
       s1.close();

   }
}



