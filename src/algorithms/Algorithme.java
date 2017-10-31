package algorithms;

abstract public class Algorithme<Type1, Type2>
{
	
	Probleme<Type1, Type2> p;//Le problème à résoudre avec l’algorithme 
	
	/*Méthode abstraite, résout le problème p.*/
	abstract public void solve();
	
	/*Méthode abstraite, résout le problème passé en paramètre.*/
	abstract public void solve(Probleme<Type1, Type2> p);
}
