/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Analyst;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author vgswetha92
 */

  public class Analyst extends JFrame
  {
        private JTextArea chatWindow;
        private JTextField userText;
        private JFrame scoreBoard;

        ServerSocket serverSocket;
        Socket s ;
        
        //DataOutputStream output;
        String message;
      
        
    public static void main (String args[]) 
    { 
        Analyst client = new Analyst();
        
	client.client();
    
    }
           
   
   public void client()
    {   
//         scoreBoard = new JFrame();
//         scoreBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         //scoreBoard.getContentPane().add(emptyLabel, BorderLayout.CENTER);
//         scoreBoard.pack();
//           scoreBoard.setSize(50,100);
//            scoreBoard.setVisible(true);
            
          userText = new JTextField();
            userText.setEditable(false);
            userText.addActionListener
              (
                    new ActionListener ()
                    {
                      public void actionPerformed(ActionEvent e)
                         {
                           sendMessage(e.getActionCommand());
                           showMessage("Client:"+e.getActionCommand());
                           userText.setText("");
                         }
                     }
               );

            add( userText,BorderLayout.SOUTH);
            chatWindow = new JTextArea();
            add (new JScrollPane (chatWindow), BorderLayout.CENTER);
            setSize(600,400);
            setVisible(true);
           // chatWindow.setMargin(new Insets(10,10,10,10));
            //pack();
            chatWindow.setBorder(BorderFactory.createCompoundBorder(
                chatWindow.getBorder(), 
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            
           
        try 
        {       
                connectToServer();               
                 new listen().start();
                 ableToType(true);
        }
        catch(EOFException eof)
        {
             System.out.println("Client terminated connection");
        }
        catch(IOException ie) 
        {   
            ie.printStackTrace();
        }
                
    
    }
     
 
   
   private void connectToServer() throws IOException
   {
       showMessage("Trying to establish connection");
      
        // Connect to the server process running at host
        //  and port 9000.
       s = new Socket("localhost", 9000);
       showMessage("Connection established\n---GAME INSTRUCTIONS---\n");
       showMessage("1.Guess the word correctly in two minutes- you get one point.\n");
       showMessage("2.Ask Y or N questions starting with '?'.\n");
       showMessage("3.Guess wrongly- you lose 2 points.\n");
       showMessage("4.Puzzles are case sensitive.\n");
       showMessage("Waiting for word from Oracle ");
        
   }
   
    
                
    public class listen extends Thread
     {
        
        public listen() 
        {
    	super("listen");
        }
      
        public void run() 
        {   System.out.println("inside client gmae");
          
           do
            {   
                try
                {        
                    BufferedReader br;
                    br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    if((message = br.readLine())!=null)
                        showMessage(message);
                    System.out.println(message);
                    if(message.compareTo("QUIT")==0)
                    {   System.out.println("quitting ");
                        closeIt();
                       // System.exit(0);
                    }
                } catch (IOException ex) {
              //  Logger.getLogger(Analyst.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }while (!message.equals("QUIT")) ;
        }  
    }
   
   
    private void closeIt()
    {
        System.out.println("\nClosing current connection ");
        
        try
        {
            s.close();
        }catch(IOException ie)
        {
            ie.printStackTrace();
        }
    }
    
    
    private void sendMessage(String message)
    {   System.out.println("\nInside send message");
        try
        {   showMessage(message);
            DataOutputStream output;
            output = new DataOutputStream(s.getOutputStream());
            output.writeBytes(message+"\n");
            output.flush();
            
        }catch(IOException ie)
        {
           ie.printStackTrace();
        }
    }
      	
    private void showMessage(final String text)
    {
         SwingUtilities.invokeLater
           (
                 new Runnable()
                 {
                    public void run()
                    { chatWindow.append("\n\n"+text);
                     chatWindow.setCaretPosition(chatWindow.getDocument().getLength());
                    }
                 }
                    
            );
    }
    
    private void ableToType( final boolean tof)
    {
        SwingUtilities.invokeLater
        (
            new Runnable()
                {
                    public void run()
                    {
                        userText.setEditable(tof);
                    }
                }
                
         );
    }
    
    
    
}