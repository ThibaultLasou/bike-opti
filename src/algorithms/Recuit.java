package algorithms;

import java.util.ArrayList;

public class Recuit<Type1, Type2> extends Algorithme<Type1, Type2>
{
	private Probleme<Type1, Type2> p;
	private int nbPaliers;
	private int nbIters;
	private int temperatureInit;
	private float reducTemp;
	private int minimize;
	

	public Recuit(Probleme<Type1, Type2> p, int nbPaliers, int nbIters, int reducTemp) 
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
	
	
	public void solve(Probleme<Type1, Type2> p)
	{
		int valObj;
		int valObjIter;
		int temperature;
		ArrayList<Type1> varPremIter = new ArrayList<>();
		ArrayList<Type2> varDeuxIter = new ArrayList<>();
		temperature = temperatureInit;
		
		for(int i=0;i<nbPaliers;i ++)
		{
			for(int j=0;j<nbIters;j++)
			{
				valObj = p.fonctionObj(); 
				p.voisinage(varPremIter, varDeuxIter);
				valObjIter = p.fonctionObj(varPremIter, varDeuxIter);
				if(valObjIter*minimize <= valObj*minimize)
				{
					p.setVarPremNiv(varPremIter);
					p.setVarDeuxNiv(varDeuxIter);
				}
				else
				{
					if(Math.exp(-valObjIter/temperature) >= Math.random())
					{
						p.setVarPremNiv(varPremIter);
						p.setVarDeuxNiv(varDeuxIter);
					}
				}
			}
			temperature = (int) (temperature*reducTemp);
		}
	}
}
