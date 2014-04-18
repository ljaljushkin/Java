package server;
import java.io.Serializable;

import javax.swing.JLabel;

public class MyLabel extends JLabel
{
	//private static final long serialVersionUID = 1L;
	//int x           = -1;    // координаты label в системе координат 1..10 х 1..10
	//int y           = -1; 	 // координаты label в системе координат 1..10 х 1..10
	public boolean IsShip  = false; // false - Sea 		true - Ship
	public int	indexOfShip = -1;    // индекс корабля в массиве кораблей
	public boolean IsFired = false; // false - не пуляли, true - пуляли
	public boolean mark    = false; // не размечен
	public boolean ambit   = false; // является ли окрестностью корабля?
	
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
