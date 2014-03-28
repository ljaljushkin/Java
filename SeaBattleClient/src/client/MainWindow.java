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
	static int x; // большие - коорди0наты 10х10
	static int y;
	static int X; // маленькие - экранные
	static int Y;
	static int XY; // X+10*Y
	static int currIndex=0; //индекс текущего корабля при добавлении(от нуля)
	static int currLen=0;
	static int indexInShip=0;
	static boolean lX = true,lY=true;
	
    /*public static void main(String[] ar) 
    {
    	MyRunnable runnable=new MyRunnable();
 	    SwingUtilities.invokeLater(runnable);
    }*/
    
    static Socket 		socket;
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
    
    static  JPanel panel;
    static  JPanel panel2;
    
    static Ship[]	ships   = new Ship[10];
    static MyLabel[][] labelNew2 = new MyLabel[10][10];
    static MyLabel[][] labelNew  = new MyLabel[10][10];
 	
    static boolean flag_newShips=false;
    static boolean flag_game=false;
    static boolean flag_readyShips=false;// размечены ли они?
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
    
    static boolean flag = true;
    
    public MainWindow() 
    {
    	createAndShowGUI(); //TODO: run in separate thread
    }
	public static void RunClient()
    {
		int serverPort = 0;
		try{
			serverPort=Integer.parseInt(textPORT.getText());
			//Thread.sleep(1000);
		}
		catch(NullPointerException x){
        	label_conn.setText("Не корректный порт");
        	label.setText("Подключиться не удалось!");
        }
		catch(Exception e){
			label_conn.setText("Проверьте правильность ввода порта");
		}
		/*if(serverPort == 0)
			label_conn.setText("Проверьте правильность ввода порта");*/
		
		String address=textIP.getText();
		boolean fip=false;
        try {
        	InetAddress ipAddress = null;
        	try{
            ipAddress = InetAddress.getByName(address); // создаем объект который отображает вышеописанный IP-адрес.
        	}
        	catch(UnknownHostException e){
        		label_conn.setText("UnknownHostException: некорректный IP");
        		fip=true;
        	}
            System.out.println("Any of you heard of a socket with IP address " + address + " and port " + serverPort + "?");
            try{
            	socket = new Socket(ipAddress, serverPort); // создаем сокет используя IP-адрес и порт сервера.
            }
            catch(ConnectException x){
            	label_conn.setText("Сервер не запущен!");
            }
            catch(IllegalArgumentException x){
            	label_conn.setText("Не корректные данные");
            }
            catch(NullPointerException x){
            	label_conn.setText("NullPointerException: че-то с данными!");
            }
            catch(Exception x){
            	label_conn.setText("Exception");
            }
           
        	// Берем входной и выходной потоки сокета, теперь можем получать и отсылать данные клиентом. 
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
            label_conn.setText(line);
             
            label.setText("Раставьте корабли"); 
            while(flag)
            {
            	if(flag_readyShips)
            	{
            		 out.writeUTF("Клиент разметил корабли!");
            		 out.flush(); // заставляем поток закончить передачу данных.
            		 flag_readyShips=false;
            		 flag=false;
            		 flag_game=true;
            		 flag_xoda=false; // Server is the first
            		 
            	}
            }
            
            line = in.readUTF(); // ждем пока сервер пришлет, что разметил корабли
            label_conn.setText(line);
            XOD.setText("ХОДИТ СЕРВЕР");
            label_i.setText("Ходит Сервер...");
            
            currLen=0;
            indexInShip=0;
            flag=true;
            while(flag){
            	if(flag_xoda)// ваш ход
            	{
					if(flag_choose) // если выбрана клетка
					{
						flag_choose=false;
						byte fromServer=-1;
						out.writeUTF(X+","+Y);
						label_i.setText("Вы выстрелили по: "+X+" "+Y);
						
					 	fromServer=in.readByte();
					 	switch(fromServer){
						case 0: 
							labelNew2[X][Y].setIcon(mimo);
							labelNew2[X][Y].IsShip=false;
							flag_xoda=false;
							decks.setText("Мимо!");
							XOD.setText("ХОДИТ СЕРВЕР");
							break;
						case 1:
							labelNew2[X][Y].setIcon(injured);
							labelNew2[X][Y].IsShip=true;
							decks.setText("Ранили!");
							XOD.setText("ВАШ ХОД");
							flag_xoda=true;
							break;
						case 2:
							line = in.readUTF();
        			 		StringTokenizer st = new StringTokenizer(line, ",");
        			 		while(st.hasMoreTokens())
        			 		{
	                    		X=Integer.parseInt(st.nextToken());
	                    		Y=Integer.parseInt(st.nextToken());
	                    		labelNew2[X][Y].IsShip=true;
	                    		
	                    		for(int j=-1;j<2;j++)
								{
									for(int g=-1;g<2;g++)
									{
										if( X+j>-1 && X+j<10 && Y+g>-1 && Y+g<10 )
										{
											MyLabel la=labelNew2[X+j][Y+g];
											if(!la.IsShip)
											la.setIcon(mimo);
											la.IsFired=true;
										}
									}
								}
	                    	
	                    		labelNew2[X][Y].setIcon(dead);
        			 		}
        			 		decks.setText("Корабль убит!");
        			 		XOD.setText("ВАШ ХОД");
        			 		flag_xoda=true;
        			 		currLen++;
        			 		if(currLen==10){
        			 			flag=false;
        			 			XOD.setText("YOU WIN!!!");
        			 		}
							break;
						 }
					}
           		}
            	else // сервер стреляет
            	{
            		line = in.readUTF();
            		StringTokenizer st = new StringTokenizer(line, ",");
            		X=Integer.parseInt(st.nextToken());
            		Y=Integer.parseInt(st.nextToken());
            		MyLabel lN=labelNew[X][Y];
            		label_i.setText("Cервер выстрелил по: "+X+" "+Y);
            		byte toServ=-1;
            		if(X>-1 && X<10 && Y>-1 && Y<10)
            		{
            			lN.IsFired=true;
            			if(lN.IsShip )
        				{
        					if(CheckDead())
        					{	
        						toServ=2;
        						out.writeByte(toServ);
        						int ind=lN.indexOfShip;
        						int n=ships[ind].len;
        						int xx,yy;
        						String temp = "";
        						for(int i=0;i<n;i++)
        						{
        							xx=ships[ind].arrOfXY[2*i];
        							yy=ships[ind].arrOfXY[2*i+1];
        							labelNew[xx][yy].setIcon(dead);
        							temp=temp + xx+","+yy+",";
        						}
        						decks.setText("Ваш корабль убит!");
        						XOD.setText("ХОДИТ СЕРВЕР");
        						out.writeUTF(temp);
        						indexInShip++;
    							if(indexInShip==10){
            			 			flag=false;
            			 			XOD.setText("YOU LOSE!!!");
            			 		}
        					}
        					else
        					{
        						lN.setIcon(injured);
        						toServ=1;
        						out.writeByte(toServ);
        						decks.setText("Ваш корабль ранен!");
        						XOD.setText("ХОДИТ СЕРВЕР");
        					}
        				}
        				else{
        					lN.setIcon(mimo);
        					toServ=0;
        					out.writeByte(toServ);
        					flag_xoda=true;
        					decks.setText("Сервер промазал!");
        					XOD.setText("ВАШ ХОД");
        				}
            		}		
            		else
            			label_i.setText("Некорректные координаты");
            			//out.writeByte(toServ);
            		
            	}
            }
            
       }
       
        catch(NullPointerException x){
        	if(serverPort == 0)
    			label_conn.setText("Проверьте правильность ввода порта");
        	else
        		if(fip)
            		label_conn.setText("Не корректный IP");
        		else
        			label_conn.setText("Не верный порт или IP");
        	label.setText("Подключиться не удалось!");
        }
        catch(SocketException x){
        	if(fip)
        		label_conn.setText("Некорректный IP");
        	else
        		label_conn.setText("SocketException");
        	label.setText("Cоединение не установлено");
        }
        catch (Exception x) 
        {	
            label.setText("Exception.");
            label.setText("Cоединение не установлено");
            x.printStackTrace();
        }
        
    }
	public static boolean CheckDead(){
		int ind=labelNew[X][Y].indexOfShip;
		int n=ships[ind].len;
		int xx,yy;
		for(int i=0;i<n;i++)
		{
			xx=ships[ind].arrOfXY[2*i];
			yy=ships[ind].arrOfXY[2*i+1];
			if(!labelNew[xx][yy].IsFired)
				return false;
		}
		return true;
	}
	public static boolean FindXY(MouseEvent arg0)
	{
		int tempX=-1;
		int tempY=-1;
		x=arg0.getX();X=-1;
		y=arg0.getY();Y=-1;
		
		tempX=x-20;
		tempY=y-26;
		
		
		if(x<315 && x>20 && y<321 && y>26){
			X=tempX/30;
			Y=tempY/30;
			XY=X+10*Y;
			//label.setText("x= "+x+" y= "+y+"X= "+X+"Y = "+Y+" XY="+XY);
			return true;
		}
		return false;
	}
	public static void SetCurrLen(int currInd)
	{
		switch(currInd)
		{
		case 0:
			currLen=4;
		break;
		case 1: case 2:
			currLen=3;
		break;
		case 4: case 5: case 3:
			currLen=2;
		break;
		case 7: case 8: case 9: case 6:
			currLen=1;
		break;
		
		}
	}
	public static void markShips()
	{
		currIndex=0;
		SetCurrLen(currIndex);
		CreateShip(currLen,currIndex);
		
	}
	public static void CreateShip(int len,int ind)
	{
		ships[ind] = new Ship(len);
		
	}
	public static boolean IsLine()
	{
		Ship sh=ships[currIndex];
		Integer Xx=sh.arrOfXY[0];
		Integer Yy=sh.arrOfXY[1];
		
		lX=true;
		lY=true;
		
		Integer tempX=0,xMIN=Xx,xMAX=Xx;
		Integer tempY=0,yMIN=Yy,yMAX=Yy;
		for(int i=1;i<currLen;i++){	
			tempX=sh.arrOfXY[2*i];
			tempY=sh.arrOfXY[2*i+1];
			
			if(!Xx.equals(tempX)) 
				lX=false;
			
			if(!Yy.equals(tempY))  
				lY=false;
			
			if(tempX<xMIN)		xMIN=tempX;
			if(tempY<yMIN)		yMIN=tempY;
			if(tempX>xMAX)		xMAX=tempX;
			if(tempY>yMAX)		yMAX=tempY;
		}
		if(lX)
			decks.setText("В одну линию по Y");
		else 
			if(lY)
				decks.setText("В одну линию по X");
		
		if(!lX && !lY)
		{
			decks.setText("Не в одну линию!");
			return false;
		}
		else // IsSequence
		{
			if ( (lX && ((yMAX-yMIN) == currLen-1)) || (lY && ((xMAX-xMIN) == currLen-1 )) )
			{
				decks.setText("корабль был добавлен!");
				
			}
			else
			{
				decks.setText("Не подряд");
				return false;
			}
			
		}
		
		return true;
		
	}
	public static boolean CheckShip(){
		if( currLen != (indexInShip) )
		{
			decks.setText("Мало палуб!");
			return false;
		}
		if (!IsLine())
		{
			return false;
		}
		
		
		return true;
			
	}
	public static void DrawSea(Icon image1,Icon image2,GridBagConstraints c)
	{
		for (int i=0; i<10;i++)
		{
			for (int j=0; j<10;j++)
			{
				labelNew2[j][i] = new MyLabel();
				labelNew[j][i]  = new MyLabel();
				
				labelNew[j][i].setIcon(image1);
				labelNew2[j][i].setIcon(image1);
								
				c.gridx = i;
				c.gridy = j;
				c.gridwidth=1;
				c.gridheight=1;

				panel.add (labelNew[j][i], c);
				
				c.gridx = i+10;
				c.gridy = j;
				c.gridwidth=1;
				c.gridheight=1;
				
				panel2.add(labelNew2[j][i], c);
				
			}
		}
		XOD	  = new JLabel("Игра еще не началась...");
		XOD.setForeground(Color.RED);
		Font font = new Font("Verdana", Font.BOLD, 24);
		XOD.setFont(font);
		XOD.setVisible(true);
		
		panel2.add(XOD);
	}
	
 
	
     static void createAndShowGUI()
   {
		JFrame frame = new JFrame("CLIENT");
		frame.setSize(1043,463);
		frame.setVisible(true);
		frame.setLocation(0, 500);	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		panel  = new JPanel();
		panel2 = new JPanel();
		
		panel.setBorder(BorderFactory.createTitledBorder("ВАШ ФЛОТ"));
		panel2.setBorder(BorderFactory.createTitledBorder("ФЛОТ СЕРВЕРА"));
		GridBagConstraints c  = new GridBagConstraints();
		
		URL url = MainWindow.class.getResource("images/blue.jpg");
		sea   		=  new ImageIcon(url);
		url = MainWindow.class.getResource("images/red.jpg");
		dead  		=  new ImageIcon(url);
		url = MainWindow.class.getResource("images/green.jpg");
		green  		=  new ImageIcon(url);
		url = MainWindow.class.getResource("images/orange.jpg");
		injured  	=  new ImageIcon(url);
		url = MainWindow.class.getResource("images/yellow.jpg");
		marked  	=  new ImageIcon(url);
		url = MainWindow.class.getResource("images/mimo.jpg");
		mimo  		=  new ImageIcon(url);
			
		DrawSea(sea,dead,c);
		
		decks = new JLabel();
		final JPanel panel_palub = new JPanel();
		panel.add(panel_palub,BorderLayout.SOUTH);
			panel_palub.setLayout(new GridLayout(0,2));
			label_i = new JLabel("Выберите корабль");
			label_i.setVisible(false);
			panel_palub.setVisible(false);
			panel_palub.add(label_i);
			final JButton buttonAdd = new JButton("Добавить");
			buttonAdd.setVisible(false);
			panel_palub.add(buttonAdd);
			
				JPanel panel_decks=new JPanel();
				panel_palub.add(panel_decks,BorderLayout.SOUTH);
				decks.setVisible(false);
				panel_decks.add(decks);
		
		panel.addMouseListener(
			new MouseListener()
			{				
				@Override
				public void mouseClicked(MouseEvent arg0) {
					// TODO Auto-generated method stub
					if (flag_newShips )
					{
						
						if( FindXY(arg0) )
							{					
									
									if(!labelNew[X][Y].IsShip && !labelNew[X][Y].mark && !labelNew[X][Y].ambit){
										//decks.setText("CurrInd= "+currIndex+" CurrL= "+currLen+" IndInShip= "+indexInShip);
										if(indexInShip == currLen)
										{
											//decks.setText("Много! "+"CurrL: "+currLen+" IndInShip= "+indexInShip);
											decks.setText("Много палуб!");
										}
										else
										{
											
											labelNew[X][Y].setIcon(green);
											labelNew[X][Y].indexOfShip=currIndex;
											ships[currIndex].arrOfXY[2*indexInShip]=X;
											ships[currIndex].arrOfXY[2*indexInShip+1]=Y;									
											//decks.setText("CurrInd= "+currIndex+" CurrL= "+currLen+" IndInShip= "+indexInShip);	
											labelNew[X][Y].IsShip=true;
											indexInShip++;
										}
									}
									else
										if(!labelNew[X][Y].mark && !labelNew[X][Y].ambit)
										{
											labelNew[X][Y].setIcon(sea);
											labelNew[X][Y].IsShip=false;
											ships[currIndex].Del(X,Y, indexInShip);
											indexInShip--; 
										//	decks.setText("CurrInd= "+currIndex+" CurrL= "+currLen+" IndInShip= "+indexInShip);
											
										}
										else
											if(labelNew[X][Y].ambit)
												decks.setText("Не может здесь быть");
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
		
		panel2.addMouseListener(
				new MouseListener()
				{				
					@Override
					public void mouseClicked(MouseEvent arg0) {
						// TODO Auto-generated method stub
						if (flag_game&&flag_xoda){
							flag_choose=false;
							if( FindXY(arg0) )
							{	
								if(!labelNew2[X][Y].IsFired)
								{
									labelNew2[X][Y].IsFired=true;
									flag_choose=true;
								}
								else
									label_i.setText("Уже стреляли сюда!");
								
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
		
		label = new JLabel("Состояние: ");                              
		label_conn = new JLabel("Состояние соединения: ");
		closeButton = new JButton("Разорвать соединение");
		sendButton = new JButton("Отправить сообщение клиенту");
		connectButton = new JButton("Подключиться к игре");
		textSend= new JTextField("");
		textIP  = new JTextField("127.0.0.1");
		textPORT= new JTextField("port");
		
		shipButton = new JButton("Разметить корабли");
		
		frame.setLayout(new GridLayout(1,3,10,5));
			JPanel panelButton = new JPanel();
			
		frame.add(panel,1,0);
		frame.add(panel2,2,0);
		frame.add(panelButton,3,0);
		
			panelButton.setLayout(new GridLayout(9,1,10,10));
			panelButton.add(connectButton);
			panelButton.add(label);
			panelButton.add(label_conn);
			panelButton.add(textSend);
			
			panelButton.add(sendButton);
			
			panelButton.add(shipButton,BorderLayout.SOUTH);
			panelButton.add(textIP);
			panelButton.add(textPORT);
			panelButton.add(closeButton);
		
		connectButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0){
				
			//	int port=Integer.parseInt(textField.getText());
				
				MyRunClient runn=new MyRunClient();		
				Thread thread=new Thread(runn);
				thread.start();
			}
		} );
		shipButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0){
				if(!flag_newShips)
				{
					label.setText("Началась разметка кораблей");
					label_i.setVisible(true);
					panel_palub.setVisible(true);
					buttonAdd.setVisible(true);
					flag_newShips=true;
					decks.setVisible(true);
					label_i.setText("Разметка 4-палубника");
					indexInShip=0;
					markShips();	
					shipButton.setEnabled(false);
				}
			}
		} );
		
		buttonAdd.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0){
				
				if(CheckShip())
				{
					currIndex++;
					if (currIndex == 10)
					{
						flag_newShips=false;
						flag_readyShips=true;
						decks.setText("Разметка завершена!");
						label_i.setText("Ожидание сервера...");
						
						buttonAdd.setVisible(false);
						//shipButton.setEnabled(false);
						
						int x=ships[9].arrOfXY[0];
						int y=ships[9].arrOfXY[1];
						
						labelNew[x][y].setIcon(marked);
						labelNew[x][y].mark=true;
					}
					else
					{
						Ship sh=ships[currIndex-1];
																	
						for(int i=0;i<currLen;i++)
						{
							
							int x=sh.arrOfXY[i*2];
							int y=sh.arrOfXY[i*2+1];
						
							labelNew[x][y].setIcon(marked);
							labelNew[x][y].mark=true;
							
							// marking ambit
							for(int j=-1;j<2;j++)
							{
								for(int g=-1;g<2;g++)
								{
									if( x+j>-1 && x+j<10 && y+g>-1 && y+g<10 )
									{
										MyLabel la=labelNew[x+j][y+g];
										if(!la.IsShip && !la.ambit)
											la.ambit=true;
									}
								}
							}
									
						}
						decks.setText("Корабль добавлен");
						SetCurrLen(currIndex);
						label_i.setText("Разметка" + currLen+" -палубника");
						CreateShip(currLen,currIndex);
						indexInShip=0;
					}	
					
				}
			}
		} );
		
		closeButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0){

			try 
				{
					socket.close();
					System.out.println("Соединение завершено");
					label.setText("Соединение завершено");
				} 
			  	catch(NullPointerException x)
			  	{
			       	label_conn.setText("NullPointerException ");
			       	label.setText("Подключиться не удалось!");
			    }
				catch (IOException e) 
				{
					label_conn.setText("IOException");
					label.setText("Cоединение не установлено");
				}
			}
			
			
				
		} );
		
		return;
   }  
}
