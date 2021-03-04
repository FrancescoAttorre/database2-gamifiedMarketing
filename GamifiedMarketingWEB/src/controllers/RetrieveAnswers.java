package controllers;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import entities.Response;
import services.QuestionnaireService;
import services.ResponseService;

@WebServlet("/RetrieveAnswers")
public class RetrieveAnswers extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@EJB(name = "services/ResponseService")
	private ResponseService responseService;
	TemplateEngine templateEngine;
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}

	//todo:retrieve all answers of a selected user
	//requested when clicking on show answers of the inspection page
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int writerId = Integer.parseInt(req.getParameter("userid"));
		
		HttpSession session = req.getSession();
		
		System.out.println(session.getCreationTime());
		
		
		int questionnaireId = (int) session.getAttribute("questionnaireId");
		
		
		List<Response> responses = responseService.retrieveResponsesOfQuestionnaire(writerId, questionnaireId);
		
		
		String path = "WEB-INF/InspectionPage.html";
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		
		//session.setAttribute("answers", responses);
		//ctx.setVariable("questionnaireId", questionnaireId);
		
		ctx.setVariable("answers", responses);
		
		templateEngine.process(path, ctx, resp.getWriter());
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}
	
	
}
