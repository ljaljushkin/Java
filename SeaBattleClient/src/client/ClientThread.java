package client;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;


public class ClientThread extends Thread
{
	
	public Socket m_clientSocket;
	
	public GameState gs;
	
	public ClientThread(GameState gameState)
	{
		gs = gameState;
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
		try{
			
			//gs.out.setText("");
			
			//gs.out.append("Connection succes!\n");		
			
			//refreshGameField();
			
			/*if (gs.nTeam==2){
				gs.myTurn=false;
				gs.out.append("Opponents turn!\n");
			}
			else{
				gs.myTurn=true;
				gs.out.append("Your turn!\n");
			}
			
			
			gs.chat.setText("");*/
			
			while (true){
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
			//gs.out.setText("lost connection!");
			//cleanGameState();
		}finally{
			try {
				
				m_clientSocket.close();
			} catch (IOException e) {
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
	

	
	public void sendMessage(String mes){
		try {
			ObjectOutputStream oos= new ObjectOutputStream(m_clientSocket.getOutputStream());
			oos.writeInt(0);
			oos.writeObject(mes);
			oos.flush();
		} catch (IOException e) {
			//gs.out.setText("lost connection!");
			//cleanGameState();
		}
		
	}
}
