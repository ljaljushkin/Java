package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;


public class ClientThread extends Thread{
	
	
	public Socket m_clientSocket;
	
	public GameState m_gameState;
	
	public ClientThread(GameState gameState){
		m_gameState = gameState;
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
	}
	
	
public boolean allCantEat(){
	for (int i=0; i<8; i++){
		for (int j=0; j<8; j++){
			if (canEat(i,j)) return false;
		}
	}
	return true;
}

public boolean canEat(int x, int y){
	
	if (gs.wasEat&&(x!=gs.prevEatX||y!=gs.prevEatY)) return false;
	
	int ind=x*8+y;
	if (gs.nTeam==2){
		if (gs.gameState[ind]!=1&&gs.gameState[ind]!=3&&gs.gameState[ind]!=5&&gs.gameState[ind]!=7) return false;
	}
	if (gs.nTeam==1){
		if (gs.gameState[ind]!=2&&gs.gameState[ind]!=4&&gs.gameState[ind]!=6&&gs.gameState[ind]!=8) return false;
	}
	
	

	
	//if (gs.gameState[ind]<5){
		
	int nx,ny;
	
	boolean x1=true;
	boolean x2=true;
	boolean x3=true;
	boolean x4=true;
	
	nx=x+2;
	ny=y+2;
	ind=(x+1)*8+(y+1);
	if (gs.gameState[x*8+y]>4){
		nx=x; ny=y;
		while (nx+1<8&&nx+1>=0&&ny+1<8&&ny+1>=0&&gs.gameState[(nx+1)*8+(ny+1)]==0){
			nx++; ny++;
		}
		nx+=2;
		ny+=2;
		ind=(nx-1)*8+(ny-1);
	}
	
	if (nx>7||nx<0||ny>7||ny<0){
		x1=false;
	}else{
		if (gs.gameState[nx*8+ny]!=0){
			x1=false;
		}
		if (gs.nTeam==2){
			if (gs.gameState[ind]!=2&&gs.gameState[ind]!=6) x1=false;
		}
		if (gs.nTeam==1){
			if (gs.gameState[ind]!=1&&gs.gameState[ind]!=5) x1=false;
		}
	}
	if (gs.wasEat&&x==gs.prevEatX&&y==gs.prevEatY
			&& gs.signWayX==-1 && gs.signWayY==-1){
		x1=false;
	}
	
	
	nx=x-2;
	ny=y-2;
	ind=(x-1)*8+(y-1);
	if (gs.gameState[x*8+y]>4){
		nx=x; ny=y;
		while (nx-1<8&&nx-1>=0&&ny-1<8&&ny-1>=0&&gs.gameState[(nx-1)*8+(ny-1)]==0){
			nx--; ny--;
		}
		nx-=2;
		ny-=2;
		ind=(nx+1)*8+(ny+1);
	}
	if (nx>7||nx<0||ny>7||ny<0){
		x2=false;
	}else{
		if (gs.gameState[nx*8+ny]!=0){
			x2=false;
		}
		if (gs.nTeam==2){
			if (gs.gameState[ind]!=2&&gs.gameState[ind]!=6) x2=false;
		}
		if (gs.nTeam==1){
			if (gs.gameState[ind]!=1&&gs.gameState[ind]!=5) x2=false;
		}
	}
	if (gs.wasEat&&x==gs.prevEatX&&y==gs.prevEatY
			&& gs.signWayX==1 && gs.signWayY==1){
		x2=false;
	}
	
	
	
	nx=x+2;
	ny=y-2;
	ind=(x+1)*8+(y-1);
	if (gs.gameState[x*8+y]>4){
		nx=x; ny=y;
		while (nx+1<8&&nx+1>=0&&ny-1<8&&ny-1>=0&&gs.gameState[(nx+1)*8+(ny-1)]==0){
			nx++; ny--;
		}
		nx+=2;
		ny-=2;
		ind=(nx-1)*8+(ny+1);
	}
	if (nx>7||nx<0||ny>7||ny<0){
		x3=false;
	}else{
		if (gs.gameState[nx*8+ny]!=0){
			x3=false;
		}
		if (gs.nTeam==2){
			if (gs.gameState[ind]!=2&&gs.gameState[ind]!=6) x3=false;
		}
		if (gs.nTeam==1){
			if (gs.gameState[ind]!=1&&gs.gameState[ind]!=5) x3=false;
		}
	}
	if (gs.wasEat&&x==gs.prevEatX&&y==gs.prevEatY
			&& gs.signWayX==-1 && gs.signWayY==1){
		x3=false;
	}
	
	
	
	nx=x-2;
	ny=y+2;
	ind=(x-1)*8+(y+1);
	if (gs.gameState[x*8+y]>4){
		nx=x; ny=y;
		while (nx-1<8&&nx-1>=0&&ny+1<8&&ny+1>=0&&gs.gameState[(nx-1)*8+(ny+1)]==0){
			nx--; ny++;
		}
		nx-=2;
		ny+=2;
		ind=(nx+1)*8+(ny-1);
	}
	if (nx>7||nx<0||ny>7||ny<0){
		x4=false;
	}else{
		if (gs.gameState[nx*8+ny]!=0){
			x4=false;
		}
		if (gs.nTeam==2){
			if (gs.gameState[ind]!=2&&gs.gameState[ind]!=6) x4=false;
		}
		if (gs.nTeam==1){
			if (gs.gameState[ind]!=1&&gs.gameState[ind]!=5) x4=false;
		}
	}
	if (gs.wasEat&&x==gs.prevEatX&&y==gs.prevEatY
			&& gs.signWayX==1 && gs.signWayY==-1){
		x4=false;
	}
	
	return x1||x2||x3||x4;

	
	//return false;
}

public boolean checkMove(int x, int y, int nx, int ny){
	
	boolean ace=allCantEat();
	boolean ce=canEat(x,y);
	if	(!ace&&!ce) return false;
	
	if (nx<0||nx>7||ny<0||ny>7){
		return false;
	}
	if (gs.nTeam==2){
		if (ace&&nx==x+1&&ny==y+1) return true;
		if (ace&&nx==x-1&&ny==y+1) return true;
	}
	if (gs.nTeam==1){
		if (ace&&nx==x+1&&ny==y-1) return true;
		if (ace&&nx==x-1&&ny==y-1) return true;
	}
	
	if (ce&&nx==x+2&&ny==y+2) return true;
	if (ce&&nx==x-2&&ny==y-2) return true;
	if (ce&&nx==x+2&&ny==y-2) return true;
	if (ce&&nx==x-2&&ny==y+2) return true;
	
	if (gs.gameState[x*8+y]>4){
		int nnx=x, nny=y;
		do{
			nnx++;
			nny++;
			if (!ce&&nnx==nx&&nny==ny) return true;
		}while(nnx>=0&&nnx<8&&nny>=0&&nny<8&&gs.gameState[nnx*8+nny]==0);
		if (nnx>=0&&nnx<8&&nny>=0&&nny<8&&
				ce	&& (gs.gameState[nnx*8+nny]%2==1&&gs.nTeam==1 
				|| gs.gameState[nnx*8+nny]%2==0&&gs.nTeam==2)){
			do{
				nnx++;
				nny++;
				if (nnx==nx&&nny==ny) return true;
			}while(nnx>=0&&nnx<8&&nny>=0&&nny<8&&gs.gameState[nnx*8+nny]==0);
		}
	
		
		nnx=x; nny=y;
		do{
			nnx--;
			nny--;
			if (!ce&&nnx==nx&&nny==ny) return true;
		}while (nnx>=0&&nnx<8&&nny>=0&&nny<8&&gs.gameState[nnx*8+nny]==0);
		if (nnx>=0&&nnx<8&&nny>=0&&nny<8&&
				ce	&& (gs.gameState[nnx*8+nny]%2==1&&gs.nTeam==1 
				|| gs.gameState[nnx*8+nny]%2==0&&gs.nTeam==2)){
			do{
				nnx--;
				nny--;
				if (nnx==nx&&nny==ny) return true;
			}while(nnx>=0&&nnx<8&&nny>=0&&nny<8&&gs.gameState[nnx*8+nny]==0);
		}
		
		
		
		nnx=x; nny=y;
		do{
			nnx++;
			nny--;
			if (!ce&&nnx==nx&&nny==ny) return true;
		}while (nnx>=0&&nnx<8&&nny>=0&&nny<8&&gs.gameState[nnx*8+nny]==0);
		if (nnx>=0&&nnx<8&&nny>=0&&nny<8&&
				ce	&& (gs.gameState[nnx*8+nny]%2==1&&gs.nTeam==1 
				|| gs.gameState[nnx*8+nny]%2==0&&gs.nTeam==2)){
			do{
				nnx++;
				nny--;
				if (nnx==nx&&nny==ny) return true;
			}while(nnx>=0&&nnx<8&&nny>=0&&nny<8&&gs.gameState[nnx*8+nny]==0);
		}
		
		
		
		
		nnx=x; nny=y;
		do{
			nnx--;
			nny++;
			if (!ce&&nnx==nx&&nny==ny) return true;
		}while (nnx>=0&&nnx<8&&nny>=0&&nny<8&&gs.gameState[nnx*8+nny]==0);
		if (nnx>=0&&nnx<8&&nny>=0&&nny<8&&
				ce	&& (gs.gameState[nnx*8+nny]%2==1&&gs.nTeam==1 
				|| gs.gameState[nnx*8+nny]%2==0&&gs.nTeam==2)){
			do{
				nnx--;
				nny++;
				if (nnx==nx&&nny==ny) return true;
			}while(nnx>=0&&nnx<8&&nny>=0&&nny<8&&gs.gameState[nnx*8+nny]==0);
		}

	
	}
	
	return false;
}
*/

/*public boolean makeMove(int x, int y, int nx, int ny, boolean dist){
	
	//Boolean aa=new Boolean(checkMove(x,y,nx,ny));
	//gs.out.append(aa.toString());	
	
	if ((!dist&&checkMove(x,y,nx,ny))||dist){
		int tmp=gs.gameState[x*8+y];
		gs.gameState[x*8+y]=gs.gameState[nx*8+ny];
		if (dist) gs.gameState[nx*8+ny]=tmp;
		else gs.gameState[nx*8+ny]=tmp-2;
		gs.wasSel=false;
		
		/*if (canEat(x,y)&&Math.abs(nx-x)==2||Math.abs(ny-y)==2){
			int nnx=x+(nx-x)/2;
			int nny=y+(ny-y)/2;
			gs.gameState[nnx*8+nny]=0;
			gs.wasEat=true;
		}*/
		
