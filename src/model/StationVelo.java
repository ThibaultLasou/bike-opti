package model;

import ilog.concert.*;
import ilog.cplex.*;

public class StationVelo 
{
	// Parametres deterministes
	public int pbID;
	public int c; // cout acquisition
	public int v; // cout manque de velo
	public int w; // cout pas de place
	public int k; // capacite
	//Parametres stochastiques
	public int[] demande; // demande stochastique 
	
	// Variables de premier niveau
	public IloIntVar x; // nb vélos
	// Variables de deuxieme niveau
	public IloIntVar[] B; // nb vélos loués
	public IloNumExpr Iplus;
	public IloNumExpr[] Imoins;
	public IloNumExpr Oplus;
	public IloNumExpr Omoins;
	
	public StationVelo(int id, int cc, int cv, int cw, int ck, int[] cdemande, IloCplex modele)
	{
		this.pbID = id;
		this.c = cc;
		this.v = cv;
		this.w = cw;
		this.k = ck;
		this.demande = cdemande;
		
		try {
			this.x = modele.intVar(0, k);
			this.B = modele.intVarArray(this.demande.length, 0, Integer.MAX_VALUE);
			
			IloNumExpr res = modele.numExpr();
			for(IloIntVar i : B)
			{
				res = modele.sum(res, i);
			}
			res = modele.diff(x, res);
			this.Iplus = modele.max(0, res);
			
			for(int j=0;j<this.demande.length;j++)
			{
				this.Imoins[j] = modele.diff(B[j], x);
				this.Imoins[j] = modele.max(0, this.Imoins[j]);
			}
		}
		catch (IloException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}