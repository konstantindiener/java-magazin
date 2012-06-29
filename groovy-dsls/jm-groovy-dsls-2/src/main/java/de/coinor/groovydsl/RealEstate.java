package de.coinor.groovydsl;

public class RealEstate {

	boolean ownerOccupied;
	
	AmountWithCurrency monthlyRent;
	
	double sizeInSquareMeters;

	public RealEstate(boolean ownerOccupied, AmountWithCurrency monthlyRent,
			double sizeInSquareMeters) {
		super();
		this.ownerOccupied = ownerOccupied;
		this.monthlyRent = monthlyRent;
		this.sizeInSquareMeters = sizeInSquareMeters;
	}
}
