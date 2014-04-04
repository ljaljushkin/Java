package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameState {

	public int x; // большие - коорди0наты 10х10
	public int y;
	public int X; // маленькие - экранные
	public int Y;
	public int XY; // X+10*Y
	public int currIndex=0; //индекс текущего корабля при добавлении(от нуля)
	public int currLen=0;
	public int indexInShip=0;
	public boolean lX = true,lY=true;
    
    public Socket 		socket;
    public JButton 		connectButton;
    public JButton 		closeButton;
    public JButton 		sendButton;
    public JLabel  		label;
    public JLabel  		label_conn;
    public JTextField	textIP;
    public JTextField	textPORT;
    public JTextField	textSend;
    public JButton 		shipButton;
    public JLabel 		label_i;
    public JLabel 		decks;
    public JLabel 		XOD;
    
    public  JPanel myPanel;
    public  JPanel enemyPanel;
    
    public Ship[]	ships   = new Ship[10];
    public MyLabel[][] myField = new MyLabel[10][10];
    public MyLabel[][] enemyField  = new MyLabel[10][10];
 	
    public boolean flag_newShips=false;
    public boolean flag_game=false;
    public boolean flag_readyShips=false;// размечены ли они?
    public boolean flag_xoda=false;
    public boolean flag_choose=false;	
    
    public InputStream 	sin;
    public OutputStream sout;
    
    public Icon marked;  
    public Icon injured;
    public Icon green;
    public Icon sea;
    public Icon dead;
    public Icon mimo;
    
    public boolean flag = true;
}
