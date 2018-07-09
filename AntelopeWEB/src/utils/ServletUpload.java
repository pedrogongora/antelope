package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import antelope.AntelopeAccess;
import antelope.ModelCheckingResults;
import controller.GeneLimitException;

public class ServletUpload extends BaseServletUpload {
	private static final long serialVersionUID = 1L;

	public static String getTable(List<byte[]> labels, SortedSet<String> ss) {
		StringBuffer file = new StringBuffer();
	    StringBuffer output = new StringBuffer("<table cellspacing='0' cellpadding='0'>\n");
        
	    output.append("<tr>\n");
	    output.append("<th>&nbsp;</th>");
	    //output.append("<th>&nbsp;</th>");
	    //output.append("<th>&nbsp;</th>");
        for(String s : ss){
        	output.append("<th>" + s + "</th>");
        	file.append(completa(s));
        }
        file.append("\n");
        output.append("<th><NOBR>Row results</NOBR></th>");
        output.append("\n</tr>\n<tr>\n");
        

        long accumulator = 0;
        int index = 0;
        for (byte[] row : labels) {
        	StringBuilder fullRow = new StringBuilder();

        	for(byte b : row){
        		if (b==-1) {
        			file.append(completa("*"));
        			fullRow.append("*");
        		} else {
        			file.append(completa(b+""));
        			fullRow.append(b);
        		}
        	}
        	file.append("\n");
        	index++;
        	
        	boolean hasWildcards = (fullRow.indexOf("*")>0);
        	
        	output.append("<td><select onChange=\"javascript:if (this.value!='') {go(); location=this.value;}\">");
        	output.append("<option value=\"\">Choose an action:</option>");
        	if (hasWildcards) output.append("<option value=\"removeWildcards.jsp?fullRow="+fullRow+"\">Remove wildcards</option>");
        	output.append("<option value=\"basin.jsp?r="+fullRow+"\">Compute basin of attraction</option>");
        	output.append("<option value=\"rowstep.jsp?fullRow="+fullRow+"&direction=Y\">Compute successors</option>");
        	output.append("<option value=\"rowstep.jsp?fullRow="+fullRow+"&direction=X\">Compute predecessors</option>");
        	output.append("</select></td>");
        	
        	//if(fullRow.indexOf("*")>0) {
	        //	output.append("<td><input type='button' name='fullRow'");
	        //	output.append(index);
	        //	output.append(" value='Remove wildcards' onclick='location=\"removeWildcards.jsp?fullRow="+fullRow+"\"' /></td>");
        	//} else {
        	//	output.append("<td>&nbsp;</td>");
        	//}
        	//output.append("<td><input type='button' name='basin' value='Basin of attraction' onclick='location=\"basin.jsp?r="+fullRow+"\"' /></td>");
        	
        	int counter = 0;
        	for(byte b : row){
        		if (b==-1) {
        			counter++;
        			output.append("<td>*</td>");
        		} else {
        			output.append("<td>" + b + "</td>");
        		}
        	}
        	long card = exp(counter);
        	accumulator = accumulator + card;
        	output.append("<td class='remarca'>" + card + "</td>");
        	output.append("\n</tr>\n<tr>\n");
        }
        
        int tam = ss.size()+2;
        String fileName = creaFile(file.toString());
        output.append("<td colspan='"+(tam-1)+"'>");
        //output.append("  <form name='getfile' method='post' action='print.jsp'>");
        //output.append("    <input type='hidden' name='resultado' value='"+file.toString()+"' />");
        output.append("      Right click <u><a href='"+fileName+"'>here</a></u> and select 'Save link as' to get a plain text file with this results");
        //output.append("  </form>");
        output.append("</td>");
        
        output.append("<td class='remarca2'>"+accumulator+"</td>");
        output.append("</table>\n");
        return output.toString();
	}
	
	private static String completa(String source) {
		int maxLen = 7;
		int tam = source.length();
		if(tam<=maxLen) {
			return source + spaces(maxLen-tam);
		}
		return source;
	}

	private static String spaces(int n) {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<n; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}

