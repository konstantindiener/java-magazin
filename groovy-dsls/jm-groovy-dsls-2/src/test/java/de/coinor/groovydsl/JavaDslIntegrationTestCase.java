package de.coinor.groovydsl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.Before;
import org.junit.Test;

import de.coinor.groovydsl.dslifiers.AmountWithCurrencyDslifier;
import de.coinor.groovydsl.dslifiers.ApplicantDslifier;
import de.coinor.groovydsl.dslifiers.RealEstateDslifier;
import de.coinor.groovydsl.placeholder.GroovyFormulaValuePlaceholder;
import de.coinor.groovydsl.placeholder.SimpleValuePlaceholder;
import de.coinor.groovydsl.placeholder.ValuePlaceholder;

public class JavaDslIntegrationTestCase {
	
	private static final String FORMEL = ".formel";

	private static final Currency EURO_CURRENCY = Currency.getInstance("EUR");
	
	@Before
	public void setUp() {
		new RealEstateDslifier().dslify();
		new AmountWithCurrencyDslifier().dslify();
		new ApplicantDslifier().dslify();
	}
	
	@Test
	public void testFormelIntegration() throws FileNotFoundException, IOException {
		Map<String, ValuePlaceholder> valuePlaceholders = new HashMap<String, ValuePlaceholder>();
		//new ApplicantDslifier().addPlaceholders(valuePlaceholders);
		
		RealEstate realEstateToBuy = new RealEstate(
				false, new AmountWithCurrency(430.0, EURO_CURRENCY), 60.0);
		valuePlaceholders.put("Immobilie", new SimpleValuePlaceholder(realEstateToBuy, valuePlaceholders));
		Applicant applicant = new Applicant(
				new AmountWithCurrency(0.0, EURO_CURRENCY),
				new RealEstate(false,
						new AmountWithCurrency(650.0, EURO_CURRENCY), 75.0));
		valuePlaceholders.put("Antragsteller", new SimpleValuePlaceholder(applicant, valuePlaceholders));

		valuePlaceholders.put("monatlichesBruttoeinkommen", toAmountPlaceholder(2300.0));
		valuePlaceholders.put("Steuern", toAmountPlaceholder(210.0));
		valuePlaceholders.put("Sozialabgaben", toAmountPlaceholder(00.0));
		valuePlaceholders.put("sonstigeBelastungen", toAmountPlaceholder(460.0));
		valuePlaceholders.put("Kreditrate", toAmountPlaceholder(270.0));
		
		String[] formelFiles = new File("./").list(new FilenameFilter() {
			
			@Override
			public boolean accept(File directory, String filename) {
				return filename.endsWith(FORMEL);
			}
		});
		
		CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
		compilerConfiguration.setScriptBaseClass(DslDefinition.class.getName());
		
		for (String formelFile : formelFiles) {
			String code = IOUtils.toString(new FileInputStream(new File(formelFile)), "UTF-8");
			String formulaName = formelFile.replace(FORMEL, "");
			GroovyFormula groovyFormula = new GroovyFormula(
					formulaName, code, compilerConfiguration, valuePlaceholders);
			valuePlaceholders.put(formulaName, new GroovyFormulaValuePlaceholder(groovyFormula));
		}
		
		for (int i = 0; i < 1000; i++) {
			valuePlaceholders.put("monatlichesBruttoeinkommen", toAmountPlaceholder(2300.0 + i));

			ValuePlaceholder vp = valuePlaceholders.get("monatlichFreiVerfuegbarerBetrag");
			Object result = vp.getValue();
			
			assertEquals(new AmountWithCurrency(1140.0 + i, EURO_CURRENCY), result);
		}
	}

	private ValuePlaceholder toAmountPlaceholder(double d) {
		return new SimpleValuePlaceholder(new AmountWithCurrency(d, EURO_CURRENCY));
	}
}
