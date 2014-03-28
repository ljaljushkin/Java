package client;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;

import javax.swing.JLabel;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConnectionThread extends Thread{
	
	private ConnectionState m_connectionState;
	
	private String m_name;
	
	private JScrollPane m_gamesScrollPane;
	private JScrollPane m_clientsScrollPane;
	
	private boolean con=true;
	
	public ConnectionThread(ConnectionState cs){
		this.m_connectionState=cs;
	}
	
	public void sendName(String Name){
		try{
			
			
			ObjectOutputStream oos= new ObjectOutputStream(m_connectionState.m_clientSocket.getOutputStream());
			oos.writeInt(5);
			oos.writeObject(Name);
			oos.flush();
			
			//cs.frame.setTitle(cs.frame.getTitle()+" - "+Name);
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void remakeFrame(){
		
		GridBagConstraints c = new GridBagConstraints();
		
		//cs.frame.remove(cs.but);
		
		con=true;
		
		m_connectionState.m_frame.setSize(270,320);
		
		ActionListener[] al=m_connectionState.m_button.getActionListeners();
		for (int i=0; i<al.length; i++){
			m_connectionState.m_button.removeActionListener(al[i]);
		}
		
		//cs.lab.setText("Choose game or start new game:");
		
		m_connectionState.m_button.setText("Choose!");
		
		m_connectionState.m_button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (con&&!(m_connectionState.m_textField.getText().equals(m_name))){
					try{
						ObjectOutputStream oos= new ObjectOutputStream(m_connectionState.m_clientSocket.getOutputStream());
						oos.writeInt(4);
						oos.writeObject(m_connectionState.m_textField.getText());
						oos.flush();
					}catch(Exception e1){
						e1.printStackTrace();
					}
				}
			}
			
		});
		
		m_connectionState.m_textField.setText("");
		
		
		c.anchor=GridBagConstraints.PAGE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady=20;
		c.ipadx=65;
		c.gridx = 0;
		c.gridy = 3;
		c.gridheight=1;
		c.gridwidth=1;
		m_connectionState.m_frame.add(new JLabel("Games:"),c);
		
		
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady=20;
		c.ipadx=50;
		c.gridx = 1;
		c.gridy = 3;
		c.gridheight=1;
		c.gridwidth=1;
		m_connectionState.m_frame.add(new JLabel("Clients:"),c);
		
		c.ipady=0;
		c.ipadx=0;
		
		c.anchor = GridBagConstraints.PAGE_END;
		m_connectionState.m_clientsTextArea = new JTextArea();
		m_connectionState.m_clientsTextArea.setText(" ");
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 4;
		c.gridheight=1;
		c.gridwidth=1;
		c.ipadx=100;
		c.ipady=100;
		c.fill = GridBagConstraints.NONE;
		m_clientsScrollPane = new JScrollPane(m_connectionState.m_clientsTextArea );
		m_clientsScrollPane.setSize(100,100);
		m_connectionState.m_frame.add(m_clientsScrollPane,c);
		
		
		m_connectionState.m_gamesTextArea=new JTextArea();
		
		
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy = 4;
		c.gridheight=1;
		c.gridwidth=1;
		c.ipadx=100;
		c.ipady=100;
		
		m_gamesScrollPane = new JScrollPane(m_connectionState.m_gamesTextArea);
		m_gamesScrollPane.setSize(100,100);
		
		m_connectionState.m_frame.add(m_gamesScrollPane,c);
		
		
		
		m_connectionState.m_gamesTextArea.setEditable(false);
		m_connectionState.m_clientsTextArea.setEditable(false);
		
		c.ipady=0;
		c.ipadx=0;
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 5;
		c.gridheight=1;
		c.gridwidth=2;
		
		
		
		JButton but=new JButton("Create game");
		but.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (con){	
					try{
						ObjectOutputStream oos= new ObjectOutputStream(m_connectionState.m_clientSocket.getOutputStream());
						oos.writeInt(3);
						oos.flush();
						
					}catch(Exception e1){
						e1.printStackTrace();
					}
				}
					
			}
			
		});
		
		m_connectionState.m_frame.add(but,c);
		m_connectionState.m_label.setText("Choose game or start new game:");
		
	}
	
	public void InitGame(final int nTeam){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();//(nTeam, m_connectionState.m_clientSocket, m_connectionState.m_frame.getTitle());
					//window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		this.m_connectionState.m_frame.setVisible(false);
	}
	
	public void disableAll(){
		for (int i=0; i<m_connectionState.m_frame.getComponentCount(); i++){
			m_connectionState.m_frame.getComponent(i).setEnabled(false);
		}
	}
	
	public void run(){
		boolean runned=false;
		
		try {
			m_connectionState.m_clientSocket = new Socket(m_connectionState.m_textField.getText(), ConnectionState.s_port);
			
			
			m_connectionState.m_label.setText("Connected! Enter your Nick: ");
			m_connectionState.m_textField.setText("");
			
			//cs.frame.remove(cs.but);
			
			ActionListener[] al=m_connectionState.m_button.getActionListeners();
			for (int i=0; i<al.length; i++){
				m_connectionState.m_button.removeActionListener(al[i]);
			}
			m_connectionState.m_button.setText("Ok");
			m_connectionState.m_button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					if (m_connectionState.m_textField.getText().length()>0){
						sendName(m_connectionState.m_textField.getText());
					}
					
				}
				
			});
			
			
			/*GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 2;
			c.gridheight=1;
			c.gridwidth=1;
			cs.frame.add(cs.but,c);*/
			
			runned=true;
		} catch (Exception e) {
			//cs.lab.setText("CONNECTION LOST!!!");
			this.interrupt();
			//e.printStackTrace();
		}
		try{
			
			while (runned&&true){
				
				ObjectInputStream ois=new ObjectInputStream(m_connectionState.m_clientSocket.getInputStream());
				int nComand=ois.readInt();
				
				if(nComand==7){
					m_connectionState.m_label.setText("This Nick already used!");
				}
				
				if(nComand==8){
					String str=(String)ois.readObject();
					m_connectionState.m_frame.setTitle(str);
					m_name=str;
					remakeFrame();
				}
				
				if (nComand==6){
					m_connectionState.m_clientsTextArea.setText("");
					m_connectionState.m_gamesTextArea.setText("");
					int freeCount = ois.readInt();
					int busyCount = ois.readInt();
					for (int i=0; i<freeCount; i++){
						String str=(String)ois.readObject();
						m_connectionState.m_clientsTextArea.append(str+"\n");
					}
					for (int i=0; i<busyCount; i++){
						String str=(String)ois.readObject();
						m_connectionState.m_clientsTextArea.append(str+"\n");
					}
					int countGames=ois.readInt();
					for (int i=0; i<countGames; i++){
						String str=(String)ois.readObject();
						m_connectionState.m_gamesTextArea.append(str+"\n");
					}
				}
				
				if (nComand==5){
					int nTeam=ois.readInt();
					InitGame(nTeam);
					break;
				}
			
			}
			
		} catch (Exception e) {
			
			m_connectionState.m_label.setText("CONNECTION LOST!!!");
			disableAll();
			con=false;
			this.interrupt();
			e.printStackTrace();
		}
	}
}
