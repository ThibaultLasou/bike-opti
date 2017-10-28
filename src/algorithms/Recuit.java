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

	public Recuit(Probleme<Type1, Type2> p, int nbPaliers, int nbIters, float reducTemp) 
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
		temperatureInit = 5;
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
		ArrayList<Type1> varPremIter = p.getVarPremNiv();
		ArrayList<Type2> varDeuxIter = p.getVarDeuxNiv();
		temperature = temperatureInit;
		
		for(int i=0;i<nbPaliers;i ++)
		{
			System.out.println("Palier "+i);
			for(int j=0;j<nbIters;j++)
			{
				System.out.print(j + "|");
				valObj = p.fonctionObj();
				System.out.print(valObj + "|");
				p.voisinage(varPremIter, varDeuxIter);
				System.out.print(varPremIter+ "|");
				valObjIter = p.fonctionObj(varPremIter, varDeuxIter);
				System.out.print(valObjIter + "|");
				if(valObjIter*minimize <= valObj*minimize)
				{
					p.setVarPremNiv(varPremIter);
					p.setVarDeuxNiv(varDeuxIter);
					System.out.print("Acceptée");
					System.out.println("|" + temperature);
					continue;
				}
				else
				{
					double p1 = Math.exp(-(valObjIter-valObj)/(double) temperature);
					double p2 = Math.random();
					System.out.print("("+p1+", "+p2+") ");
					if(p1 >= p2)
					{
						p.setVarPremNiv(varPremIter);
						p.setVarDeuxNiv(varDeuxIter);
						System.out.print("Acceptée");
						System.out.println("|" + temperature);
						continue;
					}
					System.out.print("Refusée");
				}
				System.out.println("|" + temperature);
			}
			temperature = (int) (temperature*reducTemp);
		}
	}
}
