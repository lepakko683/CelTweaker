package celestibytes.celtweaker;

import java.util.LinkedList;
import java.util.List;

public class Util {
	
	public static class OreString {
		
		public final String ore;
		
		public OreString(String ore) {
			this.ore = ore;
		}
		
		public String getOre() {
			return ore;
		}
	}
	
	public static String[] escapedSplit(String str, char escChar) {
		List<String> parts = new LinkedList<String>();
		
		int splitStart = 0;
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i) == escChar && (i > 0 ? str.charAt(i - 1) != '\\' : true)) {
				parts.add(str.substring(splitStart, i));
				splitStart = i + 1;
			}
		}
		if(str.length() - splitStart > 0) {
			parts.add(str.substring(splitStart, str.length()));
		}
		
		return parts.toArray(new String[0]);
	}
	
	public static String[] unescape(String[] strs) {
		for(int i = 0; i < strs.length; i++) {
			strs[i] = strs[i].replace("\\", "");
		}
		
		return strs;
	}
	
	public static String unescape(String str, char c) {
		return str.replace("\\", "");
	}
	
	public static int nUnescapedIndexOf(String s, char c, int n) {
		for(int i = 0; i < s.length(); i++) {
			if(s.charAt(i) == c && (i > 0 ? s.charAt(i - 1) != '\\' : true)) {
				if(n == 0) {
					return i;
				} else {
					n--;
				}
			}
		}
		
		return -1;
	}
	
	public static void printStringArray(String[] stra) {
		System.out.println("PrintArray:");
		for(String s : stra) {
			System.out.println("\t" + s);
		}
	}
	
	public static String unquote(String str) {
		return str.substring(1, str.length() - 1);
	}
	
}
