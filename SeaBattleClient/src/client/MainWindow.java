package client;

import java.net.*;
import java.util.Random;
import java.util.StringTokenizer;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

public class MainWindow 
{
	public JFrame    m_frame;
	public GameState gs;
	   
    public MainWindow( Socket clientSocket, boolean isFirst ) 
    {
    	gs = new GameState();
    	gs.flag_xoda = isFirst;
    	
    	createAndShowGUI(); //TODO: run in separate thread   	
    	
    	ClientThread clientThread   = new ClientThread( gs );	
		clientThread.m_clientSocket = clientSocket;
		gs.socket = clientSocket;
		clientThread.start();
        
    }
    
	public void RunClient()
    {        		
    	/* Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом. 
        sin = socket.getInputStream();
        sout = socket.getOutputStream();

        // Конвертируем потоки в другой тип, чтоб легче обрабатывать текстовые сообщения.
        DataInputStream in = new DataInputStream(sin);
        DataOutputStream out = new DataOutputStream(sout);

        String line = "";
        System.out.println("Type in something and press enter. Will send it to the server and tell ya what it thinks.");
        System.out.println();

        out.writeUTF("Клиент подключился"); 
        
        
        line = in.readUTF(); // ждем пока сервер пришлет, что установил соединение
        label_conn.setText(line); */
         
		/*gs.label.setText("Раставьте корабли");
		
        while( gs.flag )
        {
        	if( gs.flag_readyShips )
        	{
        		//out.writeUTF("Клиент разметил корабли!");
        		//out.flush(); // заставляем поток закончить передачу данных.
        		gs.flag_readyShips = false;
        		gs.flag = false;
        		gs.flag_game = true;
        		//gs.flag_xoda = false; // Server is the first
        	}
        }*/
        
        //line = in.readUTF(); // ждем пока сервер пришлет, что разметил корабли
        //label_conn.setText(line);
        gs.XOD.setText("ХОДИТ СЕРВЕР");
        gs.label_i.setText("Ходит Сервер...");
        
        gs.currLen     = 0;
        gs.indexInShip = 0;
        gs.flag        = true;
        
    }
	
