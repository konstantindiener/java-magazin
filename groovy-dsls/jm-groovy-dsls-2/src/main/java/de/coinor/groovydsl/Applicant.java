package de.coinor.groovydsl;

public class Applicant {
	
	AmountWithCurrency takeHomeAmount;
	
	RealEstate domicile;

	public Applicant(AmountWithCurrency takeHomeAmount, RealEstate domicile) {
		super();
		this.takeHomeAmount = takeHomeAmount;
		this.domicile = domicile;
	}
}	
