package client;
import java.io.Serializable;

import javax.swing.JLabel;

public class MyLabel extends JLabel implements Serializable
{
	//private static final long serialVersionUID = 1L;
	//int x           = -1;    // ���������� label � ������� ��������� 1..10 � 1..10
	//int y           = -1; 	 // ���������� label � ������� ��������� 1..10 � 1..10
	boolean IsShip  = false; // false - Sea 		true - Ship
	int	indexOfShip = -1;    // ������ ������� � ������� ��������
	boolean IsFired = false; // false - �� ������, true - ������
	boolean mark    = false; // �� ��������
	boolean ambit   = false; // �������� �� ������������ �������?
	
	public MyLabel()
	{
		super();
		
		IsShip  = false; 
		indexOfShip = -1;    
		IsFired = false; 
		mark    = false; 
		ambit   = false;
	}
	public boolean isAvailable()
	{
		return (!IsShip && !ambit);
	}
}
