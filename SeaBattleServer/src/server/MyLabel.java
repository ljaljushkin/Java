package server;
import java.io.Serializable;

import javax.swing.JLabel;

public class MyLabel extends JLabel
{
	//private static final long serialVersionUID = 1L;
	//int x           = -1;    // ���������� label � ������� ��������� 1..10 � 1..10
	//int y           = -1; 	 // ���������� label � ������� ��������� 1..10 � 1..10
	public boolean IsShip  = false; // false - Sea 		true - Ship
	public int	indexOfShip = -1;    // ������ ������� � ������� ��������
	public boolean IsFired = false; // false - �� ������, true - ������
	public boolean mark    = false; // �� ��������
	public boolean ambit   = false; // �������� �� ������������ �������?
	
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
