package de.coinor.groovydsl;

import java.util.Map;

import groovy.lang.GroovyShell;
import groovy.lang.Script;

import org.codehaus.groovy.control.CompilerConfiguration;

import de.coinor.groovydsl.dslifiers.ScriptDslifier;
import de.coinor.groovydsl.placeholder.ValuePlaceholder;

public class GroovyFormula {

	final String name;
	
	final String code;

	private Script script;

	private final Map<String, ValuePlaceholder> valuePlaceholders;

	private GroovyShell groovyShell;

	public GroovyFormula(String name, String code, CompilerConfiguration compilerConfiguration, Map<String, ValuePlaceholder> valuePlaceholders) {
		super();
		this.name = name;
		this.code = code;
		this.valuePlaceholders = valuePlaceholders;

		groovyShell = new GroovyShell(compilerConfiguration);
	}
	
	public synchronized Object execute() {
		if (script == null) {
			script = groovyShell.parse(this.code);
		}
		
		new ScriptDslifier(script).dslify(valuePlaceholders);
		return script.run();
	}
}
