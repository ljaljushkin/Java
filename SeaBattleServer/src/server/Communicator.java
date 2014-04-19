package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.StringTokenizer;
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
	
	private int m_indexInShip = 0;
	
	private int m_count = 0;
	
	public Ship[]	   ships;
    public MyLabel[][] enemyField;  
    
    boolean[][] bool_arr = new boolean [10][10];
	
    private int GetCurrLen( int currInd )
	{
    	int currLen = -1;
		switch( currInd )
		{
			case 0:
				currLen = 4;
			break;
			case 1: case 2:
				currLen = 3;
			break;
			case 3: case 4: case 5: 
				currLen = 2;
			break;
			case 6: case 7: case 8: case 9: 
				currLen = 1;
			break;
		}
		return currLen; 
	}
	
    private void CreateShip( int len, int ind )
	{
		ships[ind] = new Ship(len);
	}
    
	public void allocShips()
	{
		for (int currIndex = 0; currIndex < 10; currIndex++) 
		{
			CreateShip( GetCurrLen(currIndex), currIndex );
		}
			
	}
	
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
		
		ships = new Ship[ 10 ];
		enemyField = new MyLabel[ 10 ][ 10 ];
		
		allocShips();
		
		for(int i = 0; i < 10; i++)
		{
			//ships[i] = new Ship();
			enemyField[i] = new MyLabel[10];
			for(int j = 0; j < 10; j++)
			{
				enemyField[i][j] = new MyLabel();
			}
		}
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
			oos.writeInt( 666 );
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
	
	public void sendTurn( int x, int y, int nx, int ny ) //!!
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
				Communicator partner = m_clientInfo.m_partner;
				if (partner != null)
				{
					if ( partner.m_clientInfo.m_isMarking && m_clientInfo.isFirst && m_clientInfo.m_isMarking)
					{
						m_clientTextArea.append("Соточка один ушла \n");
						ObjectOutputStream oos = new ObjectOutputStream( m_client.getOutputStream());
						oos.writeInt( 101 );
						oos.flush();
						ObjectOutputStream oos_partner = new ObjectOutputStream( partner.m_client.getOutputStream());
						m_clientTextArea.append("Соточка один ушла \n");
						oos_partner.writeInt( 101 );
						oos_partner.flush();
						partner.m_clientInfo.m_isMarking = false;
						m_clientInfo.m_isMarking = false;
					}
				}
				
				ObjectInputStream ois = new ObjectInputStream( m_client.getInputStream() );
				int nComand = ois.readInt();
								
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
							m_clientInfo.isFirst = false;
							s.getKey().initGame( true );
							s.getKey().m_clientInfo.isFirst = true;
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
					m_clientTextArea.append("Троечка пришла\n");
					m_clientInfo.m_waiting = true;
					m_busyClient.put( this, m_clientInfo );
					m_freeClient.remove( this );
					m_clientTextArea.append( m_clientInfo.m_name+" created game!\n" );
					m_serverThread.sendListsAll();
				}
				if ( nComand == 100 ) // клиент - разметил
				{
					m_clientTextArea.append("Соточка пришла\n");
					//System.out.println("Соточка пришла");
					m_clientInfo.m_isMarking = true;
					
					if (!m_clientInfo.isFirst && m_clientInfo.m_partner.m_clientInfo.m_isMarking)
					{
						ObjectOutputStream oos = new ObjectOutputStream(m_clientInfo.m_partner.m_client.getOutputStream());
						oos.writeInt( 102 );
						oos.flush();
					}
				}
				
				if ( nComand == 103 ) // прием карты, запись в комм. партнера
				{
					m_clientTextArea.append("Соточка три пришла\n");
					for (int i = 0; i < 10; i++)
					{
						for (int j = 0; j < 10; j++)
						{
							m_clientInfo.m_partner.enemyField[i][j].IsShip = (boolean)ois.readObject();
							m_clientInfo.m_partner.enemyField[i][j].IsFired = (boolean)ois.readObject();
							m_clientInfo.m_partner.enemyField[i][j].ambit = (boolean)ois.readObject();
							m_clientInfo.m_partner.enemyField[i][j].indexOfShip = (int)ois.readObject();
							
						}
					}
					
					//dumping
					for(int j = 0; j < 10; j++ )
					{
						for(int i = 0; i < 10; i++ )
						{
							if (m_clientInfo.m_partner.enemyField[i][j].ambit && m_clientInfo.m_partner.enemyField[i][j].IsShip)		
								m_clientTextArea.append("3 ");
							else
							if (m_clientInfo.m_partner.enemyField[i][j].ambit)		
								m_clientTextArea.append("1 ");
							else
							if (m_clientInfo.m_partner.enemyField[i][j].IsShip)		
								m_clientTextArea.append("2 ");
							else
							if (!m_clientInfo.m_partner.enemyField[i][j].ambit && !m_clientInfo.m_partner.enemyField[i][j].IsShip)		
								m_clientTextArea.append("0 ");
							
						}
						m_clientTextArea.append("\n");
					}
					m_clientTextArea.append("\n");m_clientTextArea.append("\n");
				}
				if ( nComand == 104 ) // прием карты, запись в комм. партнера
				{
					m_clientTextArea.append("Соточка четыре пришла\n");
					for (int i = 0; i < 10; i++)
					{
						m_clientInfo.m_partner.ships[i].len = (int)ois.readObject();
						m_clientInfo.m_partner.ships[i].arrOfXY = (int[])ois.readObject();
						m_clientTextArea.append("len["+i+"] = " + m_clientInfo.m_partner.ships[i].len + "\n" );
						/*for (int j = 0; j < m_clientInfo.m_partner.ships[i].len; j++)
						{
							m_clientTextArea.append("x = " + m_clientInfo.m_partner.ships[i].arrOfXY[2 * j] + " y = " + m_clientInfo.m_partner.ships[i].arrOfXY[2 * j + 1] + "\n" );
						}*/
					}
				}
				if ( nComand == 105 )
				{
					m_clientTextArea.append("Соточка пять пришла\n");
	        		String line = ois.readUTF();
	        		StringTokenizer st = new StringTokenizer(line, ",");
	        		
	        		int X = Integer.parseInt(st.nextToken());
	        		int Y = Integer.parseInt(st.nextToken());
	        		
	        		MyLabel lN = m_clientInfo.m_partner.enemyField[X][Y];
	        		
	        		m_clientTextArea.append("Клиент выстрелил по: " + X + " " + Y + "\n");
	        		
	        		ObjectOutputStream oos = new ObjectOutputStream(m_client.getOutputStream());
	        		
	        		ObjectOutputStream oos1 = new ObjectOutputStream(m_clientInfo.m_partner.m_client.getOutputStream());
	        		if( X > -1 && X < 10 && Y > -1 && Y < 10 )
	        		{
	        			lN.IsFired = true;
	        			if( lN.IsShip )
	    				{
	    					if( CheckDead(X, Y) )
	    					{	
	    						oos.writeInt(108);
	    						oos.flush();
	    						oos1.writeInt(108);
	    						oos1.flush();
	    						int ind = lN.indexOfShip;
	    						int n   = ships[ind].len;
	    						int xx,yy;
	    						String temp = "";
	    						
	    						for( int i = 0; i < n; i++ )
	    						{
	    							xx = ships[ind].arrOfXY[2*i];
	    							yy = ships[ind].arrOfXY[2*i+1];
	    							
	    							temp = temp + xx + "," + yy + ",";
	    						}
	    						
	    						//gs.decks.setText("Ваш корабль убит!");
	    						//gs.XOD.setText("ХОДИТ СЕРВЕР");
	    						oos.writeUTF(temp);
	    						m_indexInShip++;
								
	    						if( m_indexInShip == 10 ) // send if someone win/lose
	    						{
	    							oos.writeInt(109);
		    						oos.flush();
		    						
		    						
		    						oos1.writeInt(110);
		    						oos1.flush();
	        			 		}
	    					}
	    					else
	    					{
	    						oos1.writeInt(107);
	    						oos1.flush();
	    						oos.writeInt(107);
	    						oos.flush();
	    						//gs.decks.setText("Ваш корабль ранен!");
	    						//gs.XOD.setText("ХОДИТ СЕРВЕР");
	    					}
	    				}
	    				else
	    				{
	    					oos1.writeInt(106);
    						oos1.flush();
	    					oos.writeInt(106);
    						oos.flush();
	    					//gs.decks.setText("Сервер промазал!");
	    					//gs.XOD.setText("ВАШ ХОД");
	    				}
	        		}		
				}
				
			}
		}
		catch( Exception e )
		{
			e.printStackTrace();
			if ( m_clientInfo.m_partner != null )
				m_clientInfo.m_partner.disconnect();
			
			disconnect();
		}
	}

	private void FillEnemyField() {
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				enemyField[i][j].IsShip = bool_arr[i][j];
			}
		}
		
	}
	
	public boolean CheckDead(int X, int Y)
	{
		int ind = enemyField[X][Y].indexOfShip;
		int n   = ships[ind].len;
		int xx, yy;
		
		for( int i = 0; i < n; i++ )
		{
			xx = ships[ind].arrOfXY[ 2 * i ];
			yy = ships[ind].arrOfXY[ 2 * i + 1 ];
			if( !enemyField[xx][yy].IsFired )
				return false;
		}
		return true;
	}
}
