package algorithms;

import java.util.ArrayList;

abstract public class Probleme
{
	protected ArrayList<Integer> varPremierNiv;
	protected boolean minimize; // true : minimisation, false : maximisation
	
	final public int fonctionObj()
	{
		return fonctionObj(varPremierNiv);
	}
	
	abstract public int fonctionObj(ArrayList<Integer> vars); // Evalue la fonction objectif avec les valeurs des variables passées en paramètres
	abstract public boolean constraints(ArrayList<Integer> vars); // Determine si les valeurs des variables passées en paramètres respectent les contraintes
	abstract public ArrayList<Integer> voisinage(); // retourne des valeurs des variables principales selon le voisinage choisi pour le problème
}