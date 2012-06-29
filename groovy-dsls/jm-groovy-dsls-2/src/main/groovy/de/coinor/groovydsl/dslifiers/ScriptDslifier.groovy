package de.coinor.groovydsl.dslifiers

import java.util.Map;

import de.coinor.groovydsl.placeholder.ValuePlaceholder;
import groovy.lang.Script;

class ScriptDslifier {

	private final Script script

	public ScriptDslifier(Script script) {
		this.script = script
	}

	public void dslify(Map<String, ValuePlaceholder> valuePlaceholders) {
		script.metaClass.getProperty { String name ->
			ValuePlaceholder valuePlaceholder = valuePlaceholders[name]
			if (valuePlaceholder) {
				valuePlaceholder.value
			}
			else {
				delegate.@"$name"
			}
		}
	}
}
