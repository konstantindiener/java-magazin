package de.coinor.groovydsl;

import java.util.Currency;

public class AmountWithCurrency implements Comparable<Object> {

	double amount;
	
	Currency currency;

	public AmountWithCurrency(double amount, Currency currency) {
		super();
		this.amount = amount;
		this.currency = currency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((currency == null) ? 0 : currency.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AmountWithCurrency other = (AmountWithCurrency) obj;
		if (Double.doubleToLongBits(amount) != Double
				.doubleToLongBits(other.amount))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		return true;
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof AmountWithCurrency) {
			AmountWithCurrency awc = (AmountWithCurrency) o;
			return Double.compare(this.amount, awc.amount);
		}
		if (o instanceof Number) {
			Number n = (Number) o;
			return Double.compare(this.amount, n.doubleValue());
		}
		return 0;
	}

	@Override
	public String toString() {
		return "AmountWithCurrency [amount=" + amount + ", currency="
				+ currency + "]";
	}
}
