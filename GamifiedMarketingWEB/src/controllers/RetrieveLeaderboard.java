package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import entities.Leaderboard;
import entities.OptionalResponse;
import entities.Questionnaire;
import entities.State;
import entities.User;
import services.OptionalResponseService;
import services.QuestionnaireService;
import services.ResponseService;
import services.StateService;

@WebServlet("/RetrieveLeaderboard")
public class RetrieveLeaderboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	@EJB(name = "services/StateService")
	private StateService stateService;
	@EJB(name = "services/QuestionnaireService")
	private QuestionnaireService questionnaireService;
	@EJB(name = "services/OptionalResponseService")
	private OptionalResponseService optionalResponseService;
	@EJB(name = "services/ResponseService")
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
		Questionnaire questionnaireOfTheDay = questionnaireService.findQuestionnaireOfTheDay();
		int numberOfMandatoryQuestions = questionnaireOfTheDay.getAmountOfQuestions();

		List<State> states = stateService.findStatesOfQuestionnaire(questionnaireOfTheDay.getId());

		List<Leaderboard> leaderboard = new ArrayList();
		if (states.isEmpty()) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No user submitted the questionnaire of the day");
			return;
		} else {
			for (State state : states) {
				int points = 0;
				Leaderboard element = new Leaderboard();

				if (state.isSubmitted()) {
					int answeredQuestions = responseService.retrieveResponsesOfQuestionnaire(state.getUser().getId(), state.getQuestionnaire().getId()).size();
					
					if(answeredQuestions != numberOfMandatoryQuestions) {
						//user answered before inserting other questions
					}
					
					points = points + answeredQuestions;

					OptionalResponse optional = optionalResponseService
							.getResponsesOfQuestionnaireForUser(questionnaireOfTheDay.getId(), state.getUser().getId());

					if (optional.getAge() != 0)
						points = points + 2;
					if (optional.getSex() != '\u0000')
						points = points + 2;
					if (optional.getExpertise() != '\u0000')
						points = points + 2;

					element.setPoints(points);
					element.setUser(state.getUser());

					int i = 0;
					while (leaderboard.size() > i && leaderboard.get(i) != null) {
						if (leaderboard.get(i).getPoints() < points)
							break;
						i++;
					}
					leaderboard.add(i, element);

				}
			}
			String path = "WEB-INF/LeaderboardPage.html";

			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());

			ctx.setVariable("leaderboard", leaderboard);

			templateEngine.process(path, ctx, resp.getWriter());

		}
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
