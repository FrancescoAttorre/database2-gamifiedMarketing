package controllers;

import java.io.IOException;
import java.util.List;

import entities.*;
import services.*;
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

import services.QuestionnaireService;

@WebServlet("/GoToQuestionnairePage")
public class GoToQuestionnairePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name="services/QuestionnaireService")
	private QuestionnaireService qstService;
	@EJB(name="services/StateService")
	private StateService stateService;
	
    public GoToQuestionnairePage() {
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Questionnaire questionnaireOfTheDay = null;
		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}
		User user = (User) session.getAttribute("user");
		try {
			questionnaireOfTheDay = qstService.findQuestionnaireOfTheDay();
		}catch(Exception e){
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to retrieve Questionnaire of the day");
			return;
		}
		//if the user has already submitted the questionnaire display a message and redirect to home
		List<State> states = stateService.findStatesOfQuestionnaire(questionnaireOfTheDay.getId());
		for(State state : states) {
			if(state.getUser().getId() == user.getId() && state.isSubmitted()) {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Already submitted questionnaire of the day");
				return;
			}
		}
		
		
		//otherwise retrieve qod and its questions and let the user answer
		String path = "/WEB-INF/QuestionnairePage.html";
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("qod", questionnaireOfTheDay);
		ctx.setVariable("questions", questionnaireOfTheDay.getQuestions());

		templateEngine.process(path, ctx, response.getWriter());
		
	}

}
