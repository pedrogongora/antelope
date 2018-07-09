/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package antelope;

import antelope.ctl.parser.ParseException;
import java.util.TreeSet;

/**
 *
 * @author pedrogl
 */
public class CTLSyntaxErrorMessage {
    
    private final static int MAX_LIST = 155;
    
    public static String getCurrentTokenMsg(ParseException ex) {
        StringBuilder sb = new StringBuilder();
        sb.append("Encountered \"");
        sb.append((ex.currentToken.image == null ? "" : ex.currentToken.image));
        sb.append("\" at line ");
        sb.append(ex.currentToken.next.beginLine);
        sb.append(", column ");
        sb.append(ex.currentToken.next.beginColumn);
        sb.append(".");
        return sb.toString();
    }
    
    public static String getExpectedTokensMsg(ParseException ex) {
        StringBuilder sb = new StringBuilder();
        sb.append("Expecting one of: ");
        TreeSet<String> set = new TreeSet<String>();
        for (int i=0; i<ex.expectedTokenSequences.length && i<MAX_LIST; i++) {
            //if (i != 0) {
            //    sb.append(", ");
            //}
            for (int tindex : ex.expectedTokenSequences[i]) set.add(prettyToken(tindex, ex));
        }
        int tmp = 0;
        for (String prettyName : set) {
            if (tmp != 0 && tmp != set.size() && !prettyName.equals("")) sb.append(", ");
            sb.append(prettyName);
            tmp++;
        }
        if (ex.expectedTokenSequences.length > MAX_LIST) {
            sb.append(" ...");
        }
        sb.append(".");
        return sb.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }
    
    private static String prettyToken(int tokenIndex, ParseException ex) {
        String tokenImage = ex.tokenImage[tokenIndex];
        String s;
        if (tokenImage.equals("<EOF>")) {
            s = "<End Of File>";
        } else if (tokenImage.equals("\" \"")) {
            s = "<Blank Space>";
        } else if (tokenImage.equals("\"\\t\"")) {
            s = "<Blank Space>";
        } else if (tokenImage.equals("\"\\n\"")) {
            s = "<Blank Space>";
        } else if (tokenImage.equals("\"\\r\"")) {
            s = "<Blank Space>";
        } else if (tokenImage.equals("\"\\r\\n\"")) {
            s = "<Blank Space>";
        } else if (tokenImage.equals("<token of kind 6>")) {
            s = "<One Line Comment>";
        } else if (tokenImage.equals("<token of kind 7>")) {
            s = "<Multiline Comment>";
        } else if (tokenImage.equals("<AOPEN>")) {
            s = "\"A(\"";
        } else if (tokenImage.equals("<EOPEN>")) {
            s = "\"E(\"";
        } else if (tokenImage.equals("<AT_VAR>")) {
            s = "\"@\"";
        } else if (tokenImage.equals("<AT_NOM>")) {
            s = "\"@\"";
        } else if (tokenImage.equals("<IDENTIFIER>")) {
            s = "<Identifier>";
        } else if (tokenImage.equals("<NOMINAL>")) {
            s = "<State Name>";
        } else if (tokenImage.equals("<DECIMAL>")) {
            s = "<Decimal Number>";
        } else if (tokenImage.equals("<HEX>")) {
            s = "<Hexadecimal Number>";
        } else if (tokenImage.equals("<BIN>")) {
            s = "<Binary Number>";
        } else if (tokenImage.equals("<LETTER>")) {
            s = "<Letter>";
        } else if (tokenImage.equals("<DIGIT>")) {
            s = "<Decimal Digit>";
        } else if (tokenImage.equals("<HEX_DIGIT>")) {
            s = "<Hexadecimal Digit>";
        } else if (tokenImage.equals("<BIN_DIGIT>")) {
            s = "<Binary Digit>";
        } else if (tokenImage.equals("<BLANKS>")) {
            s = "<Blank Space>";
        } else if (tokenImage.equals("<ILLEGAL_CHAR>")) {
            s = "<Illegal Character>";
        } else {
            s = tokenImage;
        }
        return s;
    }
}
