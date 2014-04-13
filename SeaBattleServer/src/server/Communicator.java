package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JTextArea;

public class Communicator extends Thread
{	
	private Socket m_client;
	
	private HashMap< Communicator,ClientInfo > m_freeClient;
	private HashMap< Communicator,ClientInfo > m_busyClient;
	
	private JTextArea m_clientTextArea;
	
	private ClientInfo m_clientInfo;
	
	private ServerThread m_serverThread;
	
	private int m_count = 0;
	
	public Communicator( Socket client, HashMap< Communicator,ClientInfo > freeClient, 
			HashMap< Communicator,ClientInfo > busyClient,JTextArea out, 
			ClientInfo cl, ServerThread st)
	{
		this.m_freeClient     = freeClient;
		this.m_busyClient     = busyClient;
		this.m_client         = client;
		this.m_clientTextArea = out;
		this.m_clientInfo     = cl;
		this.m_serverThread   = st;
	}
	
	public void sendLists()
	{
		if ( !m_clientInfo.m_played && m_clientInfo.m_name != null )
		{	
			try
			{	
				ObjectOutputStream oos = new ObjectOutputStream( m_client.getOutputStream() );
				oos.writeInt( 6 );
				oos.writeInt( m_freeClient.size() );
				oos.writeInt( m_busyClient.size() );
				
				Iterator<Entry < Communicator, ClientInfo >> it = m_freeClient.entrySet().iterator();
				
				while ( it.hasNext() )
				{
					Entry< Communicator, ClientInfo > s = it.next();
					String str = s.getValue().m_name;
					oos.writeObject( str );
				}
				
				Iterator<Entry < Communicator, ClientInfo >> it2 = m_busyClient.entrySet().iterator();
				int countGames = 0;
				
				while ( it2.hasNext() )
				{
					Entry< Communicator, ClientInfo > s = it2.next();
					String str = s.getValue().m_name;
					oos.writeObject( str );
					if ( s.getValue().m_waiting )
						countGames++;
				}
				
				oos.writeInt( countGames );
				
				Iterator<Entry < Communicator, ClientInfo >> it3 = m_busyClient.entrySet().iterator();
				
				while ( it3.hasNext() )
				{
					Entry< Communicator, ClientInfo > s = it3.next();
					
					if ( s.getValue().m_waiting )
						oos.writeObject( s.getValue().m_name );
				}
				
				oos.flush();
			} 
			catch ( IOException e )
			{	
				e.printStackTrace();
			}
		}
	}
	
	public void disconnect()
	{
		if ( m_count < 1 )
		{
			try
			{
				m_clientTextArea.append("Client disconnected!\n");
				m_freeClient.remove(this);
				m_busyClient.remove(this);
				m_serverThread.sendListsAll();
				m_client.close();
				m_count++;
				this.interrupt();
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}	
		}
	}
	
	public void initGame( boolean isFirst )
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream( m_client.getOutputStream() );
			oos.writeInt( 5 );
			oos.writeBoolean( isFirst );
			oos.flush();
		}
		catch( Exception e )
		{
			if ( m_clientInfo.m_partner != null )
				m_clientInfo.m_partner.disconnect();
			
			disconnect();
		}
	}
	
	
	public void sendMess( String mess )
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream( m_client.getOutputStream() );
			oos.writeInt( 0 );
			oos.writeObject( mess );
			oos.flush();
		}
		catch( Exception e )
		{
			if ( m_clientInfo.m_partner != null )
				m_clientInfo.m_partner.disconnect();
			
			disconnect();
		}
	}
	
	public void sendTurn( int x, int y, int nx, int ny )
	{
		try
		{
			ObjectOutputStream oos = new ObjectOutputStream( m_client.getOutputStream() );
			oos.writeInt( 1 );
			oos.writeInt( x );
			oos.writeInt( y );
			oos.writeInt( nx );
			oos.writeInt( ny );
			oos.flush();
		}
		catch( Exception e )
		{
			if ( m_clientInfo.m_partner != null )
				m_clientInfo.m_partner.disconnect();
			
			disconnect();
		}
	}
	
	public void run()
	{
		try
		{
			while ( true )
			{
				ObjectInputStream ois = new ObjectInputStream( m_client.getInputStream() );
				int nComand=ois.readInt();
				
				if ( nComand == 0 ) 
				{
					String str = (String)ois.readObject();
					m_clientInfo.m_partner.sendMess( str );
				}
				
				if ( nComand == 1 )
				{
					int x  = ois.readInt();
					int y  = ois.readInt();
					int nx = ois.readInt();
					int ny = ois.readInt();
					m_clientInfo.m_partner.sendTurn(x, y, nx, ny);
				}
				
				if ( nComand == 4 )
				{
					String str = (String)ois.readObject();
					Iterator< Entry < Communicator, ClientInfo >> it2 = m_busyClient.entrySet().iterator();
					
					while ( it2.hasNext() )
					{
						Entry< Communicator, ClientInfo > s = it2.next();
						
						if ( str.equals( s.getValue().m_name ) && s.getValue().m_waiting )
						{	
							s.getValue().m_played = true;
							m_busyClient.put( this, m_clientInfo );	
							m_freeClient.remove( this );
							m_clientInfo.m_played = true;
							m_clientInfo.m_partner = s.getKey();
							s.getValue().m_partner = this;
							s.getValue().m_waiting = false;
							s.getValue().m_played = true;
							m_clientInfo.m_waiting = false;
							m_clientInfo.m_played = true;
							
							//add to communicator client info
							//and add to connection thread initializing game 
							m_clientTextArea.append( m_clientInfo.m_name+" and "+s.getValue().m_name+" started game!\n" );
							m_serverThread.sendListsAll();
							
							this.initGame( false );
							s.getKey().initGame( true );
							break;
						}
					}
				}
				
				if ( nComand == 5 )
				{	
					String str = (String)ois.readObject();
					boolean notBusy = true;
					Iterator<Entry < Communicator, ClientInfo >> it = m_freeClient.entrySet().iterator();
					
					while ( it.hasNext() )
					{
						Entry< Communicator, ClientInfo > s = it.next();
						
						if( str.equals( s.getValue().m_name ) )
							notBusy = false;
					}
					
					Iterator<Entry < Communicator, ClientInfo >> it2 = m_busyClient.entrySet().iterator();
					
					while ( it2.hasNext() )
					{
						Entry< Communicator, ClientInfo > s = it2.next();
						
						if( str.equals( s.getValue().m_name ) )
							notBusy=false;
					}
					
					if ( notBusy )
					{
						m_freeClient.get(this).m_name = str;
						//sendLists();
						m_clientTextArea.append("Client choose nick: "+str+"\n");
						ObjectOutputStream oos = new ObjectOutputStream( m_client.getOutputStream());
						oos.writeInt( 8 );
						oos.writeObject( str );
						oos.flush();
						m_serverThread.sendListsAll();
					}
					else
					{
						ObjectOutputStream oos = new ObjectOutputStream(m_client.getOutputStream());
						oos.writeInt( 7 );
						oos.flush();
					}
				}
				
				if ( nComand == 3 )
				{
					m_clientInfo.m_waiting = true;
					m_busyClient.put( this, m_clientInfo );
					m_freeClient.remove( this );
					m_clientTextArea.append( m_clientInfo.m_name+" created game!\n" );
					m_serverThread.sendListsAll();
				}
			}
		}
		catch( Exception e )
		{
			if ( m_clientInfo.m_partner != null )
				m_clientInfo.m_partner.disconnect();
			
			disconnect();
		}
	}
}
