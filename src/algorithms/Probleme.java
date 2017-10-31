package algorithms;

import java.util.ArrayList;

abstract public class Probleme<Type1, Type2>
{
	protected ArrayList<Scenario> scenarios;
	protected boolean minimize; // true : minimisation, false : maximisation
	
	final public double fonctionObj()
	{
		return fonctionObj(getVarPremNiv());
	}
	
	abstract public double fonctionObj(ArrayList<Type1> vars1); // Evalue la fonction objectif avec les valeurs des variables passées en paramètres
	abstract public boolean constraints(ArrayList<Type1> vars1); // Determine si les valeurs des variables passées en paramètres respectent les contraintes
	abstract public void voisinage(ArrayList<Type1> vars1); // retourne des valeurs des variables principales selon le voisinage choisi pour le problème
	abstract public ArrayList<Type1> getVarPremNiv();
	abstract public void setVarPremNiv(ArrayList<Type1> vars);
	abstract public Probleme<Type1, Type2> clone();
}