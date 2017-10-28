package vls;

import java.awt.*;
import java.util.ArrayList;

public class StationVelo {

	private int number;
	private int bikeStands;
	private String address;
	private int availableBikes;
	private Position position;

	// Parametres deterministes
	public int pbID;
	public int c; // cout acquisition
	public int v; // cout manque de velo
	public int w; // cout pas de place
	public int k; // capacite
	//Parametres stochastiques
	public ArrayList<Integer> demande; // demande stochastique
	
	public int x;
	public ArrayList<Integer> B;
	
	public StationVelo(int number, int bikeStands, String address, Position position, int availableBikes) {
		this.number = number;
		this.bikeStands = bikeStands;
		this.address = address;
		this.position = position;
		this.availableBikes = availableBikes;
	}
	
	public int getImoins_J(int j)
	{
		return Math.max(B.get(j)-x,0);
	}
	
	public int getIplus()
	{
		return Math.max(x-B.stream().mapToInt(Integer::intValue).sum(),0);
	}

	public int getOmoins(ArrayList<StationVelo> stations)
	{	
		int Bj=0;
		for(StationVelo s : stations)
		{
			Bj += s.B.get(this.pbID);
		}
		
		return Math.max(Bj - k + x- B.stream().mapToInt(Integer::intValue).sum(),0);
	}
	
	public int getOplus(ArrayList<StationVelo> stations)
	{
		int Bj=0;
		for (StationVelo s : stations)
		{
			Bj += s.B.get(this.pbID);
		}
		
		return Math.max(k - x + B.stream().mapToInt(Integer::intValue).sum(),0) - Bj;	
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

	@SuppressWarnings("serial")
	public static class Position extends Point {

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
	}
}