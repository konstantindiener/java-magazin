package de.coinor.groovydsl

class MietFassade {

	def monatlicheMiete
	
	MietPruefer für(def groesse) {
		new MietPruefer(monatlicheMiete: monatlicheMiete, groesse: groesse)
	}
}
