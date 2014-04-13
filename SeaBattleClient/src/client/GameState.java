package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameState 
{
	public int displayX; //��������� - ��������
	public int displayY;
	public int X; //������� - ���������� 10�10
	public int Y;
	public int XY; // X+10*Y
	public int currIndex=0; //������ �������� ������� ��� ����������(�� ����)
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
    
    public Ship[]	   ships       = new Ship[ 10 ];
    public MyLabel[][] myField     = new MyLabel[ 10 ][ 10 ];
    public MyLabel[][] enemyField  = new MyLabel[ 10 ][ 10 ];
 	
    public boolean flag_newShips = false;
    public boolean flag_game = false;
    public boolean flag_readyShips = false;// ��������� �� ���?
    public boolean flag_xoda = false;
    public boolean flag_choose = false;	
    
    public InputStream 	sin;
    public OutputStream sout;
    
    public Icon marked;  
    public Icon injured;
    public Icon green;
    public Icon sea;
    public Icon dead;
    public Icon mimo;
    
    public boolean flag = true;
    
    public void DrawSea( GridBagConstraints c )
	{	
		for ( int i = 0; i < 10; i++ )
		{
			for ( int j = 0; j < 10; j++ )
			{
				enemyField[j][i] = new MyLabel();
				myField[j][i]    = new MyLabel();
				
				myField[j][i].setIcon( sea );
				enemyField[j][i].setIcon( sea );
								
				c.gridx      = i;
				c.gridy      = j;
				c.gridwidth  = 1;
				c.gridheight = 1;

				myPanel.add( myField[j][i], c );
				
				c.gridx      = i + 10;
				c.gridy      = j;
				c.gridwidth  = 1;
				c.gridheight = 1;
				
				enemyPanel.add( enemyField[j][i], c );	
			}
		}
		XOD	= new JLabel("���� ��� �� ��������...");
		XOD.setForeground( Color.RED );
		Font font = new Font("Verdana", Font.BOLD, 24);
		XOD.setFont( font );
		XOD.setVisible( true );
		
		enemyPanel.add( XOD );
	}
    
    public boolean FindXY( MouseEvent arg0 )
	{
		int tempX = -1;
		int tempY = -1;
		displayX  = arg0.getX();
		X         = -1;
		displayY  = arg0.getY();
		Y         = -1;
		
		tempX = displayX - 20;
		tempY = displayY - 26;
		
		if( displayX < 315 && displayX > 20 && displayY < 321 && displayY > 26 )
		{
			X = tempX / 30;
			Y = tempY / 30;
			XY = X + 10 * Y;
			//label.setText("x= "+x+" y= "+y+"X= "+X+"Y = "+Y+" XY="+XY);
			return true;
		}
		return false;
	}
    
	public boolean CheckDead()
	{
		int ind = myField[X][Y].indexOfShip;
		int n   = ships[ind].len;
		int xx, yy;
		
		for( int i = 0; i < n; i++ )
		{
			xx = ships[ind].arrOfXY[ 2*i ];
			yy = ships[ind].arrOfXY[ 2*i + 1 ];
			if( !myField[xx][yy].IsFired )
				return false;
		}
		return true;
	}
	
	public void SetCurrLen( int currInd )
	{
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
	}
	
	public void markShips()
	{
		currIndex = 0;
		SetCurrLen( currIndex );
		CreateShip( currLen, currIndex );	
	}
	
	public void CreateShip( int len, int ind )
	{
		ships[ind] = new Ship(len);
	}
	
	public boolean IsLine()
	{
		Ship sh    = ships[ currIndex ];
		Integer Xx = sh.arrOfXY[0];
		Integer Yy = sh.arrOfXY[1];
		
		lX = true;
		lY = true;
		
		Integer tempX = 0, xMIN = Xx, xMAX = Xx;
		Integer tempY = 0, yMIN = Yy, yMAX = Yy;
		
		for( int i = 1; i < currLen; i++ )
		{	
			tempX = sh.arrOfXY[ 2*i ];
			tempY = sh.arrOfXY[ 2*i + 1 ];
			
			if( !Xx.equals( tempX ) ) 
				lX=false;
			
			if( !Yy.equals( tempY ) )  
				lY=false;
			
			if( tempX < xMIN ) xMIN = tempX;
			if( tempY < yMIN ) yMIN = tempY;
			if( tempX > xMAX ) xMAX = tempX;
			if( tempY > yMAX ) yMAX = tempY;
		}
		
		if( lX )
			decks.setText("� ���� ����� �� Y");
		else 
			if( lY )
				decks.setText("� ���� ����� �� X");
		
		if( !lX && !lY )
		{
			decks.setText("�� � ���� �����!");
			return false;
		}
		else // IsSequence
		{
			if ( (lX && (( yMAX - yMIN ) == currLen-1)) || (lY && (( xMAX - xMIN ) == currLen-1 )) )
				decks.setText("������� ��� ��������!");
			else
			{
				decks.setText("�� ������");
				return false;
			}	
		}
		return true;
	}
	
	public boolean CheckShip()
	{
		if( currLen != indexInShip )
		{
			decks.setText("���� �����!");
			return false;
		}
		
		if ( !IsLine() )
			return false;
		
		return true;			
	}
	
}
