package utils;

import controller.GeneLimitException;
import java.io.IOException; 
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.fileupload.FileUploadException;

public abstract class BaseServletUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletConfig config = null;

	protected abstract String getData(HttpServletRequest request) throws FileUploadException, GeneLimitException;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//response.setContentType("text/html");
		//java.io.PrintWriter out = response.getWriter();
		try {
			String result = getData(request);
			request.getSession().setAttribute("result", result);
			response.sendRedirect("../content/result.jsp");
		} catch (SecurityException suex) {
			String msg = suex.toString();
//			out.println("Error detectado por smartUpload al subir el(los) archivo(s).<br>");
//			out.println("Error especifico: <font color='red'>" + traduce(msg) + "</font><br>");
//			out.println("Numero de error especifico: " + corta(msg) + "<br>");
//			out.println("<a href='#' onclick='history.back(-1)'>Regresar</a>");
			request.getSession().setAttribute("uploadError", msg);
			response.sendRedirect("../content/error.jsp");
		} catch (GeneLimitException e) {
                    response.sendRedirect("../content/download.jsp");
                } catch (Exception ex) {
			request.getSession().setAttribute("uploadError", ex.toString());
			response.sendRedirect("../content/error.jsp");
		}
	}
	/*
	private String corta(String s) {
		int inicio = s.indexOf("(");
		int fin = s.indexOf(")");
		return s.substring(inicio + 1, fin);
	}
	
	private String traduce(String msg) {
		String err = corta(msg);
		if (err.equals("1010"))
			return "No esta permitido subir archivos con esta extension";
		if (err.equals("1105"))
			return "Se ha exedido el tamano de archivo permitido";
		return msg;
	}*/
	
	final public void init(ServletConfig config) throws ServletException {
		this.config = config;
	}

	final public ServletConfig getServletConfig() {
		return config;
	}

	/**
	 * Este es un metodo de prueba
	 * @param a
	 * @param b
	 * @return
	 */
	public int suma(int a, int b) {
		return a+b;
	} 
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		java.io.PrintWriter out = response.getWriter();
		out.println("Metodo doGet no permitido para este request<br>");
		out.println("<br><a href='#' onclick='history.back(-1)'>Regresar</a>");
	}
	protected void logger_info(String s) {
		//System.out.println(s);
	}
}
