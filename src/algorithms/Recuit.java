package algorithms;

import java.util.ArrayList;

public class Recuit extends Algorithme
{
	private Probleme p;
	private int nbPaliers;
	private int nbIters;
	private int temperatureInit;
	private int temperature;
	private float reducTemp;
	private int minimize;
	

	public Recuit(Probleme p, int nbPaliers, int nbIters, int reducTemp) 
	{
		this.p = p;
		this.nbPaliers = nbPaliers;
		this.nbIters = nbPaliers;
		this.reducTemp = reducTemp;
		if(p.minimize)
		{
			minimize = 1;
		}
		else
		{
			minimize = -1;
		}
		initTemp();
	}
	
	private void initTemp()
	{
		//TODO
		temperatureInit = 00000;
	}
	
	@Override
	public void solve()
	{
		solve(this.p);
	}
	
	
	public void solve(Probleme p)
	{
		int valObj;
		int valObjIter;
		ArrayList<Integer> varIter;
		temperature = temperatureInit;
		
		for(int i=0;i<nbPaliers;i ++)
		{
			for(int j=0;j<nbIters;j++)
			{
				valObj = p.fonctionObj(); 
				varIter = p.voisinage();
				valObjIter = p.fonctionObj(varIter);
				if(valObjIter*minimize <= valObj*minimize)
				{
					p.varPremierNiv = varIter;
				}
				else
				{
					if(Math.exp(-valObjIter/temperature) >= Math.random())
					{
						p.varPremierNiv = varIter;
					}
				}
			}
			temperature = (int) (temperature*reducTemp);
		}
	}
}
