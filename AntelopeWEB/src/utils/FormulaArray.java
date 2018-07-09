package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FormulaArray implements Serializable, Iterable<Formula> {
	private static final long serialVersionUID = 1L;
	private List<Formula> array = new ArrayList<Formula>();
	
	public void add(Formula f) {
		this.array.add(f);
	}
	
	public List<Formula> getList() {
		return this.array;
	}
	public void remove(long id){
		for(Formula fa: array){
			if (fa.getId()==id) {
				array.remove(fa);
				break;
			}
		}
	}

	@Override
	public Iterator<Formula> iterator() {
		return array.iterator();
	}
	
	public String getFormulaName(String formula) {
		for(Formula f : array) {
			if (f.getFormula().equals(formula)) return f.getNombre();
		}
		return "Custom property";
	}
	
}// end class
