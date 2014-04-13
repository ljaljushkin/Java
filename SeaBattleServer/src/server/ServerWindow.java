package server;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

//import server.ServerWindwow;

public class ServerWindow 
{
	// m_ prefix for class members
	private JFrame m_frame;
	
	private JButton m_startServerButton;
	private JButton m_stopServerButton;
	
	private JTextArea m_serverInfo;
	private boolean m_isStarted = false;
	private ServerThread server;
	
	private JScrollPane m_scrollPane;
	
	public static void main( String[] args )
	{
		EventQueue.invokeLater( new Runnable() 
		{
			public void run() 
			{
				try 
				{
					ServerWindow window = new ServerWindow();
					window.m_frame.setVisible( true );
				} 
				catch ( Exception e )
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public ServerWindow() 
	{
		initialize();
	}
	
	private void initialize()
	{
		m_frame = new JFrame("Sea Battle Server");
		m_frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		m_frame.setSize( 250, 200 );
		m_frame.setLocation( 100, 100 );
		m_frame.setVisible( true );
		m_frame.setResizable( false );
		m_frame.getContentPane().setLayout( new GridBagLayout() );
		
		GridBagConstraints c = new GridBagConstraints();
		
		//start server button
		c.fill       = GridBagConstraints.HORIZONTAL;
		c.gridx      = 0;
		c.gridy      = 0;
		c.gridheight = 1;
		c.gridwidth  = 1;
		m_startServerButton = new JButton("Start Server");
		m_startServerButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed( ActionEvent arg0 )
			{
				if (!m_isStarted) 
				{
					server = new ServerThread( m_serverInfo );
					server.start();
					m_startServerButton.setEnabled(false);
					m_stopServerButton.setEnabled(true);
					m_isStarted = true;
				}

			}
		});
		
		m_frame.add(m_startServerButton,c);
		
		//stop server button
		c.fill       = GridBagConstraints.HORIZONTAL;
		c.gridx      = 1;
		c.gridy      = 0;
		c.gridheight = 1;
		c.gridwidth  = 1;
		
		m_stopServerButton = new JButton("Stop Server");
		m_stopServerButton.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed( ActionEvent e )
			{
				if ( m_isStarted )
				{
					//server.disable();
					//server.interrupt();
					m_startServerButton.setEnabled( true );
					m_stopServerButton.setEnabled( false );
					m_isStarted = false;
				}
			}
		});
		
		m_frame.add( m_stopServerButton, c );		
		
		//ServerInfo text area
		c.fill       = GridBagConstraints.HORIZONTAL;
		c.gridx      = 0;
		c.gridy      = 1;
		c.gridheight = 1;
		c.gridwidth  = 2;
		c.ipady      = 100;
		
		m_serverInfo = new JTextArea();
		m_serverInfo.setEditable( false );
		m_scrollPane = new JScrollPane( m_serverInfo );
		m_scrollPane.setAutoscrolls( true );	
		m_serverInfo.setAutoscrolls( true );
		
		m_serverInfo.addComponentListener( new ComponentListener()
		{
			@Override
			public void componentHidden( ComponentEvent e ) {}

			@Override
			public void componentMoved( ComponentEvent e ) {}

			@Override
			public void componentResized( ComponentEvent e ) 
			{
				JScrollBar b = m_scrollPane.getVerticalScrollBar();
				int max      = b.getMaximum();
				b.setValue( max );
			}

			@Override
			public void componentShown(ComponentEvent e) {}
		});
		
		m_frame.add( m_scrollPane, c );
		//scrollPane.setSize(scrollPane.getWidth(), 200);
	}
}
