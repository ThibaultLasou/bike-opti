/*********************************************************************
***************************    PARAMETRES      **********************
*********************************************************************/

/*****DETERMINISTES*******/
 float ps=1;	//probabilit� qu'un scenario se produise

 float ca=5;	//Co�t d'acquisition d'un v�lo � la station i
 float cb=8;
 float cc=2;
 
 float va=8;	//co�t du manque d'un v�lo � la station i
 float vb=3;
 float vc=13;
 
 float wa=16;	//co�t du temps perdu par un utilisatuer lorsqu'il n'y a pas de v�lo
 float wb=2;
 float wc=8;
 
 int ka=15;	//capacit� de v�los de la station 
 int kb=15;
 int kc=15;
 
/*******STOCHASTIQUES******/
 int Eab=2;	//demande de v�lo de i � j
 int Eac=2;
 int Eba=8;
 int Ebc=2;
 int Eca=2;
 int Ecb=8;
 
 /**********************************************************************************
 ***********************************  VARIABLES   **********************************
 ***********************************************************************************/ 

/****PREMIER NIVEAU*****/
 dvar int+ xa;  //nombre de v�lo � affecter � la station i
 dvar int+ xb;
 dvar int+ xc;
 
/****SECOND NIVEAU****/
 dvar int+ Bab;	//nombre de v�lo lou�s pour aller de i � j
 dvar int+ Bac;
 dvar int+ Bba;
 dvar int+ Bbc;
 dvar int+ Bca;
 dvar int+ Bcb;
 
 dvar int+ IaP;	//surplus de v�los dans la station i
 dvar int+ IbP;
 dvar int+ IcP;
 
 dvar int+ IabM;	// d�ficit de v�lo dans i pour chaque parcours entre i et j
 dvar int+ IacM;
 dvar int+ IbaM;
 dvar int+ IbcM;
 dvar int+ IcaM;
 dvar int+ IcbM;
 
 dvar int+ OaP;	//capacit� r�siduelle de la station i 
 dvar int+ ObP;
 dvar int+ OcP;
 
 dvar int+ OaM;	//surplus de la station i
 dvar int+ ObM;
 dvar int+ OcM;
 
 
 /***********************************EXPRESSIONS********************************/
 dexpr float objective= (ca*xa + cb*xb + cc*xc) 
 						 + ps*(
							(va*IabM+va*IacM+wa*OaM)
							+(vb*IbaM+vb*IbcM+wb*ObM)
							+(vc*IcaM+vc*IcbM+wc*OcM)
							)
						;
						
 /***********************************************************************************
 **********************************   MODEL      ************************************  
 **********************************************************************************/
 minimize objective;
 
 subject to {
 	const1a : xa<=ka;
 	const1b : xb<=kb;
 	const1c : xc<=kc;
 	
 	//const2 : Bijs == Eijs-IijsM ;
 	const2ab : Bab==Eab-IabM;
 	const2ac : Bac==Eac-IacM;
 	const2ba : Bba==Eba-IbaM;
 	const2bc : Bbc==Ebc-IbcM;
 	const2ca : Bca==Eca-IcaM;
 	const2cb : Bcb==Ecb-IcbM;
 		
 	 	
 	//const3 : IisP- SOMME(IijsM) == xi - SOMME(Eijs) ;
 	const3a : IaP - (IabM+IacM) == xa - (Eab+Eac);
 	const3b : IbP - (IbaM+IbcM) == xb - (Eba+Ebc);
 	const3c : IcP - (IcaM+IcbM) == xc - (Eca+Ecb);
 	
 	
 	//const4 : OisP - OisM == ki-xi+ SOMME(Bijs) - SOMME(Eijs);
 	const4a : OaP - OaM == ka - xa + (Bab+Bac) - (Bba+Bca);
 	const4b : ObP - ObM == kb - xb + (Bba+Bbc) - (Bab+Bcb);
 	const4c : OcP - OcM == kc - xc + (Bca+Bcb) - (Bac+Bbc);
 	
 	IaP==maxl(0,xa-(Bab+Bac));
 	IbP==maxl(0,xb-(Bba+Bbc));
 	IcP==maxl(0,xc-(Bca+Bcb));

	IabM == maxl(0,Eab-Bab);	// d�ficit de v�lo dans i pour chaque parcours entre i et j
	IacM == maxl(0,Eac-Bac);
	IbaM == maxl(0,Eba-Bba);
	IbcM == maxl(0,Ebc-Bbc);
	IcaM == maxl(0,Eca-Bca);
	IcbM == maxl(0,Ecb-Bcb);
	
	OaP == maxl(0,ka-xa+(Bab+Bac)-(Bba+Bca));
	ObP == maxl(0,kb-xb+(Bba+Bbc)-(Bab+Bcb));
	OcP == maxl(0,kc-xc+(Bca+Bcb)-(Bac+Bbc));
	
	OaM == maxl(0,(Bba+Bca)-ka+xa-(Bab+Bac));
	ObM == maxl(0,(Bab+Bcb)-kb+xb-(Bba+Bbc));
	OcM == maxl(0,(Bac+Bbc)-kc+xc-(Bca+Bcb));
	
	xa+xb+xc == 30;
	
}