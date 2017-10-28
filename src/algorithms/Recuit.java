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
	
	private int nbAcc;
	private int bestCost;
	private ArrayList<Type1> best1;
	private ArrayList<Type2> best2;
	
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
		best1 = new ArrayList<>();
		best2 = new ArrayList<>();
		initTemp();
	}
	
	private void initTemp()
	{
		temperatureInit = 5;
		Probleme<Type1, Type2> p2 = p.clone();
		do 
		{
			temperatureInit *= 2;
			solve(p2);
		}while(nbAcc/(float)(nbPaliers*nbIters) < 0.8);
		
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
		nbAcc = 0;
		bestCost = p.fonctionObj();
		
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
				if(!(valObjIter*minimize <= valObj*minimize))
				{
					double p1 = Math.exp(-(valObjIter-valObj)/(double) temperature);
					double p2 = Math.random();
					System.out.print("("+p1+", "+p2+") ");
					if(!(p1 >= p2))
					{
						System.out.print("Refusée");
						System.out.println("|" + temperature);
						continue;
					}
				}
				p.setVarPremNiv(varPremIter);
				p.setVarDeuxNiv(varDeuxIter);
				if(valObjIter<bestCost)
				{
					bestCost = valObjIter;
					best1.clear();
					best2.clear();
					for(Type1 t1 : varPremIter)
					{
						best1.add(t1);
					}
					for(Type2 t2 : varDeuxIter)
					{
						best2.add(t2);
					}
				}
				System.out.print("Acceptée");
				System.out.println("|" + temperature);
				nbAcc++;
			}
			temperature = (int) (temperature*reducTemp);
		}
	}
}
