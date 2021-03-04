package controllers;

import java.io.IOException;
import java.util.ArrayList;
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

import entities.Product;
import entities.Questionnaire;
import entities.State;
import entities.User;
import services.QuestionService;
import services.QuestionnaireService;
import services.ResponseService;
import services.StateService;

@WebServlet("/InspectQuestionnaire")
public class InspectQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	
	@EJB(name="services/QuestionnaireService")
	private QuestionnaireService qstService;
	
	@EJB(name="services/StateService")
	private StateService stateService;
	
	@EJB(name="services/ResponseService")
	private ResponseService responseService;
	
	public void init() throws ServletException {
		ServletContext servletContext = getServletContext();
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		this.templateEngine = new TemplateEngine();
		this.templateEngine.setTemplateResolver(templateResolver);
		templateResolver.setSuffix(".html");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int questionnaireId = Integer.parseInt(req.getParameter("questionnaireid"));
		
		
		List<State> states = stateService.findStatesOfQuestionnaire(questionnaireId);
		List<User> submitted = new ArrayList<User>();
		List<User> cancelled = new ArrayList<User>();
		
		for (State s : states) {
			if(s.isSubmitted())
				submitted.add(s.getUser());
			else if (s.isCancelled())
				cancelled.add(s.getUser());
		}
		
		
		String path = "WEB-INF/InspectionPage.html";
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
		
		if(req.getParameter("userid") == null)
			ctx.setVariable("answers", null);
		else {
			ctx.setVariable("answers", responseService.retrieveResponsesOfQuestionnaire(Integer.parseInt(req.getParameter("userid")), questionnaireId));
		}
		ctx.setVariable("questionnaireid", questionnaireId);
		ctx.setVariable("userSubmitted", submitted);
		ctx.setVariable("userCancelled", cancelled);
		
		templateEngine.process(path, ctx, resp.getWriter());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//nothing ???
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

}
