package controllers;

import java.io.IOException;

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
import services.AccessService;
import services.QuestionnaireService;
import services.StateService;
import services.UserService;

@WebServlet("/CancelSubmission")
public class CancelSubmission extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "services/StateService")
	private StateService stateService;
	@EJB(name = "services/QuestionnaireService")
	private QuestionnaireService questionnaireService;
	
	
	public CancelSubmission() {
		super();
	}

	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		
		stateService.insertState(
				((User)session.getAttribute("user")).getId(), 
				questionnaireService.findQuestionnaireOfTheDay().getId(), 
				false, 
				true);
		
		String homePage = getServletContext().getContextPath() + "/GoToHomePage";
		resp.sendRedirect(homePage);
	}
	
	

}
