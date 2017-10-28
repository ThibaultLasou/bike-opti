package algorithms;

import java.util.ArrayList;

abstract public class Probleme<Type1, Type2>
{
	protected boolean minimize; // true : minimisation, false : maximisation
	
	final public int fonctionObj()
	{
		return fonctionObj(getVarPremNiv(), getVarDeuxNiv());
	}
	
	abstract public int fonctionObj(ArrayList<Type1> vars1, ArrayList<Type2> vars2); // Evalue la fonction objectif avec les valeurs des variables passées en paramètres
	abstract public boolean constraints(ArrayList<Type1> vars, ArrayList<Type2> vars2); // Determine si les valeurs des variables passées en paramètres respectent les contraintes
	abstract public void voisinage(ArrayList<Type1> vars1, ArrayList<Type2> vars2); // retourne des valeurs des variables principales selon le voisinage choisi pour le problème
	abstract public ArrayList<Type1> getVarPremNiv();
	abstract public void setVarPremNiv(ArrayList<Type1> vars);
	abstract public ArrayList<Type2> getVarDeuxNiv();
	abstract public void setVarDeuxNiv(ArrayList<Type2> vars);
}