		/*if (Math.abs(nx-x)>1||Math.abs(ny-y)>1){
			int nnx=x;
			int nny=y;
			gs.signWayX=(int) Math.signum(nx-x);
			gs.signWayY=(int) Math.signum(ny-y);
			do{
				nnx+=Math.signum(nx-x);
				nny+=Math.signum(ny-y);
				
				if (nnx==nx&&nny==ny){
					gs.prevEatX=nx;
					gs.prevEatY=ny;
					break;
				}
				if (gs.gameState[nnx*8+nny]!=0){
					gs.wasEat=true;
				}
				gs.gameState[nnx*8+nny]=0;
			}while(nnx<8&&nnx>=0&&nny<8&&nny>=0);
		}
		
		
		if (ny==0){
			if (gs.gameState[nx*8+ny]==2) gs.gameState[nx*8+ny]=6;
		}
		if (ny==7){
			if (gs.gameState[nx*8+ny]==1) gs.gameState[nx*8+ny]=5;
		}
		try {
			if (!dist){
				ObjectOutputStream oos= new ObjectOutputStream(client.getOutputStream());
				oos.writeInt(1);
				oos.writeInt(x);
				oos.writeInt(y);
				oos.writeInt(nx);
				oos.writeInt(ny);
				oos.flush();
				if (gs.wasEat&&allCantEat()||!gs.wasEat){
					gs.myTurn=false;
					gs.out.setText("");
					gs.out.append("Opponents turn!\n");
					gs.wasEat=false;
				}
			}else{
				if (gs.nTeam==1) gs.nTeam=2;
				else gs.nTeam=1;
				if (gs.wasEat&&allCantEat()||!gs.wasEat){
					gs.myTurn=true;
					gs.out.setText("");
					gs.out.append("Your turn!\n");
					gs.wasEat=false;
				}
				if (gs.nTeam==1) gs.nTeam=2;
				else gs.nTeam=1;
			}
		} catch (IOException e) {
			gs.out.setText("lost connection!");
			cleanGameState();
		}
		refreshGameField();
	}else{
		gs.out.append("Wrong turn!\n");
	}
	
	return true;
	
	
}
*/
	
	public void run(){
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
