package de.coinor.groovydsl

import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.Test;

class DslTestCase extends GroovyTestCase {

	@Test
	public void testNettoEinkommen() {
		def result = runDsl("nettoEinkommen.formel",
			[
				monatlichesBruttoeinkommen: 2300.0,
				Steuern: 210.0,
				Sozialabgaben: 400.0,
				sonstigeBelastungen: 160.0
				])
		
		assert result == 1530.0
	}
	
	@Test
	public void testMonatlichFreiVerfügbarerBetrag1() {
		def result = runDsl("monatlichFreiVerfuegbarerBetrag.formel", [
			Immobilie: new ImmobilieObject(
				selbstgenutzt: true,
				monatlicheMiete: 430.0),
			Antragsteller: new AntragstellerObject(
				nettoEinkommen: 1530.0,
				monatlicheMiete: 650.0),
			Kreditrate: 270.0])
		
		assert result == 1260.0
	}
	
	@Test
	public void testMonatlichFreiVerfügbarerBetrag2() {
		def result = runDsl("monatlichFreiVerfuegbarerBetrag.formel", [
			Immobilie: new ImmobilieObject(
				selbstgenutzt: false,
				monatlicheMiete: 430.0),
			Antragsteller: new AntragstellerObject(
				nettoEinkommen: 1530.0,
				monatlicheMiete: 650.0),
			Kreditrate: 270.0])
		
		assert result == 1040.0
	}
	
	@Test
	public void testMietPruefung1() {
		def result = runDsl("mietPruefung.formel", [
			Immobilie: new ImmobilieObject(
				monatlicheMiete: 430.0,
				größe: 60)])
		
		assert result
	}
	
	@Test
	public void testMietPruefung2() {
		def result = runDsl("mietPruefung.formel", [
			Immobilie: new ImmobilieObject(
				selbstgenutzt: false,
				monatlicheMiete: 680.0,
				größe: 60)])
		
		assert !result
	}
	
	@Test
	void testEinkuenfteRentner() {
		Object result = runDsl("einkuenfte.formel", [
			monatlichesBruttoeinkommen: 0.0,
			Steuern: 150.0,
			Sozialabgaben: 200.0,
			sonstigeBelastungen: 0.0,
			Beruf: "Renter",
			monatlicheRentenzahlungen: 1700.0,
			monatlichePensionszahlungen: 0.0
			])
		
		assert result == 1550.0
	}
	
	@Test
	void testEinkuenftePensionaer() {
		Object result = runDsl("einkuenfte.formel",  [
			monatlichesBruttoeinkommen: 0.0,
			Steuern: 350.0,
			Sozialabgaben: 200.0,
			sonstigeBelastungen: 0.0,
			Beruf: "Pensionär",
			monatlicheRentenzahlungen: 0.0,
			monatlichePensionszahlungen: 3200.0
			]);
		
		assert result == 2650
	}
	
	@Test
	void testEinkuenfteStudent() {
		Object result = runDsl("einkuenfte.formel", [
			monatlichesBruttoeinkommen: 400.0,
			Steuern: 0.0,
			Sozialabgaben: 0.0,
			sonstigeBelastungen: 150.0,
			Beruf: "Student",
			monatlicheRentenzahlungen: 0.0,
			monatlichePensionszahlungen: 0.0
			]);
		
		assert result == 250.0
	}
	
	@Test
	void testEinkuenfteAngestellter() {
		Object result = runDsl("einkuenfte.formel", [
			monatlichesBruttoeinkommen: 2300.0,
			Steuern: 210.0,
			Sozialabgaben: 400.0,
			sonstigeBelastungen: 160.0,
			Beruf: "Angestellter",
			monatlicheRentenzahlungen: 0.0,
			monatlichePensionszahlungen: 0.0
			]);
		
		assert result == 1530.0
	}
	
	@Test
	void testEinkuenfteBeamter() {
		Object result = runDsl("einkuenfte.formel", [
			monatlichesBruttoeinkommen: 2300.0,
			Steuern: 210.0,
			Sozialabgaben: 0.0,
			sonstigeBelastungen: 460.0,
			Beruf: "Beamter",
			monatlicheRentenzahlungen: 0.0,
			monatlichePensionszahlungen: 0.0
			]);
		
		result == 1630.0
	}

	def runDsl(String scriptName, Map p) {
		Binding b = new Binding(p)
		
		CompilerConfiguration cc = new CompilerConfiguration(
			scriptBaseClass: DslDefinition.class.name)
		
		GroovyShell gs = new GroovyShell(b, cc)
		Script s = gs.parse(new File(scriptName))
		
		def result = s.run()
	}
}
