package utils;

import javax.servlet.ServletContext;
import javax.servlet.http.*;

import antelope.AntelopeAccess;

/**
 * 
 * @author Administrador
 *
 */
public class Starter extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static String REAL_PATH = "";
    public static String FS = "";

    @Override
    public void init() {
        ServletContext application = getServletContext();
        FS = System.getProperty("file.separator");
        REAL_PATH = application.getRealPath("");
        // Retorna: C:\Users\Administrador\Workspaces\MyEclipse-8.6\.metadata\.me_tcat\webapps\AntelopeWEB
        getAntelopeAccessEngine(application);

        Formula f = null;
        FormulaArray fa = ManageFormulas.load();
        int i = 1;
        if (fa == null) {
            fa = new FormulaArray();
            
            f = new Formula(i++,
                    "Stable steady states",
                    "!s.AX s",
                    "A more efficient (propositional) method is &quot;Use conjunctive equational fixed point formula&quot; in &quot;Select a property:&quot;.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Unstable steady states",
                    "!s.EX s",
                    "A more efficient (propositional) method is &quot;Use disjunctive equational fixed point formula&quot; in &quot;Select a property:&quot;.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Membership to a cycle of any size",
                    "!s.EX EF s",
                    "A state s appears in the strict future of s.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Cycles of size 1 or 2",
                    "!s.EX EX s",
                    "A state s appears after two transitions from s.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Cycles of size 1, 2 or 3",
                    "(!s.EX EX s) | (!s.EX EX EX s)",
                    "The second disjunct does not cover size-two cycles.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Cycles of size 2",
                    "!s.EX (~s & EX s)",
                    "Distinct states.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Cycles of size 3",
                    "!s.EX (~s & (!t.EX (~s & ~t & EX s)))",
                    "Distinct states.");
            fa.add(f);
            
            f = new Formula(i++,
                    "s1 necessary for s2",
                    "~E(~s1 U s2) & EF s2",
                    "It is necessary to go through s1 to reach s2. This is a formula schema, and s1 and s2 must be named. See user's manual for naming states.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Basins of stable stationary states",
                    "EF(!s.AX s)",
                    "Basins of attraction of all stable stationary states.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Basins of possible cycles size 1 or 2",
                    "EF(!s.EX EX s)",
                    "Basins of attraction of all possible cycles of size one or two.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Basins of cycles greater than 2",
                    "~(EF(!s.EX EX s))",
                    "Basins of attraction of all possible cycles of size greater than two.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Possible oscillations any period",
                    "EF!s.EF((not x) and EF(x and s))",
                    "Basins of attraction of cycles with possible oscillations of any period for gene x.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Possible oscillations period 2",
                    "EF!s.EX((not x) and EX(x and s))",
                    "Basins of attraction of cycles with possible oscillations of period two for gene x.");
            fa.add(f);
            
            f = new Formula(i++,
                    "More than one successor",
                    "!s. EX !t. @s. EX ~t",
                    "This formula is optimized to run in time linear in the size of the dynamics of the GRN.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Exactly one successor",
                    "!s. EX !t. @s. AX t",
                    "This formula is optimized to run in time linear in the size of the dynamics of the GRN.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Pulse generator",
                    "(~x & ~y) -> EF(EG x & ~y & EX y & EX EX EG~y)",
                    "Motif of a possible pulse generator. When gene x is activated, gene y has a pulse.");
            fa.add(f);
            
            f = new Formula(i++,
                    "Low-pass filter",
                    "((~x & ~y & EF(x & EX ~x)) -> EX ~x & EX EX ~y) & (~x & ~y -> EF(x & EX x -> EX EX y & EX EX EX y & EX EX EX EX ~y))",
                    "Motif of a low-pass filter. Short pulses in gene x are not transferred to gene y, but long pulses are.");
            fa.add(f);
            
            ManageFormulas.save(fa);
        }
        application.setAttribute("FormulaArray", fa);
    }

    public void destroy() {
    }

    public static AntelopeAccess getAntelopeAccessEngine(ServletContext application) {
        AntelopeAccess antilopeAccess = (AntelopeAccess) application.getAttribute("AntelopeAccessEngine");
        if (antilopeAccess == null) {
            antilopeAccess = new AntelopeAccess();
            application.setAttribute("AntelopeAccessEngine", antilopeAccess);
        }
        return antilopeAccess;
    }
}
