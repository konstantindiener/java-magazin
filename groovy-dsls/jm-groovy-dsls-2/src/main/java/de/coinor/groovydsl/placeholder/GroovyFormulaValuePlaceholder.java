package de.coinor.groovydsl.placeholder;

import de.coinor.groovydsl.GroovyFormula;

public class GroovyFormulaValuePlaceholder implements ValuePlaceholder {

	final GroovyFormula formula;
	
	public GroovyFormulaValuePlaceholder(GroovyFormula formula) {
		super();
		this.formula = formula;
	}

	@Override
	public Object getValue() {
		return formula.execute();
	}
}
