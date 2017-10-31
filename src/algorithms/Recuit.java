package algorithms;

import java.util.ArrayList;

import graphic.Interface.NiveauPrecision;

public class Recuit<Type1, TypeS extends Scenario> extends Algorithme<Type1, TypeS>
{
	private int nbPaliers;//Nombre de paliers de l’algorithme
	private int nbIters;//Nombre d’itérations de l’algorithme
	private double temperatureInit;//Température initiale de l’algorithme.
	private double reducTemp;//Facteur de réduction de la température de l’algorithme
	private int minimize;/*Entier valant 1 si le problème est une minimisation, -1 si c’est une maximisation. 
							Utile pour inverser les comparaisons.*/

	private int nbAcc;//Nombre d’acceptations
	private double bestCost;//Valeur objectif de la solution optimale
	private ArrayList<Type1> best1;//Valeurs de la solution optimale
	
	public Recuit(Probleme<Type1, TypeS> p, NiveauPrecision niveauPrecision, double reducTemp) 
	{
		this.p = p;
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
		temperatureInit = -1;
	}
	
	private void initTemp()
	{
		temperatureInit = 5;
		Probleme<Type1, TypeS> p2 = p.clone();
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
	
	public void solve(Probleme<Type1, TypeS> p)
	{
		if(temperatureInit == -1)
		{
			initTemp();
		}
		double valObj;
		double valObjIter;
		double temperature;
		ArrayList<Type1> varPremIter = p.getVarPremNiv();
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
				p.voisinage(varPremIter);
				System.out.print(varPremIter+ "|");
				valObjIter = p.fonctionObj(varPremIter);
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
				if(valObjIter<bestCost)
				{
					bestCost = valObjIter;
					best1.clear();
					for(Type1 t1 : varPremIter)
					{
						best1.add(t1);
					}
				}
				System.out.print("Acceptée");
				System.out.println("|" + temperature);
				nbAcc++;
			}
			temperature = (int) (temperature*reducTemp);
		}
	}

	public void setPrecision(NiveauPrecision niveauPrecision) 
	{
		if(this.nbPaliers != niveauPrecision.nombrePaliers && this.nbIters != niveauPrecision.nombreIterations) 
		{
			this.nbPaliers = niveauPrecision.nombrePaliers;
			this.nbIters = niveauPrecision.nombreIterations;
			this.temperatureInit = -1;
		}
	}
}