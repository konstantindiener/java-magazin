package de.coinor.groovydsl

class MietFassade {

	def monatlicheMiete
	
	MietPruefer f�r(def groesse) {
		new MietPruefer(monatlicheMiete: monatlicheMiete, groesse: groesse)
	}
}
