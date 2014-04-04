package client;

import java.net.*;
import java.util.StringTokenizer;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

public class MainWindow {
	
	public JFrame m_frame;
	public GameState gs;
	
	
	/*static int x; // ������� - ������0���� 10�10
	static int y;
	static int X; // ��������� - ��������
	static int Y;
	static int XY; // X+10*Y
	static int currIndex=0; //������ �������� ������� ��� ����������(�� ����)
	static int currLen=0;
	static int indexInShip=0;
	static boolean lX = true,lY=true;
	
    /*public static void main(String[] ar) 
    {
    	MyRunnable runnable=new MyRunnable();
 	    SwingUtilities.invokeLater(runnable);
    }*/
    
   /* static Socket 		socket;
    static JButton 		connectButton;
    static JButton 		closeButton;
    static JButton 		sendButton;
    static JLabel  		label;
    static JLabel  		label_conn;
    static JTextField	textIP;
    static JTextField	textPORT;
    static JTextField	textSend;
    static JButton 		shipButton;
    static JLabel 		label_i;
    static JLabel 		decks;
    static JLabel 		XOD;
    
    //static  JPanel myPanel;
    //static  JPanel enemyPanel;
    
    static Ship[]	ships   = new Ship[10];
    static MyLabel[][] enemyField = new MyLabel[10][10];
    static MyLabel[][] myField  = new MyLabel[10][10];
 	
    static boolean flag_newShips=false;
    static boolean flag_game=false;
    static boolean flag_readyShips=false;// ��������� �� ���?
    static boolean flag_xoda=false;
    static boolean flag_choose=false;	
    
    static InputStream 	sin;
    static OutputStream sout;
    
    static Icon marked;  
    static Icon injured;
    static Icon green;
    static Icon sea;
    static Icon dead;
    static Icon mimo;
    
    static boolean flag = true;*/
    
    public MainWindow(Socket clientSocket) 
    {
    	createAndShowGUI(); //TODO: run in separate thread
    	
    	ClientThread clientThread = new ClientThread(gs);	
		clientThread.m_clientSocket = clientSocket;
		clientThread.start();
    }
    
