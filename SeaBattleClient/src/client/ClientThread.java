package client;

import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

public class ClientThread extends Thread
{
	public Socket    m_clientSocket;
	
	public GameState gs;
	
	public ClientThread( GameState gameState )
	{
		gs             = gameState;
		m_clientSocket = null;
	}
	
	/*protected ImageIcon createImageIcon(String path,String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
		System.out.println("Couldn't find file: " + path);
			return null;
		}
	}*/
/*	
	public void refreshGameField(){
		
		ImageIcon iconCblack = createImageIcon("images/c_black.PNG","");
		ImageIcon iconCwhite = createImageIcon("images/c_white.PNG","");
		ImageIcon iconBblack = createImageIcon("images/b_black.PNG","");
		ImageIcon iconBwhite = createImageIcon("images/b_white.PNG","");
		ImageIcon iconBblacksel = createImageIcon("images/b_black_sel.PNG","");
		ImageIcon iconBwhitesel = createImageIcon("images/b_white_sel.PNG","");
		ImageIcon iconDwhite = createImageIcon("images/d_white.PNG","");
		ImageIcon iconDblack = createImageIcon("images/d_black.PNG","");
		ImageIcon iconDwhitesel = createImageIcon("images/d_white_sel.PNG","");
		ImageIcon iconDblacksel = createImageIcon("images/d_black_sel.PNG","");
		
		int n=gs.gameField.getComponentCount();
		
		//out.append(n+" components ");
		for (int i=0; i<n; i++){
			ChessLabel l=(ChessLabel)gs.gameField.getComponent(i);
			int x=l.x;
			int y=l.y;
			//out.append(l.x+", "+l.y+"\n");		
			if ((x+y)%2==1 && gs.gameState[x*8+y]==0){
				l.setIcon(iconCblack);
			}else if(gs.gameState[x*8+y]==1){
				l.setIcon(iconBblack);
			}else if(gs.gameState[x*8+y]==2){
				l.setIcon(iconBwhite);
			}else if(gs.gameState[x*8+y]==3){
				l.setIcon(iconBblacksel);
			}else if(gs.gameState[x*8+y]==4){
				l.setIcon(iconBwhitesel);
			}else if (gs.gameState[x*8+y]==5){
				l.setIcon(iconDblack);
			}else if (gs.gameState[x*8+y]==6){
				l.setIcon(iconDwhite);
			}else if (gs.gameState[x*8+y]==7){
				l.setIcon(iconDblacksel);
			}else if (gs.gameState[x*8+y]==8){
				l.setIcon(iconDwhitesel);
			}else{
				l.setIcon(iconCwhite);
			}
			//gs.out.append(x+","+y+" state "+gs.gameState[x*8+y]+"\n");
		}
	}*/	
	
