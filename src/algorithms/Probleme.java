package algorithms;

import java.util.ArrayList;

abstract public class Probleme<Type1, Type2>
{
	protected ArrayList<? extends Scenario> scenarios; /*Liste des scenarios*/
	protected boolean minimize; /* Booléen valant “true”  si le problème est une minimisation, “false” si c’est une maximisation*/
	
	final public double fonctionObj()
	{
		return fonctionObj(getVarPremNiv());
	}
	/*Evalue la fonction objectif avec les valeurs des variables passées en paramètres*/
	abstract public double fonctionObj(ArrayList<Type1> vars1);
	
	/* Determine si les valeurs des variables passées en paramètres respectent les contraintes*/
	abstract public boolean constraints(ArrayList<Type1> vars1); 
	
	/*Retourne des valeurs des variables principales selon le voisinage choisi pour le problème*/
	abstract public void voisinage(ArrayList<Type1> vars1); 
	
	/*Retourne la liste des valeurs des variables de premier niveau.*/
	abstract public ArrayList<Type1> getVarPremNiv();
	
	/*Affecte la liste passée en paramètre aux variables de premier niveau.*/
	abstract public void setVarPremNiv(ArrayList<Type1> vars);
	
	/*Retourne un clone du problème.*/
	abstract public Probleme<Type1, Type2> clone();
}