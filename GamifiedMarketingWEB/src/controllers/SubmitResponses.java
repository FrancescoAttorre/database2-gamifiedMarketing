package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import entities.User;
import services.QuestionnaireService;
import services.ResponseService;

@WebServlet("/SubmitResponses")
public class SubmitResponses extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name="services/ResponseService")
    private ResponseService rspService;  
    
    public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		
    	HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}
		//get data from statistical questions
		char gender = '\u0000';
		if(request.getParameter("gender") != null)
			gender = request.getParameter("gender").charAt(0);
		
		char expertise = '\u0000';
		if(request.getParameter("expertise") != null)
			expertise = request.getParameter("expertise").charAt(0);
		
		Integer age = null;
		if(request.getParameter("age").toString() != "" && request.getParameter("age") != null)
			age = Integer.parseInt(request.getParameter("age"));
		
		//load from session
		Map<Integer,String> responseMap = (Map<Integer, String>) session.getAttribute("responses");
		
		//remove from session
		session.removeAttribute("responses");
		
		//create responses in database
		User user = (User) session.getAttribute("user");
		rspService.insertResponses(user.getId(), responseMap, age, gender, expertise);
		
		//redirect to home page
		String homePage = getServletContext().getContextPath() + "/GoToHomePage";
		response.sendRedirect(homePage);
		
	}

	public void destroy() {}

}
