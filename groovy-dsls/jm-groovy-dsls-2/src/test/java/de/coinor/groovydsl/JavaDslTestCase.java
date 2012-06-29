package de.coinor.groovydsl;

import static org.junit.Assert.assertEquals;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.Before;
import org.junit.Test;

import de.coinor.groovydsl.dslifiers.AmountWithCurrencyDslifier;
import de.coinor.groovydsl.dslifiers.ApplicantDslifier;
import de.coinor.groovydsl.dslifiers.RealEstateDslifier;

public class JavaDslTestCase {

	private static final Currency EURO_CURRENCY = Currency.getInstance("EUR");
	
	@Before
	public void setUp() {
		new RealEstateDslifier().dslify();
		new AmountWithCurrencyDslifier().dslify();
		new ApplicantDslifier().dslify();
	}

	@Test
	public void testNettoeinkommen() throws CompilationFailedException, IOException {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("monatlichesBruttoeinkommen", 2300.0);
		p.put("Steuern", 210.0);
		p.put("Sozialabgaben", 400.0);
		p.put("sonstigeBelastungen", 160.0);
		
		Object result = runDsl("monatlichesNettoeinkommen.formel", p);
		
		assertEquals(1530.0, result);
	}
	
	@Test
	public void testMonatlichFreiVerfügbarerBetrag1() throws CompilationFailedException, IOException {
		RealEstate realEstate = new RealEstate(
				true, new AmountWithCurrency(650.0, EURO_CURRENCY), 60.0);
		Applicant applicant = new Applicant(
				new AmountWithCurrency(1530.0, EURO_CURRENCY),
				realEstate);

		Map<String, Object> p = new HashMap<String, Object>();
		p.put("Immobilie", realEstate);
		p.put("Antragsteller", applicant);
		p.put("Kreditrate", new AmountWithCurrency(270.0, EURO_CURRENCY));

		Object result = runDsl("monatlichFreiVerfuegbarerBetrag.formel", p);
		
		assertEquals(new AmountWithCurrency(1260.0, EURO_CURRENCY), result);
	}
	
	@Test
	public void testMonatlichFreiVerfügbarerBetrag2() throws CompilationFailedException, IOException {
		RealEstate realEstateToBuy = new RealEstate(
				false, new AmountWithCurrency(430.0, EURO_CURRENCY), 60.0);
		Applicant applicant = new Applicant(
				new AmountWithCurrency(1530.0, EURO_CURRENCY),
				new RealEstate(false,
						new AmountWithCurrency(650.0, EURO_CURRENCY), 75.0));

		Map<String, Object> p = new HashMap<String, Object>();
		p.put("Immobilie", realEstateToBuy);
		p.put("Antragsteller", applicant);
		p.put("Kreditrate", new AmountWithCurrency(270.0, EURO_CURRENCY));
		
		Object result = runDsl("monatlichFreiVerfuegbarerBetrag.formel", p);
		
		assertEquals(new AmountWithCurrency(1040.0, EURO_CURRENCY), result);
	}
	
	@Test
	public void testMietPruefung1() throws CompilationFailedException, IOException {
		RealEstate realEstate = new RealEstate(
				true, new AmountWithCurrency(430.0, EURO_CURRENCY), 60.0);
		
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("Immobilie", realEstate);
		
		Object result = runDsl("mietPruefung.formel", p);
		
		assertEquals(true, result);
	}
	
	@Test
	public void testMietPruefung2() throws CompilationFailedException, IOException {
		RealEstate realEstate = new RealEstate(
				true, new AmountWithCurrency(680.0, EURO_CURRENCY), 60.0);
		
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("Immobilie", realEstate);
		
		Object result = runDsl("mietPruefung.formel", p);
		
		assertEquals(false, result);
	}
	
	@Test
	public void testEinkuenfteRentner() throws CompilationFailedException, IOException {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("monatlichesBruttoeinkommen", 0.0);
		p.put("Steuern", 150.0);
		p.put("Sozialabgaben", 200.0);
		p.put("sonstigeBelastungen", 0.0);
		p.put("Beruf", "Renter");
		p.put("monatlicheRentenzahlungen", 1700.0);
		p.put("monatlichePensionszahlungen", 0.0);
		
		Object result = runDsl("einkuenfte.formel", p);
		
		assertEquals(1550.0, result);
	}
	
	@Test
	public void testEinkuenftePensionaer() throws CompilationFailedException, IOException {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("monatlichesBruttoeinkommen", 0.0);
		p.put("Steuern", 350.0);
		p.put("Sozialabgaben", 200.0);
		p.put("sonstigeBelastungen", 0.0);
		p.put("Beruf", "Pensionär");
		p.put("monatlicheRentenzahlungen", 0.0);
		p.put("monatlichePensionszahlungen", 3200.0);
		
		Object result = runDsl("einkuenfte.formel", p);
		
		assertEquals(2650.0, result);
	}
	
	@Test
	public void testEinkuenfteStudent() throws CompilationFailedException, IOException {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("monatlichesBruttoeinkommen", 400.0);
		p.put("Steuern", 0.0);
		p.put("Sozialabgaben", 0.0);
		p.put("sonstigeBelastungen", 150.0);
		p.put("Beruf", "Student");
		p.put("monatlicheRentenzahlungen", 0.0);
		p.put("monatlichePensionszahlungen", 0.0);
		
		Object result = runDsl("einkuenfte.formel", p);
		
		assertEquals(250.0, result);
	}
	
	@Test
	public void testEinkuenfteAngestellter() throws CompilationFailedException, IOException {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("monatlichesBruttoeinkommen", 2300.0);
		p.put("Steuern", 210.0);
		p.put("Sozialabgaben", 400.0);
		p.put("sonstigeBelastungen", 160.0);
		p.put("Beruf", "Angestellter");
		p.put("monatlicheRentenzahlungen", 0.0);
		p.put("monatlichePensionszahlungen", 0.0);
		
		Object result = runDsl("einkuenfte.formel", p);
		
		assertEquals(1530.0, result);
	}
	
	@Test
	public void testEinkuenfteBeamter() throws CompilationFailedException, IOException {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("monatlichesBruttoeinkommen", 2300.0);
		p.put("Steuern", 210.0);
		p.put("Sozialabgaben", 00.0);
		p.put("sonstigeBelastungen", 460.0);
		p.put("Beruf", "Beamter");
		p.put("monatlicheRentenzahlungen", 0.0);
		p.put("monatlichePensionszahlungen", 0.0);
		
		Object result = runDsl("einkuenfte.formel", p);
		
		assertEquals(1630.0, result);
	}

	Object runDsl(String name, Map<String, Object> properties) throws CompilationFailedException, IOException {
		Binding b = new Binding(properties);
		
		CompilerConfiguration conf = new CompilerConfiguration();
		conf.setScriptBaseClass(DslDefinition.class.getName());
		GroovyShell gs = new GroovyShell(b, conf);
		
		Script s = gs.parse(new InputStreamReader(new FileInputStream(new File(name)), "UTF-8"));
		
		return s.run();
	}
}
