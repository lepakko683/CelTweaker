package celestibytes.celtweaker.api;

public class Version {
	
	public final int majorNumber, minorNumber;
	
	public Version(int majorNumber, int minorNumber) {
		this.majorNumber = majorNumber;
		this.minorNumber = minorNumber;
	}
	
	public boolean isNewer(Version other) {
		if(majorNumber > other.majorNumber) {
			return true;
		} else if(majorNumber == other.majorNumber) {
			if(minorNumber > other.minorNumber) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Version) {
			Version other = (Version) obj;
			
			return other.minorNumber == minorNumber && other.majorNumber == majorNumber;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return majorNumber + "." + minorNumber;
	}
}
