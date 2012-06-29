package de.coinor.groovydsl.dslifiers

import java.security.KeyPairGenerator.Delegate;

import de.coinor.groovydsl.AmountWithCurrency;

class AmountWithCurrencyDslifier {

	public void dslify() {
		AmountWithCurrency.metaClass {
			
			plus { AmountWithCurrency operand2 ->
				if (delegate.currency == operand2.currency) {
					new AmountWithCurrency(delegate.amount + operand2.amount, delegate.currency)
				}
			}

			minus { AmountWithCurrency operand2 ->
				if (delegate.currency == operand2.currency) {
					new AmountWithCurrency(delegate.amount - operand2.amount, delegate.currency)
				}
			}

			div { Number operand2 ->
				new AmountWithCurrency(delegate.amount / operand2, delegate.currency)
			}
		}
	}
}
