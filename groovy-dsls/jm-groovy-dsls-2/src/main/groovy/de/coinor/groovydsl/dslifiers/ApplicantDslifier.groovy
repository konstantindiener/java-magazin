package de.coinor.groovydsl.dslifiers

import java.util.Map;

import de.coinor.groovydsl.Applicant;
import de.coinor.groovydsl.placeholder.ValuePlaceholder;

class ApplicantDslifier {

	public void dslify() {
		Applicant.metaClass {
			getMonatlichesNettoeinkommen {
				delegate.takeHomeAmount
			}
			
			getMonatlicheMiete {
				delegate.domicile.monthlyRent
			}
		}
	}
}