	public void run()
	{
		try
		{			
			gs.label.setText("Раставьте корабли");
			
	        while( gs.flag )
	        {
	        	if( gs.flag_readyShips )
	        	{
	        		gs.flag_readyShips = false;
	        		gs.flag = false;
	        		gs.flag_game = true;
	        	}
	        }
	        
	        ObjectOutputStream oos = new ObjectOutputStream( gs.socket.getOutputStream());
	        oos.writeInt(100);
	        oos.flush();
	        System.out.println("Соточка ушла");
	        ObjectInputStream ois = new ObjectInputStream( gs.socket.getInputStream()); 
	        int command = 0;
	        do
	        {
		        //System.out.println("ois");
		        command = ois.readInt(); // ждем пока коммуникатор пришлет, что разметил корабли
		        if ( command == 101 )
		        {
		        	System.out.println("Соточка один пришла");
		        	
		        	if (gs.flag_xoda) 
		        		gs.XOD.setText("Your turn, FIREEEE");
		        	else 
		        		gs.XOD.setText("Opponent's turn");
		        	
		        	gs.label_i.setText("Клиент разметил корабли. ура");
		        }
		        if ( command == 102 )
		        {
		        	System.out.println("Соточка два пришла");
		        	//ObjectOutputStream oos1 = new ObjectOutputStream( gs.socket.getOutputStream());
			        oos.writeInt(100);
			        oos.flush();
			        System.out.println("Соточка ушла");
		        }
	        } while (command != 101);
	        
	        gs.currLen     = 0;
	        gs.indexInShip = 0;
	        gs.flag        = true;
	        gs.flag_game = true;
	        
	        ObjectOutputStream oos4 = new ObjectOutputStream( gs.socket.getOutputStream());
	        oos4.writeInt(104);
	        oos4.flush();
	        for(int i = 0; i < 10; i++)
	        {
		        oos4.writeObject(gs.ships[i].len);
		        oos4.flush();
		        oos4.writeObject(gs.ships[i].arrOfXY);
		        oos4.flush();
	        }
	        System.out.println("Корабли ушли");
	        
	        ObjectOutputStream oos3 = new ObjectOutputStream( gs.socket.getOutputStream());
	        oos3.writeInt(103);
	        oos3.flush();
	        for (int i = 0; i < 10; i++ )
	        {
	        	for (int j = 0; j < 10; j++ )
	        	{
	        		oos3.writeObject(gs.myField[i][j].IsShip);
	        		oos3.flush();
	        		oos3.writeObject(gs.myField[i][j].IsFired);
	        		oos3.flush();
	        		oos3.writeObject(gs.myField[i][j].ambit);
	        		oos3.flush();
	        		oos3.writeObject(gs.myField[i][j].indexOfShip);
	        		oos3.flush();
	        		
	        	}
	        }
	        //oos3.flush();
	        System.out.println("Карта ушла");
	        
	        gs.DumpFields();
	        
	        while( gs.flag )
	        {
	        	ObjectInputStream ois1 = new ObjectInputStream( gs.socket.getInputStream()); 
	        	if( gs.flag_xoda )// ваш ход
	        	{
	        		//System.out.println(" " + gs.flag_choose + " ");
	        		this.sleep(100);
					if( gs.flag_choose ) // если выбрана клетка
					{
						System.out.println("Выстрел!!!");
						ObjectOutputStream oos5 = new ObjectOutputStream( gs.socket.getOutputStream());
						oos5.writeInt(105);
				        oos5.flush();
						gs.flag_choose  = false;
						int fromServer = 0;
						oos5.writeUTF(gs.X + "," + gs.Y);
						oos5.flush();
						System.out.println("Вы выстрелили по: " + gs.X + " " + gs.Y);
						
					 	fromServer = ois1.readInt();
					 	switch( fromServer )
					 	{
							case 106: 
								gs.enemyField[gs.X][gs.Y].setIcon(gs.mimo);
								gs.enemyField[gs.X][gs.Y].IsShip = false;
								gs.flag_xoda = false;
								gs.decks.setText("MISSED!");
								gs.XOD.setText("Opponent's turn");
								break;
							case 107:
								gs.enemyField[gs.X][gs.Y].setIcon(gs.injured);
								gs.enemyField[gs.X][gs.Y].IsShip = true;
								gs.decks.setText("INJURED!");
								gs.XOD.setText("Your turn, FIREEEE");
								gs.flag_xoda = true;
								break;
							case 108:
								String line = ois1.readUTF();
		    			 		StringTokenizer st = new StringTokenizer(line, ",");
		    			 		while( st.hasMoreTokens() )
		    			 		{
		    			 			gs.X = Integer.parseInt(st.nextToken());
		    			 			gs.Y = Integer.parseInt(st.nextToken());
		    			 			gs.enemyField[gs.X][gs.Y].IsShip = true;
		                    		
		                    		for( int j = -1; j < 2; j++ )
									{
										for( int g = -1; g < 2; g++ )
										{
											if( gs.X + j > -1 && gs.X + j < 10 && gs.Y + g > -1 && gs.Y + g < 10 )
											{
												MyLabel la = gs.enemyField[gs.X + j][gs.Y + g];
												
												if( !la.IsShip )
													la.setIcon(gs.mimo);
												
												la.IsFired = true;
											}
										}
									}
		                    		
		                    		gs.enemyField[gs.X][gs.Y].setIcon(gs.dead);
		    			 		
		    			 		}
		    			 		
		    			 		gs.decks.setText("Корабль убит!");
		    			 		gs.XOD.setText("ВАШ ХОД");
		    			 		gs.flag_xoda = true;
								break;
							case 109:
								gs.flag = false;
								gs.XOD.setText("YOU WIN!!!");
							case 110:
								gs.flag = false;
								gs.XOD.setText("YOU LOSE!!!");
							}
					}
	       		}
	        	else
	        	{
	        		command = ois1.readInt(); // ждем пока коммуникатор пришлет информацию 2 клиенту про ход первого
	        		switch( command )
				 	{
						case 106: 
							gs.myField[gs.X][gs.Y].setIcon(gs.mimo);
							gs.myField[gs.X][gs.Y].IsShip = false;
							gs.flag_xoda = true;
							gs.decks.setText("MISSED!");
							gs.XOD.setText("Your turn, FIREEEE");
							break;
						case 107:
							gs.myField[gs.X][gs.Y].setIcon(gs.injured);
							gs.myField[gs.X][gs.Y].IsShip = true;
							gs.decks.setText("INJURED!");
							gs.XOD.setText("Opponent's turn");
							gs.flag_xoda = false;
							break;
						case 108:
							String line = ois1.readUTF();
	    			 		StringTokenizer st = new StringTokenizer(line, ",");
	    			 		while( st.hasMoreTokens() )
	    			 		{
	    			 			gs.X = Integer.parseInt(st.nextToken());
	    			 			gs.Y = Integer.parseInt(st.nextToken());
	    			 			gs.myField[gs.X][gs.Y].IsShip = true;
	                    		
	                    		for( int j = -1; j < 2; j++ )
								{
									for( int g = -1; g < 2; g++ )
									{
										if( gs.X + j > -1 && gs.X + j < 10 && gs.Y + g > -1 && gs.Y + g < 10 )
										{
											MyLabel la = gs.enemyField[gs.X + j][gs.Y + g];
											
											if( !la.IsShip )
												la.setIcon(gs.mimo);
											
											la.IsFired = true;
										}
									}
								}
	                    		
	                    		gs.myField[gs.X][gs.Y].setIcon(gs.dead);
	    			 		
	    			 		}
	    			 		
	    			 		gs.decks.setText("Корабль убит!");
	    			 		gs.XOD.setText("ВАШ ХОД");
	    			 		gs.flag_xoda = true;
							break;
						case 109:
							gs.flag = false;
							gs.XOD.setText("YOU WIN!!!");
						case 110:
							gs.flag = false;
							gs.XOD.setText("YOU LOSE!!!");
						}
	        	}
	        }
		}
		catch( Exception e )
		{
			e.printStackTrace();
		
			//gs.out.setText("lost connection!");
			//cleanGameState();
		}
		finally
		{
			try 
			{	
				m_clientSocket.close();
			}
			catch ( IOException e )
			{
				//e.printStackTrace();
				//gs.out.setText("lost connection!");
				//cleanGameState();
			}
		}
	}
	
	/*public void cleanGameState(){
		for (int i=0; i<64; i++){
			gs.gameState[i]=0;
		}
		gs.wasSel=false;
		gs.wasEat=false;
		
		try{
			client.close();
			//server.close();
		}catch(IOException e){
		}
		
		gs.serverStarted=false;
		gs.clientStarted=false;
		
		//gs.startServerBut.setEnabled(true);
		//gs.connectBut.setEnabled(true);
		refreshGameField();
		
		//gs.frame.setName("Chess");
		
		
		this.interrupt();
	}*/
	
	public void sendMessage( String mes )
	{
		try 
		{
			ObjectOutputStream oos= new ObjectOutputStream( m_clientSocket.getOutputStream() );
			oos.writeInt( 0 );
			oos.writeObject( mes );
			oos.flush();
		} 
		catch ( IOException e )
		{
			//gs.out.setText("lost connection!");
			//cleanGameState();
		}
	}
}
