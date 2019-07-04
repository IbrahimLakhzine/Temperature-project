import java.util.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.fazecast.jSerialComm.SerialPort;
import com.sun.mail.smtp.SMTPSaslAuthenticator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.Scanner;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
public class Graphe_t {
	
	static SerialPort chosenPort;
	static 	int x=0;
	static 	int i=0;
	static 	int j=0;
	public static  void main(String[] args) {
		//inteface graphique
		JFrame win=new JFrame();
		JPanel pan=new JPanel();
		Dimension dim1=new Dimension();
		Dimension dim2=new Dimension();
		dim1.height=20;
		dim1.width=50;
		dim2.height=20;
		dim2.width=200;
		JOptionPane mesg=new JOptionPane(); 
		JLabel label=new JLabel();
		JLabel label1=new JLabel();
		label.setText("temperature max:");
		label.setForeground(Color.GREEN);
		label1.setText("e-mail:");
		label1.setForeground(Color.YELLOW);
		BorderLayout layout=new BorderLayout();
		JButton button=new JButton("connect");
		JTextField text=new JTextField();
		JTextField email=new JTextField();
		text.setPreferredSize(dim1);
		email.setPreferredSize(dim2);
		JComboBox<String> jcom =new JComboBox<String>();
		pan.add(label);
		pan.add(text);
		pan.add(label1);
		pan.add(email);
		pan.add(button);
		pan.add(jcom);
		win.setTitle("temperature graphe");
		win.setSize(800, 400);
		win.setLocation(300,100);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setLayout(layout);
		pan.setBackground(Color.BLACK);
		win.add(pan,BorderLayout.NORTH);
		//serial port 
		SerialPort[] portNames=SerialPort.getCommPorts();
		for(int i=0;i<portNames.length;i++) {
			jcom.addItem(portNames[i].getSystemPortName());
		}
		
		
		
		//graphe
		XYSeries series=new XYSeries("temperature sensor reading");
		XYSeriesCollection seriesCollection=new XYSeriesCollection(series);
		JFreeChart chart=ChartFactory.createXYLineChart("temperature sensor", "time (s)", "temperature(c)",seriesCollection);
		win.add(new ChartPanel(chart),BorderLayout.CENTER);
		
		//button action
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					if(button.getText().equals("connect")) {
						chosenPort= SerialPort.getCommPort(jcom.getSelectedItem().toString());
						chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
						if(chosenPort.openPort()) {
							button.setText("disconnect");
							jcom.setEnabled(false);
						}
						//create a thread read the data from arduino and fill the graphe
						
						Thread th=new Thread() {
							
							public void run() {
								Scanner sc=new Scanner(chosenPort.getInputStream()/*System.in*/);
								while (sc.hasNextLine()) {
									try {
										String line=sc.nextLine();
										float data=Float.parseFloat(line);
										series.add(x++,data);
										win.repaint();
										if(data>Float.parseFloat(text.getText())&&j==0) {
											j++;
										}
										if(data>Float.parseFloat(text.getText())&& j!=0) {
											mesg.showMessageDialog(win,"attention la temperature actuelle a depassé la temperature maximale","warning",JOptionPane.WARNING_MESSAGE);
											
											
											//sending email
												Properties props = new Properties();    
												props.put("mail.smtp.host", "smtp.gmail.com");    
												props.put("mail.smtp.socketFactory.port", "465");    
												props.put("mail.smtp.socketFactory.class",    
									                  "javax.net.ssl.SSLSocketFactory");    
												props.put("mail.smtp.auth", "true");    
												props.put("mail.smtp.port", "465");    
												//get Session   
												Session session = Session.getDefaultInstance(props,    
														new javax.mail.Authenticator() {    
													protected PasswordAuthentication getPasswordAuthentication() {    
														return new PasswordAuthentication("ibra.lakhzine2015@gmail.com","montrealcanada10");  
													}    
												});    
												//compose message    
												try {
													MimeMessage message = new MimeMessage(session);    
													message.addRecipient(Message.RecipientType.TO,new InternetAddress(/*"i.lakhzine2015@gmail.com"*/""+email.getText().toString()+""));    
													message.setSubject("température");    
													message.setText("la température maximale à été dépasser");    
													//send message  
													Transport.send(message);    
													System.out.println("message sent successfully");    
												} catch (MessagingException e) {throw new RuntimeException(e);}    
												
											  
											  
											
											
											  
											   
										 }  
  																			  
										
									
									}catch(Exception e) {
										e.printStackTrace();
									}
									
								}
								sc.close();
							}
						};
						th.start();
						
					}
					else {
						//for disconnecting
						chosenPort.closePort();
						jcom.setEnabled(true);
						button.setText("connect");
					}
					
					
				
				
			}
			
		});
		win.setVisible(true);

	}

}

