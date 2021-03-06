package vls;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class StationVelo {

	public static final int INDICE_COUT_C = 0;
	public static final int INDICE_COUT_V = 1;
	public static final int INDICE_COUT_W = 2;

	private int number;
	private String nom;
	private int bikeStands;
	private String address;
	private int availableBikes;
	private int availableBikeStands;
	private Position position;

	// Parametres deterministes
	public int pbID;
	public int c; // cout acquisition
	public int v; // cout manque de velo
	public int w; // cout pas de place
	public int k; // capacite
	//Parametres stochastiques
	public ArrayList<Integer> demande = new ArrayList<>(); // demande stochastique

	public static HashMap<Integer, StationVelo> lienNumberStation = new HashMap<>();
	public static HashMap<Integer, StationVelo> lienIdStation = new HashMap<>();
	
	public int x;
	public ArrayList<Integer> B = new ArrayList<>();;

	public StationVelo() {
	}

	public StationVelo(int number, int bikeStands, String address, Position position, int availableBikes) {
		this();
		this.number = number;
		this.bikeStands = bikeStands;
		this.address = address;
		this.position = position;
		this.availableBikes = availableBikes;
		this.k = this.bikeStands;
		
	}

	public StationVelo(String nom, int number, int bikeStands, String address, Position position, int availableBikes) {
		this(number, bikeStands, address, position, availableBikes);
		this.nom = nom;
	}
	
	public StationVelo(String nom, int number, int bikeStands, String address, Position position, int availableBikes, int availableBikeStands) {
		this(nom, number, bikeStands, address, position, availableBikes);
		this.availableBikeStands = availableBikeStands;
	}

	public StationVelo(int number, int bikeStands, String address, Position position, int availableBikes, int availableBikeStands) {
		this(number, bikeStands, address, position, availableBikes);
		this.availableBikeStands = availableBikeStands;
	}
	
	@Override
	protected StationVelo clone() {
		StationVelo s = new StationVelo(this.number, this.bikeStands, this.address, this.position, this.availableBikes, this.availableBikeStands);
		s.setC(this.c);
		s.setK(this.k);
		s.setV(this.v);
		s.setW(this.w);
		s.demande = (ArrayList<Integer>) this.demande.clone();
		s.x = this.x;
		s.B = (ArrayList<Integer>) this.B.clone();
		return s;
	}
	
	public int getImoins_J(int j, int x)
	{
		return Math.max(B.get(j)-x,0);
	}
	
	public int getImoins_J(int j)
	{
		return getImoins_J(j, x);
	}
	
	public int getIplus()
	{
		return getIplus(x);
	}
	
	public int getIplus(int x)
	{
		return Math.max(x-B.stream().mapToInt(Integer::intValue).sum(),0);
	}
	
	public int getOmoins(int Bj)
	{
		return getOmoins(x, Bj);
	}

	public int getOmoins(int x, int Bj)
	{	
		return Math.max(Bj - k + x - B.stream().mapToInt(Integer::intValue).sum(),0);
	}
	
	public int getOplus(int Bj)
	{
		return getOplus(x, Bj);	
	}
	
	public int getOplus(int x, int Bj)
	{
		return Math.max(k - x + B.stream().mapToInt(Integer::intValue).sum() - Bj, 0);	
	}

	public int getAvailableBikeStands() {
		return availableBikeStands;
	}

	public int getC() {
		return c;
	}

	public int getV() {
		return v;
	}

	public int getW() {
		return w;
	}

	public int getK() {
		return k;
	}

	public ArrayList<Integer> getDemande() {
		return demande;
	}

	public int getX() {
		return x;
	}

	public ArrayList<Integer> getB() {
		return B;
	}

	public String getNom() {
		return nom;
	}

	public int getNumber() {
		return number;
	}

	public int getBikeStands() {
		return bikeStands;
	}

	public String getAddress() {
		return address;
	}

	public int getAvailableBikes() {
		return availableBikes;
	}

	public Position getPosition() {
		return position;
	}

	public void setC(int c) {
		this.c = c;
	}

	public void setV(int v) {
		this.v = v;
	}

	public void setW(int w) {
		this.w = w;
	}

	public void setK(int k) {
		this.k = k;
	}

	public void setParamPremierNiveau(ParamPremierNiveau var, int cvwk){
		switch(var.indice){
			case INDICE_COUT_C: setC(cvwk); break;
			case INDICE_COUT_V: setV(cvwk); break;
			case INDICE_COUT_W: setW(cvwk); break;
		}
	}

	public enum ParamPremierNiveau {
		varC("c", INDICE_COUT_C),
		varV("v", INDICE_COUT_V),
		varW("w", INDICE_COUT_W);

		public String nom;
		public int indice;

		ParamPremierNiveau(String nom, int indice) {
			this.nom = nom;
			this.indice = indice;
		}

	}

	public ArrayList<String> genererPositionsDemandesStochastiques() {
		ArrayList<String> stationsArrives = new ArrayList<>();
		for(int i = 0; i < B.size(); i++) {
			try {
				if(B.get(i) != 0)
				{
					stationsArrives.add("\"" + lienIdStation.get(i).getNom() + "\"");
				}
			} catch (Exception e) {

			}
		}
		return stationsArrives;
	}

	@SuppressWarnings("serial")
	public static class Position extends Point 
	{
		private double lng;
		private double lat;

		public Position(double lng, double lat) {
			this.lng = lng;
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public double getLat() {
			return lat;
		}

		@Override
		public String toString() {
			return "{lat:" + lat + ",lng:" + lng + "}";
		}
	}
}