	public void createAndShowGUI()
	{
		if (gs.flag_xoda)
			m_frame = new JFrame("First");
		else
			m_frame = new JFrame("Second");
		
		m_frame.setSize(1043,463);
		m_frame.setVisible(true);
		m_frame.setLocation(0, 500);	
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.setResizable(false);
		
		gs.myPanel    = new JPanel();
		gs.enemyPanel = new JPanel();
		
		gs.myPanel.setBorder(BorderFactory.createTitledBorder("ВАШ ФЛОТ"));
		gs.enemyPanel.setBorder(BorderFactory.createTitledBorder("ФЛОТ СЕРВЕРА"));
		GridBagConstraints c  = new GridBagConstraints();
		
		URL url    = MainWindow.class.getResource("images/blue.jpg");
		gs.sea     =  new ImageIcon( url );
		
		url        = MainWindow.class.getResource("images/red.jpg");
		gs.dead    = new ImageIcon( url );
		
		url        = MainWindow.class.getResource("images/green.jpg");
		gs.green   = new ImageIcon( url );
		
		url        = MainWindow.class.getResource("images/orange.jpg");
		gs.injured =  new ImageIcon( url );
		
		url        = MainWindow.class.getResource("images/yellow.jpg");
		gs.marked  =  new ImageIcon( url );
		
		url        = MainWindow.class.getResource("images/mimo.jpg");
		gs.mimo    =  new ImageIcon( url );
			
		gs.DrawSea( c );
		
		gs.decks                 = new JLabel();
		final JPanel panel_palub = new JPanel();
		gs.myPanel.add( panel_palub,BorderLayout.SOUTH );
		panel_palub.setLayout( new GridLayout(0,2) );
		gs.label_i = new JLabel("Выберите корабль");
		
		gs.label_i.setVisible( false );
		panel_palub.setVisible( false );
		
		panel_palub.add( gs.label_i );
		final JButton buttonAdd = new JButton("Добавить");
		buttonAdd.setVisible( false );
		panel_palub.add( buttonAdd );
		JPanel panel_decks = new JPanel();
		panel_palub.add( panel_decks,BorderLayout.SOUTH );
		gs.decks.setVisible( false );
		panel_decks.add( gs.decks );
		
		gs.myPanel.addMouseListener( new MouseListener()
		{				
				@Override
				public void mouseClicked( MouseEvent arg0 )
				{
					// TODO Auto-generated method stub
					if ( gs.flag_newShips )
					{
						if( gs.FindXY(arg0) )
						{								
							if( !gs.myField[gs.X][gs.Y].IsShip && !gs.myField[gs.X][gs.Y].mark && 
								!gs.myField[gs.X][gs.Y].ambit ) 
							{
								//decks.setText("CurrInd= "+currIndex+" CurrL= "+currLen+" IndInShip= "+indexInShip);
								if( gs.indexInShip == gs.currLen )
								{
									//decks.setText("Много! "+"CurrL: "+currLen+" IndInShip= "+indexInShip);
									gs.decks.setText("Много палуб!");
								}
								else
								{
									gs.myField[gs.X][gs.Y].setIcon( gs.green );
									gs.myField[gs.X][gs.Y].indexOfShip = gs.currIndex;
									gs.ships[gs.currIndex].arrOfXY[2*gs.indexInShip] = gs.X;
									gs.ships[gs.currIndex].arrOfXY[2*gs.indexInShip+1] = gs.Y;									
									//decks.setText("CurrInd= "+currIndex+" CurrL= "+currLen+" IndInShip= "+indexInShip);	
									gs.myField[gs.X][gs.Y].IsShip = true;
									gs.indexInShip++;
								}
							}
							else
							if ( !gs.myField[gs.X][gs.Y].mark && !gs.myField[gs.X][gs.Y].ambit )
							{
								gs.myField[gs.X][gs.Y].setIcon( gs.sea );
								gs.myField[gs.X][gs.Y].IsShip = false;
								gs.ships[gs.currIndex].Del( gs.X,gs.Y, gs.indexInShip );
								gs.indexInShip--; 
								//	decks.setText("CurrInd= "+currIndex+" CurrL= "+currLen+" IndInShip= "+indexInShip);			
							}
							else
							if(gs.myField[gs.X][gs.Y].ambit)
								gs.decks.setText("Не может здесь быть");
						}
					}
				}

				@Override
				public void mouseEntered( MouseEvent arg0 ) 
				{
					// TODO Auto-generated method stub	
				}

				@Override
				public void mouseExited( MouseEvent arg0 )
				{
					// TODO Auto-generated method stub	
				}

				@Override
				public void mousePressed( MouseEvent arg0 )
				{
					// TODO Auto-generated method stub	
				}

				@Override
				public void mouseReleased( MouseEvent arg0 )
				{
					// TODO Auto-generated method stub	
				}
			}
		);
		
		gs.enemyPanel.addMouseListener( new MouseListener()
		{				
			@Override
			public void mouseClicked( MouseEvent arg0 ) 
			{
				// TODO Auto-generated method stub
				if ( gs.flag_game && gs.flag_xoda )
				{
					
					gs.flag_choose = false;
					if( gs.FindXY( arg0 ) )
					{	
						if( !gs.enemyField[gs.X][gs.Y].IsFired )
						{
							System.out.println("Выстрел!!!");
							gs.enemyField[gs.X][gs.Y].IsFired = true;
							gs.flag_choose = true;
						}
						else
							gs.label_i.setText("Уже стреляли сюда!");
					}
				}
			}
			
			@Override
			public void mouseEntered( MouseEvent e ) 
			{
				// TODO Auto-generated method stub			
			}

			@Override
			public void mouseExited( MouseEvent e )
			{
				// TODO Auto-generated method stub			
			}

			@Override
			public void mousePressed( MouseEvent e )
			{
				// TODO Auto-generated method stub	
			}

			@Override
			public void mouseReleased( MouseEvent e )
			{
				// TODO Auto-generated method stub			
			}
		});
		
		gs.label         = new JLabel("Состояние: ");                              
		gs.label_conn    = new JLabel("Состояние соединения: ");
		gs.closeButton   = new JButton("Разорвать соединение");
		gs.sendButton    = new JButton("Отправить сообщение клиенту");
		gs.connectButton = new JButton("Подключиться к игре");
		gs.textSend      = new JTextField("");
		gs.textIP        = new JTextField("127.0.0.1");
		gs.textPORT      = new JTextField("port");
		gs.shipButton    = new JButton("Разметить корабли");
		
		m_frame.setLayout( new GridLayout( 1,3,10,5 ) );
		JPanel panelButton = new JPanel();
			
		m_frame.add( gs.myPanel, 1, 0 );
		m_frame.add( gs.enemyPanel, 2, 0 );
		m_frame.add( panelButton, 3, 0 );
		
		panelButton.setLayout( new GridLayout( 9, 1, 10, 10 ) );
		panelButton.add( gs.connectButton );
		panelButton.add( gs.label );
		panelButton.add( gs.label_conn );
		panelButton.add( gs.textSend );	
		panelButton.add( gs.sendButton );
		panelButton.add( gs.shipButton, BorderLayout.SOUTH );
		panelButton.add( gs.textIP );
		panelButton.add( gs.textPORT );
		panelButton.add( gs.closeButton );
		
		gs.shipButton.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent arg0 )
			{
				if( !gs.flag_newShips )
				{
					/*gs.label.setText("Началась разметка кораблей");
					gs.label_i.setVisible( true );
					panel_palub.setVisible( true );
					buttonAdd.setVisible( true );
					gs.flag_newShips = true;
					gs.decks.setVisible( true );
					gs.label_i.setText("Разметка 4-палубника");
					gs.indexInShip = 0;
					gs.markShips();	
					gs.shipButton.setEnabled( false );*/
					gs.label.setText("Началась разметка кораблей");
					gs.flag_newShips = true;
					gs.indexInShip = 0;
					
					for(gs.indexInShip = 0; gs.indexInShip < 10; gs.indexInShip++)
					{
						gs.SetCurrLen(gs.indexInShip);
						gs.createShipRandomly();
					}
					gs.shipButton.setEnabled( false );
					
					gs.flag_readyShips = true;
				}
			}
		});
		
		buttonAdd.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent arg0 )
			{	
				if( gs.CheckShip() )
				{
					gs.currIndex++;
					if ( gs.currIndex == 10 )
					{
						gs.flag_newShips = false;
						gs.flag_readyShips = true;
						gs.decks.setText("Разметка завершена!");
						gs.label_i.setText("Ожидание сервера...");
						
						buttonAdd.setVisible( false );
						//shipButton.setEnabled( false );
						
						int x = gs.ships[9].arrOfXY[0];
						int y = gs.ships[9].arrOfXY[1];
						
						gs.myField[x][y].setIcon( gs.marked );
						gs.myField[x][y].mark = true;
					}
					else
					{
						Ship sh = gs.ships[gs.currIndex-1];
																	
						for( int i = 0; i < gs.currLen; i++ )
						{
							int x = sh.arrOfXY[i*2];
							int y = sh.arrOfXY[i*2+1];
						
							gs.myField[x][y].setIcon(gs.marked);
							gs.myField[x][y].mark = true;
							
							gs.MarkingAmbit(x, y);
						}
						
						gs.decks.setText("Корабль добавлен");
						gs.SetCurrLen( gs.currIndex );
						gs.label_i.setText("Разметка" + gs.currLen + " -палубника");
						gs.CreateShip( gs.currLen, gs.currIndex );
						gs.indexInShip = 0;
					}	
				}
			}
		});
		
		gs.closeButton.addActionListener( new ActionListener()
		{
			public void actionPerformed( ActionEvent arg0 )
			{
				try 
				{
					gs.socket.close();
					System.out.println("Соединение завершено");
					gs.label.setText("Соединение завершено");
				} 
			  	catch( NullPointerException x ) 
			  	{
			  		gs.label_conn.setText("NullPointerException");
			  		gs.label.setText("Подключиться не удалось!");
			    }
				catch ( IOException e ) 
				{
					gs.label_conn.setText("IOException");
					gs.label.setText("Cоединение не установлено");
				}
			}				
		});
		return;
   }  
}
