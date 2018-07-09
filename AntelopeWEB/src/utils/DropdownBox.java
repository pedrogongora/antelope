package utils;

public class DropdownBox {
    
    public final static String CONJUNCTIVE_EFP_VALUE = "-- Use conjunctive equational fixed point formula --";
    public final static String DISJUNCTIVE_EFP_VALUE = "-- Use disjunctive equational fixed point formula --";

	private FormulaArray fa = null;

	public DropdownBox(FormulaArray box) {
		this.fa = box;
	}

	public String buildDropDown() {
		FormulaArray box = this.fa;
		StringBuffer sb = new StringBuffer("<select name='formula'  id='formula' onchange='javascript:changePropertySelect(this.options[this.selectedIndex].value);'>\n");
		if(box==null) {
			sb.append("</select>\n");
			return sb.toString();
		}
		for(Formula s : box) {
			sb.append("<option value='");
			sb.append(s.getFormula());
			sb.append("'>");
			sb.append(s.getNombre());
			sb.append("</option>\n");
		}
                sb.append("<option value='"+CONJUNCTIVE_EFP_VALUE+"'>"+CONJUNCTIVE_EFP_VALUE+"</option>\n");
                sb.append("<option value='"+DISJUNCTIVE_EFP_VALUE+"'>"+DISJUNCTIVE_EFP_VALUE+"</option>\n");
                sb.append("<option value='-- Use a custom property --'>-- Use a custom property --</option>\n");
		sb.append("</select>\n");
		return sb.toString();
	}
	public String buildTable(boolean bandera) {
		FormulaArray box = this.fa;
		StringBuffer sb = new StringBuffer("<table cellspacing='0' cellpadding='0'>\n");
		if(box==null) {
			sb.append("</table>\n");
			return sb.toString();
		}
		
		String question = "";
		if (bandera) question = "&nbsp;&nbsp;&nbsp;<img width='12' height='12' src='../img/question2.png' title='Click on any link to use such property with the current uploaded model' />";
		
		sb.append("<tr>");
		sb.append("<th width='230px'>Name</th>");
		sb.append("<th width='185px'>Formula"+question+"</th>");
		sb.append("<th width='250px'>Comments</th>");
		sb.append("<th width='40px'>&nbsp;</th>");

		
//		if (bandera) sb.append("<th width='50px'>&nbsp;</th>");
		sb.append("</tr>\n");
		for(Formula s : box) {
			sb.append("<tr>");
			sb.append("<td>");
			sb.append(s.getNombre());
			sb.append("</td><td>");
			
			if(bandera) {
				sb.append("<form method='post' action='../servlet/Recalculate'>");
				sb.append("<input type='hidden' name='formula' value='"+s.getFormula()+"'/>");
				sb.append("<a href='#' ");
				sb.append("class='remarca' ");
				sb.append("title='Click to recalculate with this formula and the current uploaded model' ");
				sb.append("onclick='parentNode.submit()'>");
				sb.append(s.getFormula());
				sb.append("</a>");
				sb.append("</form>");
			} else {
				sb.append(s.getFormula());
			}
			
			sb.append("</td><td>");
			sb.append(s.getDescripcion());
			sb.append("</td><td>");
			if(s.getId()<=17) {
				sb.append("&nbsp;");
			} else {
				sb.append("<form><a href='remove.jsp?id="+s.getId()+"'>remove</a></form>");
			}
//			if (bandera) {
//				sb.append("<td>");
//				sb.append(".");
//				sb.append("</td>");
//			}
			// 
			sb.append("</td>");
			sb.append("<tr>\n");
		}
		sb.append("</table>\n");
		return sb.toString();
	}
}
