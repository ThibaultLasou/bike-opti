package vls;

import java.awt.*;
import java.util.stream.IntStream;

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
	public int[] demande; // demande stochastique
	
	public int x;
	public int[] B;
	
	public StationVelo(int number, int bikeStands, String address, Position position, int availableBikes) {
		this.number = number;
		this.bikeStands = bikeStands;
		this.address = address;
		this.position = position;
		this.availableBikes = availableBikes;
	}
	
	public int getImoins_J(int j)
	{
		return Math.max(B[j]-x,0);
	}
	
	public int getIplus()
	{
		return Math.max(x-IntStream.of(B).sum(),0);
	}

	public int getOmoins()
	{
		//TODO
		return Math.max(x-IntStream.of(B).sum(),0);
	}
	
	public int getOplus()
	{
		//TODO
		return Math.max(x-IntStream.of(B).sum(),0);
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