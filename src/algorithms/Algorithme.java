package algorithms;

abstract public class Algorithme<Type1, TypeS extends Scenario>
{
	Probleme<Type1, TypeS> p;//Le problème à résoudre avec l’algorithme 
	
	/*Méthode abstraite, résout le problème p.*/
	abstract public void solve();
	
	/*Méthode abstraite, résout le problème passé en paramètre.*/
	abstract public void solve(Probleme<Type1, TypeS> p);
}
