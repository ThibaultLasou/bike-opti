package algorithms;

import java.util.ArrayList;
import java.util.List;

public class SAA<Type1 extends Number, TypeS extends Scenario> extends Algorithme<Type1, TypeS>{

	/*Nombre d’échantillons utilisés lors de la résolution du problème.*/
	private int nbEchantillons;
	
	/*Algorithme utilisé pour résoudre le problème pour chaque échantillon*/
	private Algorithme <Type1, TypeS> algo;
	
	/*Echantillons de l’algorithme SAA*/ 
	private ArrayList<Probleme<Type1,TypeS>> echantillon;
	
	/*Moyenne des solutions optimales des echantillons*/
	private ArrayList<Type1> solutionMoyenne;
	
	/*Moyenne des valeurs objectifs des echantillons*/
	private float valObjMoyenne;
	
	/*Echantillon N’ de l’algorithme SAA(*/
	private Probleme<Type1,TypeS> echantillonNprime;	

	/*solutions optimale trouvée par SAA*/
	private ArrayList<Type1> solutionSAA;
	
	/*valeur objectif trouvée par SAA*/
	private double valObjSAA;
	
	public SAA(int nbEchantillons, Algorithme<Type1, TypeS> algo)
	{
		this.nbEchantillons = nbEchantillons;
		this.algo = algo;
		solutionMoyenne = new ArrayList<>();
		echantillon = new ArrayList<>();
		solutionSAA = new ArrayList<>();
		this.p = this.algo.p;
		this.echantillonNprime = p.clone();
		/* Initialization de l'echantillons Nprime */
		ArrayList<TypeS> scenario = new ArrayList<>();
		for(int i = algo.p.scenarios.size()/2;i<algo.p.scenarios.size()-1;i++)
		{
			scenario.add(algo.p.scenarios.get(i));
		}
		echantillonNprime.scenarios = scenario;
		echantillonNprime.nbScenar = echantillonNprime.scenarios.size();

		/* Initialisation des echantillons */
		int tailleListe = (algo.p.scenarios.size()/2)/nbEchantillons; // taille de la liste des scenarios des echantillons
		for(int i=0; i<nbEchantillons-1;i++)
		{
			Probleme<Type1,TypeS> pb = algo.p.clone();
			ArrayList<TypeS> MyScenario = new ArrayList<>();
			for(int j = i*tailleListe;j<i*tailleListe+tailleListe-1;j++)
			{
				MyScenario.add(algo.p.scenarios.get(j));
			}
			pb.scenarios = MyScenario;
			pb.nbScenar = pb.scenarios.size();
			echantillon.add(pb);
		}
		
		/* Traitement du dernier element de la liste des scenarios */
		Probleme<Type1,TypeS> pb = algo.p.clone();
		ArrayList<TypeS> LastScenario = new ArrayList<>();
		for(int j = nbEchantillons*tailleListe;j<nbEchantillons*tailleListe+tailleListe%nbEchantillons;j++)
		{
			LastScenario.add(algo.p.scenarios.get(j));
		}
		pb.scenarios = LastScenario ;
		pb.nbScenar = pb.scenarios.size();
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
	public void solve(Probleme<Type1, TypeS> p) {
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