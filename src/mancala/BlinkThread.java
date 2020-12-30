package mancala;

import java.awt.Color;
import java.util.TimerTask;

public class BlinkThread extends TimerTask
{
	int selector = 0;

	public BlinkThread(int selector)
	{
		this.selector = selector;
	}

	@Override
	public void run()
	{
		if (selector == 0)
		{

			if (Core.arrowDown.getForeground() != Color.white)
			{
				Core.arrowDown.setForeground(Color.white);
				Core.arrowUp.setForeground(Color.white);
			} else
			{
				Core.arrowDown.setForeground(Color.BLACK);
				Core.arrowUp.setForeground(Color.BLACK);

			}
		}
		if (selector == 1)
		{
//			System.out.println("blink down");
			if (Core.turnInd11.getForeground() != Color.white)
			{
				Core.turnInd11.setForeground(Color.white);
				Core.turnInd12.setForeground(Color.white);
				Core.turnInd13.setForeground(Color.white);
				Core.turnInd14.setForeground(Color.white);
				Core.turnInd1.setForeground(Color.white);
				Core.turnInd2.setForeground(Color.white);
				Core.turnInd3.setForeground(Color.white);
				Core.turnInd4.setForeground(Color.white);
			} else
			{
				Core.turnInd11.setForeground(Color.black);
				Core.turnInd12.setForeground(Color.black);
				Core.turnInd13.setForeground(Color.black);
				Core.turnInd14.setForeground(Color.black);
				Core.turnInd1.setForeground(Color.white);
				Core.turnInd2.setForeground(Color.white);
				Core.turnInd3.setForeground(Color.white);
				Core.turnInd4.setForeground(Color.white);
			}

		}
		if (selector == 2)
		{
//			System.out.println("blink up");
			if (Core.turnInd1.getForeground() != Color.white)
			{
				Core.turnInd11.setForeground(Color.white);
				Core.turnInd12.setForeground(Color.white);
				Core.turnInd13.setForeground(Color.white);
				Core.turnInd14.setForeground(Color.white);
				Core.turnInd1.setForeground(Color.white);
				Core.turnInd2.setForeground(Color.white);
				Core.turnInd3.setForeground(Color.white);
				Core.turnInd4.setForeground(Color.white);
			} else
			{
				Core.turnInd11.setForeground(Color.white);
				Core.turnInd12.setForeground(Color.white);
				Core.turnInd13.setForeground(Color.white);
				Core.turnInd14.setForeground(Color.white);
				Core.turnInd1.setForeground(Color.black);
				Core.turnInd2.setForeground(Color.black);
				Core.turnInd3.setForeground(Color.black);
				Core.turnInd4.setForeground(Color.black);
			}
		}
	}

}
