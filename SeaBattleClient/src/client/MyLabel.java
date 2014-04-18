package client;
import java.io.Serializable;

import javax.swing.JLabel;

public class MyLabel extends JLabel implements Serializable
{
	//private static final long serialVersionUID = 1L;
	//int x           = -1;    // координаты label в системе координат 1..10 х 1..10
	//int y           = -1; 	 // координаты label в системе координат 1..10 х 1..10
	boolean IsShip  = false; // false - Sea 		true - Ship
	int	indexOfShip = -1;    // индекс корабля в массиве кораблей
	boolean IsFired = false; // false - не пуляли, true - пуляли
	boolean mark    = false; // не размечен
	boolean ambit   = false; // является ли окрестностью корабля?
	
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
