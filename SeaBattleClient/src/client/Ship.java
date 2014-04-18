package client;

import java.io.Serializable;

public class Ship implements Serializable
{
	int len;
	int[] arrOfXY;
	
	Ship(int l)
	{ 
		len=l;
		arrOfXY = new int[ len * 2 ]; 
	}
	
	boolean Del( int X, int Y, int currPos )
	{
		boolean temp = false;
		
		for ( int i = 0; i < len; i++ )
		{
			if ( ( arrOfXY[ 2*i ] == X) && ( arrOfXY[ 2*i + 1 ] == Y ) )
			{
				temp = true;
				
				if( currPos != len )
				{
					arrOfXY[ 2*i ]    = arrOfXY[ currPos*2 ];
					arrOfXY[ 2*i + 1] = arrOfXY[ currPos*2 + 1 ];
				}
				else
				{
					arrOfXY[ 2*i ]     = arrOfXY[ ( currPos - 1 ) * 2 ];
					arrOfXY[ 2*i + 1 ] = arrOfXY[ ( currPos-1 ) * 2 + 1 ];
				}
				return temp;
			}
		}
		return temp;	
	}
}