	public void RunClient()
    {        		
    	/* ����� ������� � �������� ������ ������, ������ ����� �������� � �������� ������ ��������. 
        sin = socket.getInputStream();
        sout = socket.getOutputStream();

        // ������������ ������ � ������ ���, ���� ����� ������������ ��������� ���������.
        DataInputStream in = new DataInputStream(sin);
        DataOutputStream out = new DataOutputStream(sout);

        String line = "";
        System.out.println("Type in something and press enter. Will send it to the server and tell ya what it thinks.");
        System.out.println();

        out.writeUTF("������ �����������"); 
        
        
        line = in.readUTF(); // ���� ���� ������ �������, ��� ��������� ����������
        label_conn.setText(line); */
         
		gs.label.setText("��������� �������"); 
        while(gs.flag)
        {
        	if(gs.flag_readyShips)
        	{
        		//out.writeUTF("������ �������� �������!");
        		//out.flush(); // ���������� ����� ��������� �������� ������.
        		gs.flag_readyShips = false;
        		gs.flag = false;
        		gs.flag_game = true;
        		gs.flag_xoda = false; // Server is the first
        		 
        	}
        }
        
        //line = in.readUTF(); // ���� ���� ������ �������, ��� �������� �������
        //label_conn.setText(line);
        gs.XOD.setText("����� ������");
        gs.label_i.setText("����� ������...");
        
        gs.currLen = 0;
        gs.indexInShip = 0;
        gs.flag = true;
        while(gs.flag)
        {
        	if(gs.flag_xoda)// ��� ���
        	{
				if(gs.flag_choose) // ���� ������� ������
				{
					gs.flag_choose = false;
					byte fromServer = -1;
					//out.writeUTF(X+","+Y);
					gs.label_i.setText("�� ���������� ��: " + gs.X + " " + gs.Y);
					
				 	//fromServer = in.readByte();
				 	switch(fromServer)
				 	{
						case 0: 
							gs.enemyField[gs.X][gs.Y].setIcon(gs.mimo);
							gs.enemyField[gs.X][gs.Y].IsShip=false;
							gs.flag_xoda=false;
							gs.decks.setText("����!");
							gs.XOD.setText("����� ������");
							break;
						case 1:
							gs.enemyField[gs.X][gs.Y].setIcon(gs.injured);
							gs.enemyField[gs.X][gs.Y].IsShip=true;
							gs.decks.setText("������!");
							gs.XOD.setText("��� ���");
							gs.flag_xoda=true;
							break;
						case 2:
							//line = in.readUTF();
	    			 		//StringTokenizer st = new StringTokenizer(line, ",");
							StringTokenizer st = new StringTokenizer("", ","); //TODO!!!
	    			 		while(st.hasMoreTokens())
	    			 		{
	    			 			gs.X = Integer.parseInt(st.nextToken());
	    			 			gs.Y = Integer.parseInt(st.nextToken());
	    			 			gs.enemyField[gs.X][gs.Y].IsShip = true;
	                    		
	                    		for(int j = -1; j < 2; j++)
								{
									for(int g = -1; g < 2; g++)
									{
										if( gs.X + j > -1 && gs.X + j < 10 && gs.Y + g > -1 && gs.Y + g < 10 )
										{
											MyLabel la = gs.enemyField[gs.X + j][gs.Y + g];
											if(!la.IsShip)
											la.setIcon(gs.mimo);
											la.IsFired = true;
										}
									}
								}
	                    		gs.enemyField[gs.X][gs.Y].setIcon(gs.dead);
	    			 		}
	    			 		gs.decks.setText("������� ����!");
	    			 		gs.XOD.setText("��� ���");
	    			 		gs.flag_xoda = true;
	    			 		gs.currLen++;
	    			 		if(gs.currLen == 10)
	    			 		{
	    			 			gs.flag = false;
	    			 			gs.XOD.setText("YOU WIN!!!");
	    			 		}
							break;
						 }
				}
       		}
        	else // ������ ��������
        	{
        		//line = in.readUTF();
        		//StringTokenizer st = new StringTokenizer(line, ",");
        		StringTokenizer st = new StringTokenizer("", ","); //TODO!!
        		gs.X = Integer.parseInt(st.nextToken());
        		gs.Y = Integer.parseInt(st.nextToken());
        		MyLabel lN = gs.myField[gs.X][gs.Y];
        		gs.label_i.setText("C����� ��������� ��: " + gs.X + " " + gs.Y);
        		byte toServ = -1;
        		if(gs.X > -1 && gs.X < 10 && gs.Y > -1 && gs.Y < 10)
        		{
        			lN.IsFired = true;
        			if(lN.IsShip)
    				{
    					if(CheckDead())
    					{	
    						toServ = 2;
    						//out.writeByte(toServ);
    						int ind = lN.indexOfShip;
    						int n = gs.ships[ind].len;
    						int xx,yy;
    						String temp = "";
    						for(int i = 0; i < n; i++)
    						{
    							xx = gs.ships[ind].arrOfXY[2*i];
    							yy = gs.ships[ind].arrOfXY[2*i+1];
    							gs.myField[xx][yy].setIcon(gs.dead);
    							temp = temp + xx + "," + yy + ",";
    						}
    						gs.decks.setText("��� ������� ����!");
    						gs.XOD.setText("����� ������");
    						//out.writeUTF(temp);
    						gs.indexInShip++;
							if(gs.indexInShip == 10){
								gs.flag = false;
								gs.XOD.setText("YOU LOSE!!!");
        			 		}
    					}
    					else
    					{
    						lN.setIcon(gs.injured);
    						toServ = 1;
    						//out.writeByte(toServ);
    						gs.decks.setText("��� ������� �����!");
    						gs.XOD.setText("����� ������");
    					}
    				}
    				else{
    					lN.setIcon(gs.mimo);
    					toServ = 0;
    					//out.writeByte(toServ);
    					gs.flag_xoda = true;
    					gs.decks.setText("������ ��������!");
    					gs.XOD.setText("��� ���");
    				}
        		}		
        		else
        			gs.label_i.setText("������������ ����������");
        			//out.writeByte(toServ);
        		
        	}
        }
    }
	
	public boolean CheckDead()
	{
		int ind = gs.myField[gs.X][gs.Y].indexOfShip;
		int n = gs.ships[ind].len;
		int xx,yy;
		
		for(int i = 0; i < n; i++)
		{
			xx = gs.ships[ind].arrOfXY[2*i];
			yy = gs.ships[ind].arrOfXY[2*i+1];
			if(!gs.myField[xx][yy].IsFired)
				return false;
		}
		return true;
	}
	
