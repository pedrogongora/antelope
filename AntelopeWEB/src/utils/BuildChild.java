package utils;

import java.util.List;
import java.util.ArrayList;

public class BuildChild {
	private List<String> resultado = null;
	private String initialString = "";
	
	public BuildChild(String initialString){
		this.initialString = initialString;
		this.resultado = new ArrayList<String>(countStars(initialString));
	}
	
	public List<String> build() {
		build(this.initialString);
		return this.resultado;
	}
	
	private int countStars(String initialString) {
		int j=0;
		int tam = initialString.length();
		for(int i=0; i<tam; i++) {
			if(initialString.charAt(i)=='*') j++;
		}
		return j;
	}
	
	private void build(String secuence) {
		int found = secuence.indexOf("*");
		if(found<0) {
			resultado.add(secuence);
		} else {
			String primerParte = "";
			if(found>0) primerParte = secuence.substring(0, found);
			String segundaParte = secuence.substring(found+1);
			build(primerParte + "0" + segundaParte);
			build(primerParte + "1" + segundaParte);
		}
	}
	
	/*public static void main(String...x) {
		BuildChild bh = new BuildChild("0**11*100*11011");
		List<String> lista = bh.build();
		for(String s : lista) {
			System.out.println(s);
		}
	}*/
	
}// ends class ***