	private static String creaFile(String str) {
		String subdirectorio = "bag";
		String baseName = System.currentTimeMillis() + ".txt";
		String name = subdirectorio + "/" + baseName; // aqui es "/" porque va a ser un URL web. NO USAR Starter.FS
		String fullName = Starter.REAL_PATH + Starter.FS +  "content" + Starter.FS + subdirectorio + Starter.FS + baseName;
		try {
			FileWriter fw = new FileWriter(fullName);
			fw.write(str);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return name;
	}

	private static long exp(int counter) {
		if(counter==0) return 1;
		return 2*exp(counter-1);
	}

	@SuppressWarnings("unchecked")
	protected String getData(HttpServletRequest request) throws FileUploadException, GeneLimitException {
		String modo = "1";
		String formula = "";
		String table = "";
                String customFormula = "";
		HttpSession session = request.getSession();
		
		//boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(0);
		ServletFileUpload upload = new ServletFileUpload(factory);
		
		List items = upload.parseRequest(request);
		Iterator iter = items.iterator();
		
		String fileName = "";
		while (iter.hasNext()) {
		    FileItem item = (FileItem)iter.next();
		    String name = item.getFieldName();
		    if(name.equalsIgnoreCase("tabla")) {
		    	fileName = item.getName();
		    	session.setAttribute("fileName", fileName);
		    	table = item.getString();
		    }
		    if(name.equalsIgnoreCase("formula")) {
		    	formula = item.getString();
		    	session.setAttribute("formula", formula);
		    }
		    if(name.equalsIgnoreCase("modo")){
		    	modo = item.getString();
		    	//session.setAttribute("modo", modo);
		    	// getAttribute("modo")
		    }
		    if(name.equalsIgnoreCase("customFormula")){
		    	customFormula = item.getString();
		    }
		}
                
                formula = (formula.equals("-- Use a custom property --")
                        ? customFormula
                        : formula);
                boolean useEFP = (
                        formula.equals(DropdownBox.CONJUNCTIVE_EFP_VALUE) ||
                        formula.equals(DropdownBox.DISJUNCTIVE_EFP_VALUE)
                        );
		
		
		session.setAttribute("tableWithDefinitions", table);
		//session.setAttribute("tableWithDefinitions", fileName);
        session.setAttribute("tableWithDefinitionsMode", "inline");
		
		AntelopeAccess antilopeAccess = Starter.getAntelopeAccessEngine(getServletContext());
	    
            //long iniLoad = System.currentTimeMillis();
	    if(fileName.indexOf(".eqn")>0) {
	    	antilopeAccess.loadEquationsModel(new StringReader(table));
	    }
	    else { 
	    	// .tbl y cualquier otra extension se van al modo tabla:
	    	antilopeAccess.loadTableModel(new StringReader(table));
	    }
            //long endLoad = System.currentTimeMillis();
            //session.setAttribute("modelLoadingTime", ""+(endLoad-iniLoad));
            
            if (antilopeAccess.getModelVariables().size() > 13 &&
                    request.getServerName().equals("turing.iimas.unam.mx")) {
                throw new GeneLimitException("Uploaded network has "
                        + antilopeAccess.getModelVariables().size()
                        + " genes");
            }
            
	    if(modo.equals("1")) {
	    	session.setAttribute("modo", "Synchronous");
                session.setAttribute("asyncConvertTime", "-");
	    }
	    if(modo.equals("2")) {
                long iniAsync = System.currentTimeMillis();
	    	antilopeAccess.makeModelStrictlyAsynchronous();
                long endAsync = System.currentTimeMillis();
	    	session.setAttribute("modo", "Strictly Asynchronous");
                session.setAttribute("asyncConvertTime", ""+(endAsync-iniAsync));
	    }
	    if(modo.equals("3")) {
                long iniAsync = System.currentTimeMillis();
	    	antilopeAccess.makeModelAsynchronous();
                long endAsync = System.currentTimeMillis();
                session.setAttribute("modo", "Asynchronous");
                session.setAttribute("asyncConvertTime", ""+(endAsync-iniAsync));
	    }
	    
            if (useEFP) formula = antilopeAccess.getEFPFormula(formula.equals(DropdownBox.CONJUNCTIVE_EFP_VALUE));
	    logger_info(formula);
            //long ini = System.currentTimeMillis();
	    //List<byte[]> labels = antilopeAccess.getLabelsArray(formula);
            //long end = System.currentTimeMillis();
            
            //session.setAttribute("verificationTime", ""+(end-ini));
            ModelCheckingResults results = antilopeAccess.getLabelsArray(formula);
            session.setAttribute("verificationTime", results.getFormulaVerificationTime()+"");
            session.setAttribute("modelLoadingTime", results.getModelBDDCreationTime()+"");
	    SortedSet<String> modelVariables = antilopeAccess.getModelVariables();
	    session.setAttribute("modelVariables", modelVariables);
            
            session.setAttribute("formula", formula);
            
            return getTable(results.getLabelsArray(), modelVariables);		
	}
	
	public static String getTextFromFile(String file) {
		try {
			return readFileAsString(ManageFormulas.MODEL_PATH + file);
		} catch (IOException e) {
			return "ERROR";
		}
	}
	
    private static String readFileAsString(String filePath) throws IOException{
        StringBuffer fileData = new StringBuffer(10000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[10240];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[10240];
        }
        reader.close();
        return fileData.toString();
    }

}