	public boolean FindXY(MouseEvent arg0)
	{
		int tempX=-1;
		int tempY=-1;
		gs.x = arg0.getX();
		gs.X = -1;
		gs.y = arg0.getY();
		gs.Y = -1;
		
		tempX = gs.x - 20;
		tempY = gs.y - 26;
		
		if(gs.x < 315 && gs.x > 20 && gs.y < 321 && gs.y > 26){
			gs.X = tempX / 30;
			gs.Y = tempY / 30;
			gs.XY = gs.X + 10 * gs.Y;
			//label.setText("x= "+x+" y= "+y+"X= "+X+"Y = "+Y+" XY="+XY);
			return true;
		}
		return false;
	}
	
	public void SetCurrLen(int currInd)
	{
		switch(currInd)
		{
			case 0:
				gs.currLen = 4;
			break;
			case 1: case 2:
				gs.currLen = 3;
			break;
			case 3: case 4: case 5: 
				gs.currLen = 2;
			break;
			case 6: case 7: case 8: case 9: 
				gs.currLen = 1;
			break;
		}
	}
	
	public void markShips()
	{
		gs.currIndex = 0;
		SetCurrLen(gs.currIndex);
		CreateShip(gs.currLen, gs.currIndex);	
	}
	
	public void CreateShip(int len,int ind)
	{
		gs.ships[ind] = new Ship(len);
		
	}
	public boolean IsLine()
	{
		Ship sh = gs.ships[gs.currIndex];
		Integer Xx = sh.arrOfXY[0];
		Integer Yy = sh.arrOfXY[1];
		
		gs.lX = true;
		gs.lY = true;
		
		Integer tempX = 0, xMIN = Xx, xMAX = Xx;
		Integer tempY = 0, yMIN = Yy, yMAX = Yy;
		for(int i = 1; i < gs.currLen; i++){	
			tempX = sh.arrOfXY[2*i];
			tempY = sh.arrOfXY[2*i+1];
			
			if(!Xx.equals(tempX)) 
				gs.lX=false;
			
			if(!Yy.equals(tempY))  
				gs.lY=false;
			
			if(tempX < xMIN) xMIN=tempX;
			if(tempY < yMIN) yMIN=tempY;
			if(tempX > xMAX) xMAX=tempX;
			if(tempY > yMAX) yMAX=tempY;
		}
		
		if(gs.lX)
			gs.decks.setText("� ���� ����� �� Y");
		else 
			if(gs.lY)
				gs.decks.setText("� ���� ����� �� X");
		
		if(!gs.lX && !gs.lY)
		{
			gs.decks.setText("�� � ���� �����!");
			return false;
		}
		else // IsSequence
		{
			if ( (gs.lX && ((yMAX-yMIN) == gs.currLen-1)) || (gs.lY && ((xMAX-xMIN) == gs.currLen-1 )) )
			{
				gs.decks.setText("������� ��� ��������!");
				
			}
			else
			{
				gs.decks.setText("�� ������");
				return false;
			}	
		}
		return true;
	}
	
	public boolean CheckShip(){
		if( gs.currLen != (gs.indexInShip) )
		{
			gs.decks.setText("���� �����!");
			return false;
		}
		if (!IsLine())
		{
			return false;
		}
		return true;			
	}
	
	public void DrawSea(Icon image1, Icon image2, GridBagConstraints c)
	{	
		for (int i = 0; i < 10; i++)
		{
			for (int j = 0; j < 10; j++)
			{
				gs.enemyField[j][i] = new MyLabel();
				gs.myField[j][i] = new MyLabel();
				
				gs.myField[j][i].setIcon(image1);
				gs.enemyField[j][i].setIcon(image1);
								
				c.gridx = i;
				c.gridy = j;
				c.gridwidth = 1;
				c.gridheight = 1;

				gs.myPanel.add (gs.myField[j][i], c);
				
				c.gridx = i+10;
				c.gridy = j;
				c.gridwidth = 1;
				c.gridheight = 1;
				
				gs.enemyPanel.add(gs.enemyField[j][i], c);	
			}
		}
		gs.XOD	= new JLabel("���� ��� �� ��������...");
		gs.XOD.setForeground(Color.RED);
		Font font = new Font("Verdana", Font.BOLD, 24);
		gs.XOD.setFont(font);
		gs.XOD.setVisible(true);
		
		gs.enemyPanel.add(gs.XOD);
	}
	
