package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JTextArea;

public class ServerThread extends Thread 
{	
	private JTextArea m_out;
	
	private ServerSocket m_serverSocket;
	
	private HashMap< Communicator, ClientInfo > m_freeClient;
	private HashMap< Communicator, ClientInfo > m_busyClient;
	
	public ServerThread( JTextArea out )
	{
		this.m_out = out;
	}
	
	public void sendListsAll()
	{
		Iterator<Entry < Communicator, ClientInfo >> it = m_freeClient.entrySet().iterator();
		
		while ( it.hasNext() )
		{
			Entry< Communicator, ClientInfo > s = it.next();
			s.getKey().sendLists();
		}
		
		Iterator<Entry < Communicator, ClientInfo >> it2 = m_busyClient.entrySet().iterator();
		
		while ( it2.hasNext() )
		{
			Entry< Communicator, ClientInfo > s = it2.next();
			if ( s.getValue().m_waiting ) 
				s.getKey().sendLists();
		}
	}
	
	public void disable() 
	{
		Iterator< Entry <Communicator, ClientInfo >> it = m_freeClient.entrySet().iterator();
		
		while ( it.hasNext() )
		{
			Entry< Communicator, ClientInfo > s = it.next();
			s.getKey().disconnect();
		}
		
		Iterator< Entry<Communicator, ClientInfo >> it2 = m_busyClient.entrySet().iterator();
		
		while ( it2.hasNext() )
		{
			Entry< Communicator, ClientInfo > s = it2.next();
			s.getKey().disconnect();
		}
		
		m_out.append("Server stopped!\n");
		
		try
		{
			m_serverSocket.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
		
		this.interrupt();
	}
	
	public void run()
	{
		try
		{
			m_freeClient = new HashMap< Communicator, ClientInfo >();
			m_busyClient = new HashMap< Communicator, ClientInfo >();
			
			m_serverSocket = new ServerSocket( 4378 );
			
			m_out.append("Server Started!\n");
			
			//sb.getVerticalScrollBar().setValue(25);
			//sb.getVerticalScrollBar().setValue(sb.getVerticalScrollBar().getMaximum());
			
			while ( true )
			{	
				Socket clientSocket = m_serverSocket.accept();
				
				ClientInfo   cl = new ClientInfo();
				Communicator cm = new Communicator( clientSocket, m_freeClient, m_busyClient, m_out, cl, this);
				
				m_freeClient.put( cm, cl );
				cm.start();
				m_out.append("Client connected!\n");
			}
		}
		catch( IOException e )
		{
			e.printStackTrace();	
		}
	}
}
