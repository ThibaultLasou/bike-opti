package algorithms;

import java.util.ArrayList;
import java.util.List;

public class SAA<Type1 extends Number, Type2> extends Algorithme<Type1, Type2>{

	private int nbEchantillons;
	private Algorithme <Type1, Type2> algo;
	private ArrayList<Probleme<Type1,Type2>> echantillon;
	private ArrayList<Type1> solutionMoyenne;
	private float valObjMoyenne;
	private Probleme<Type1,Type2> echantillonNprime;
	private ArrayList<Type1> solutionSAA;
	private double valObjSAA;
	
	public SAA(int nbEchantillons, Algorithme<Type1, Type2> algo)
	{
		this.nbEchantillons = nbEchantillons;
		this.algo = algo;
		solutionMoyenne = new ArrayList<>();
		echantillon = new ArrayList<>();
		solutionSAA = new ArrayList<>();
		this.p = this.algo.p;
		this.echantillonNprime = p.clone();
		/* Initialization de l'echantillons Nprime */
		ArrayList<Scenario> scenario = new ArrayList<>();
		scenario = (ArrayList<Scenario>) algo.p.scenarios.subList(algo.p.scenarios.size()/2,algo.p.scenarios.size()-1);
		echantillonNprime.scenarios = scenario;

		/* Initialisation des echantillons */
		int tailleListe = (algo.p.scenarios.size()/2)/nbEchantillons; // taille de la liste des scenarios des echantillons
		for(int i=0; i<nbEchantillons-1;i++)
		{
			Probleme<Type1,Type2> pb = algo.p.clone();
			ArrayList<Scenario> MyScenario = new ArrayList<>();
			MyScenario = (ArrayList<Scenario>) algo.p.scenarios.subList(i*tailleListe,i*tailleListe+tailleListe-1);
			pb.scenarios = MyScenario;
			echantillon.add(pb);
		}
		
		/* Traitement du dernier element de la liste des scenarios */
		Probleme<Type1,Type2> pb = algo.p.clone();
		ArrayList<Scenario> LastScenario = new ArrayList<>();
		LastScenario = (ArrayList<Scenario>) algo.p.scenarios.subList(nbEchantillons*tailleListe,nbEchantillons*tailleListe+tailleListe%nbEchantillons);
		pb.scenarios = LastScenario ;
		echantillon.add(pb);
		
	}
	
	/* Calcul de la moyenne des solutions optimales de la moyenne des valeurs objectif*/
	public void calculMoyennes()
	{
		/* Lancer le recuit sur tous les echantillons*/
		for (int i=0;i<this.nbEchantillons;i++)
		{
			this.algo.solve(echantillon.get(i));
		}
		this.solutionMoyenne = new ArrayList<>();
		Number sum=0,n;
		this.valObjMoyenne =0;
		for(int i=0 ;i<this.nbEchantillons; i++)
		{
			/* Faire la moyenne des valeurs objectif*/
			valObjMoyenne += this.echantillon.get(i).fonctionObj();
			
			/* faire la moyenne des solutions optimales*/
			for(int j = 0;j<this.echantillon.get(0).getVarPremNiv().size();j++)
			{
					sum = sum.floatValue()+ this.echantillon.get(i).getVarPremNiv().get(j).floatValue();
			}
			n=sum.floatValue()/nbEchantillons;
			this.solutionMoyenne.add((Type1) n);
		}
		
		this.valObjMoyenne=this.valObjMoyenne/this.nbEchantillons;
	}
	@Override
	public void solve() 
	{
		solve(this.echantillonNprime);
	}

	@Override
	public void solve(Probleme<Type1, Type2> p) {
		calculMoyennes();
		double min  = Double.MAX_VALUE;
		for(int i=0;i<this.nbEchantillons;i++)
		{
			this.valObjSAA = this.p.fonctionObj(this.echantillon.get(i).getVarPremNiv());
			if(this.valObjSAA <= min)
			{
				min= this.valObjSAA;
				this.solutionSAA = this.echantillon.get(i).getVarPremNiv();
			}
		}
	}
}