package de.coinor.groovydsl.dslifiers

import de.coinor.groovydsl.RealEstate;

class RealEstateDslifier {

	public void dslify() {
		RealEstate.metaClass {
			getMonatlicheMiete {
				delegate.monthlyRent
			}
			
			getGröße() {
				delegate.sizeInSquareMeters
			}
			
			isSelbstgenutzt() {
				delegate.ownerOccupied
			}
		}
	}
}
