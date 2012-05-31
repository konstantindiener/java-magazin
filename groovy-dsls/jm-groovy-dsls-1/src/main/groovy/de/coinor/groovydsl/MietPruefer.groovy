package de.coinor.groovydsl

class MietPruefer {

	def monatlicheMiete
	
	def groesse
	
	boolean angemessen() {
		(monatlicheMiete / groesse) < 10
	}
	
	def getProperty(String name) {
		if (name == "angemessen") {
			angemessen()
		}
		else {
			this.@"$name"
		}
	}	
}
