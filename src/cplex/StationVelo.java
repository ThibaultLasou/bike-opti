package cplex;

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
			
			this.Iplus = modele.max(0, modele.diff(x, modele.sum(B)));
			this.Imoins = new IloNumExpr[this.demande.length];
			for(int j=0;j<this.demande.length;j++)
			{
				this.Imoins[j] = modele.diff(B[j], x);
				this.Imoins[j] = modele.max(0, this.Imoins[j]);
			}
			System.out.println(Iplus.toString());
			for(int j=0;j<this.demande.length;j++)
			{
				System.out.println(Imoins[j].toString());
			}
		}
		catch (IloException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setOmoins(IloCplex modele, IloNumExpr Bj) throws IloException
	{
		this.Omoins = modele.diff(this.k, this.x);
		this.Omoins = modele.sum(this.Omoins, modele.sum(B));
		this.Omoins = modele.diff(this.Omoins, Bj);
		this.Omoins = modele.max(0, this.Omoins);
		System.out.println(Omoins.toString());
	}
	
	public void setOplus(IloCplex modele, IloNumExpr Bj) throws IloException
	{
		this.Oplus = modele.sum(this.k, this.x);
		this.Oplus = modele.diff(Bj, this.Oplus);
		this.Oplus = modele.diff(this.Oplus, modele.sum(B));
		this.Oplus = modele.max(0, this.Oplus);
		System.out.println(Oplus.toString());
	}
}