package client;


import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JTextField;

//import server.ServerWindwow;

public class ConnectionWindow 
{
	private ConnectionState m_connectionState;
	private JFrame m_connectionWindowFrame;
	
	public static void main( String[] args )
	{
		EventQueue.invokeLater( new Runnable() 
		{
			public void run() 
			{
				try 
				{
					ConnectionWindow window = new ConnectionWindow();
					window.m_connectionWindowFrame.setVisible( true );
				} 
				catch ( Exception e )
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public ConnectionWindow() 
	{
		initialize();
	}
	
	private void initialize()
	{
		m_connectionState = new ConnectionState();
		
		m_connectionWindowFrame = new JFrame("Sea Battle Client");
		m_connectionWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_connectionWindowFrame.setSize(190, 110);
		m_connectionWindowFrame.setLocation(100, 100);
		m_connectionWindowFrame.setVisible(true);
		
		m_connectionWindowFrame.setResizable(false);
		
		m_connectionWindowFrame.getContentPane().setLayout(new GridBagLayout());
		
		m_connectionState.m_frame = m_connectionWindowFrame;
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		//Connect to Server Button
		gbc.fill       = GridBagConstraints.HORIZONTAL;
		gbc.gridx      = 0;
		gbc.gridy      = 2;
		gbc.gridheight = 1;
		gbc.gridwidth  = 2;
		
		m_connectionState.m_button=new JButton ("Connect to server");
		
		m_connectionState.m_button.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				m_connectionState.m_connectionThread =new ConnectionThread( m_connectionState );
				m_connectionState.m_connectionThread.start();
			}
		});
		
		m_connectionWindowFrame.add( m_connectionState.m_button, gbc );
		
		//dest ip Label
		gbc.fill       = GridBagConstraints.NONE;
		gbc.gridx      = 0;
		gbc.gridy      = 0;
		gbc.gridheight = 1;
		gbc.gridwidth  = 2;
		m_connectionState.m_label = new JLabel("Destination IP: ");
		m_connectionWindowFrame.add( m_connectionState.m_label, gbc );
		
		//ip textfield
		gbc.fill       = GridBagConstraints.HORIZONTAL;
		gbc.gridx      = 0;
		gbc.gridy      = 1;
		gbc.gridheight = 1;
		gbc.gridwidth  = 2;
		m_connectionState.m_textField = new JTextField();
		m_connectionState.m_textField.setText("127.0.0.1");
		m_connectionWindowFrame.add( m_connectionState.m_textField, gbc );
	}
}