     public void createAndShowGUI()
   {
		m_frame = new JFrame("CLIENT");
		m_frame.setSize(1043,463);
		m_frame.setVisible(true);
		m_frame.setLocation(0, 500);	
		m_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		m_frame.setResizable(false);
		
    	gs = new GameState();
		
		gs.myPanel  = new JPanel();
		gs.enemyPanel = new JPanel();
		
		gs.myPanel.setBorder(BorderFactory.createTitledBorder("��� ����"));
		gs.enemyPanel.setBorder(BorderFactory.createTitledBorder("���� �������"));
		GridBagConstraints c  = new GridBagConstraints();
		
		URL url = MainWindow.class.getResource("images/blue.jpg");
		gs.sea   		=  new ImageIcon(url);
		url = MainWindow.class.getResource("images/red.jpg");
		gs.dead  		=  new ImageIcon(url);
		url = MainWindow.class.getResource("images/green.jpg");
		gs.green  		=  new ImageIcon(url);
		url = MainWindow.class.getResource("images/orange.jpg");
		gs.injured  	=  new ImageIcon(url);
		url = MainWindow.class.getResource("images/yellow.jpg");
		gs.marked  	=  new ImageIcon(url);
		url = MainWindow.class.getResource("images/mimo.jpg");
		gs.mimo  		=  new ImageIcon(url);
			
		DrawSea(gs.sea,gs.dead,c);
		
		gs.decks = new JLabel();
		final JPanel panel_palub = new JPanel();
		gs.myPanel.add(panel_palub,BorderLayout.SOUTH);
			panel_palub.setLayout(new GridLayout(0,2));
			gs.label_i = new JLabel("�������� �������");
			gs.label_i.setVisible(false);
			panel_palub.setVisible(false);
			panel_palub.add(gs.label_i);
			final JButton buttonAdd = new JButton("��������");
			buttonAdd.setVisible(false);
			panel_palub.add(buttonAdd);
			
				JPanel panel_decks=new JPanel();
				panel_palub.add(panel_decks,BorderLayout.SOUTH);
				gs.decks.setVisible(false);
				panel_decks.add(gs.decks);
		
		gs.myPanel.addMouseListener(
			new MouseListener()
			{				
				@Override
				public void mouseClicked(MouseEvent arg0)
				{
					// TODO Auto-generated method stub
					if (gs.flag_newShips )
					{
						
						if( FindXY(arg0) )
							{					
									
									if(!gs.myField[gs.X][gs.Y].IsShip && 
											!gs.myField[gs.X][gs.Y].mark && 
												!gs.myField[gs.X][gs.Y].ambit) 
									{
										//decks.setText("CurrInd= "+currIndex+" CurrL= "+currLen+" IndInShip= "+indexInShip);
										if(gs.indexInShip == gs.currLen)
										{
											//decks.setText("�����! "+"CurrL: "+currLen+" IndInShip= "+indexInShip);
											gs.decks.setText("����� �����!");
										}
										else
										{
											
											gs.myField[gs.X][gs.Y].setIcon(gs.green);
											gs.myField[gs.X][gs.Y].indexOfShip=gs.currIndex;
											gs.ships[gs.currIndex].arrOfXY[2*gs.indexInShip]=gs.X;
											gs.ships[gs.currIndex].arrOfXY[2*gs.indexInShip+1]=gs.Y;									
											//decks.setText("CurrInd= "+currIndex+" CurrL= "+currLen+" IndInShip= "+indexInShip);	
											gs.myField[gs.X][gs.Y].IsShip=true;
											gs.indexInShip++;
										}
									}
									else
										if(!gs.myField[gs.X][gs.Y].mark && !gs.myField[gs.X][gs.Y].ambit)
										{
											gs.myField[gs.X][gs.Y].setIcon(gs.sea);
											gs.myField[gs.X][gs.Y].IsShip=false;
											gs.ships[gs.currIndex].Del(gs.X,gs.Y, gs.indexInShip);
											gs.indexInShip--; 
										//	decks.setText("CurrInd= "+currIndex+" CurrL= "+currLen+" IndInShip= "+indexInShip);
											
										}
										else
											if(gs.myField[gs.X][gs.Y].ambit)
												gs.decks.setText("�� ����� ����� ����");
							}
						
					}
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

			}
		);
		
		gs.enemyPanel.addMouseListener(
				new MouseListener()
				{				
					@Override
					public void mouseClicked(MouseEvent arg0) 
					{
						// TODO Auto-generated method stub
						if (gs.flag_game && gs.flag_xoda){
							gs.flag_choose = false;
							if(FindXY(arg0))
							{	
								if(!gs.enemyField[gs.X][gs.Y].IsFired)
								{
									gs.enemyField[gs.X][gs.Y].IsFired=true;
									gs.flag_choose=true;
								}
								else
									gs.label_i.setText("��� �������� ����!");
							}
						}
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
				}
		);
		
		gs.label = new JLabel("���������: ");                              
		gs.label_conn = new JLabel("��������� ����������: ");
		gs.closeButton = new JButton("��������� ����������");
		gs.sendButton = new JButton("��������� ��������� �������");
		gs.connectButton = new JButton("������������ � ����");
		gs.textSend = new JTextField("");
		gs.textIP  = new JTextField("127.0.0.1");
		gs.textPORT= new JTextField("port");
		
		gs.shipButton = new JButton("��������� �������");
		
		m_frame.setLayout(new GridLayout(1,3,10,5));
			JPanel panelButton = new JPanel();
			
		m_frame.add(gs.myPanel,1,0);
		m_frame.add(gs.enemyPanel,2,0);
		m_frame.add(panelButton,3,0);
		
			panelButton.setLayout(new GridLayout(9,1,10,10));
			panelButton.add(gs.connectButton);
			panelButton.add(gs.label);
			panelButton.add(gs.label_conn);
			panelButton.add(gs.textSend);
			
			panelButton.add(gs.sendButton);
			
			panelButton.add(gs.shipButton,BorderLayout.SOUTH);
			panelButton.add(gs.textIP);
			panelButton.add(gs.textPORT);
			panelButton.add(gs.closeButton);

		gs.shipButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				if(!gs.flag_newShips)
				{
					gs.label.setText("�������� �������� ��������");
					gs.label_i.setVisible(true);
					panel_palub.setVisible(true);
					buttonAdd.setVisible(true);
					gs.flag_newShips = true;
					gs.decks.setVisible(true);
					gs.label_i.setText("�������� 4-���������");
					gs.indexInShip = 0;
					markShips();	
					gs.shipButton.setEnabled(false);
				}
			}
		} );
		
		buttonAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{	
				if(CheckShip())
				{
					gs.currIndex++;
					if (gs.currIndex == 10)
					{
						gs.flag_newShips=false;
						gs.flag_readyShips=true;
						gs.decks.setText("�������� ���������!");
						gs.label_i.setText("�������� �������...");
						
						buttonAdd.setVisible(false);
						//shipButton.setEnabled(false);
						
						int x = gs.ships[9].arrOfXY[0];
						int y = gs.ships[9].arrOfXY[1];
						
						gs.myField[x][y].setIcon(gs.marked);
						gs.myField[x][y].mark = true;
					}
					else
					{
						Ship sh = gs.ships[gs.currIndex-1];
																	
						for(int i = 0; i < gs.currLen; i++)
						{
							
							int x = sh.arrOfXY[i*2];
							int y = sh.arrOfXY[i*2+1];
						
							gs.myField[x][y].setIcon(gs.marked);
							gs.myField[x][y].mark = true;
							
							// marking ambit
							for(int j = -1; j < 2; j++)
							{
								for(int g = -1; g < 2; g++)
								{
									if( x+j > -1 && x+j < 10 && y+g > -1 && y+g < 10 )
									{
										MyLabel la = gs.myField[x+j][y+g];
										if(!la.IsShip && !la.ambit)
											la.ambit=true;
									}
								}
							}
						}
						gs.decks.setText("������� ��������");
						SetCurrLen(gs.currIndex);
						gs.label_i.setText("��������" + gs.currLen + " -���������");
						CreateShip(gs.currLen, gs.currIndex);
						gs.indexInShip = 0;
					}	
				}
			}
		} );
		
		gs.closeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				try 
				{
					gs.socket.close();
					System.out.println("���������� ���������");
					gs.label.setText("���������� ���������");
				} 
			  	catch(NullPointerException x)
			  	{
			  		gs.label_conn.setText("NullPointerException");
			  		gs.label.setText("������������ �� �������!");
			    }
				catch (IOException e) 
				{
					gs.label_conn.setText("IOException");
					gs.label.setText("C��������� �� �����������");
				}
			}				
		} );
		return;
   }  
}
