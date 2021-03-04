package controllers;

import entities.Response;
import entities.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import services.QuestionnaireService;
import services.ResponseService;

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

/**
 * Servlet implementation class CreateResponses
 */
@WebServlet("/CreateResponses")
public class CreateResponses extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	@EJB(name = "services/QuestionnaireService")
	private QuestionnaireService qstService;

	public CreateResponses() {
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			String loginpath = getServletContext().getContextPath() + "/index.html";
			response.sendRedirect(loginpath);
			return;
		}
		// checking data received correctness

		int questionsNumber = qstService.findQuestionnaireOfTheDay().getAmountOfQuestions();
		Map<String, String[]> responses = null;
		try {
			responses = request.getParameterMap();

			// check responses number
			if (questionsNumber != responses.size()) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect param number");
				return;
			}
			// check that key is int and value is string,array size is 1
			for (Map.Entry<String, String[]> entry : responses.entrySet()) {
				Integer.parseInt(entry.getKey());
				if (entry.getValue().length != 1) {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "More than an answer per question");
					return;
				}
				String a = entry.getValue()[0];

				if (a == "") {
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "void mandatory answer");
					return;

				}
			}
		} catch (NumberFormatException | NullPointerException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect or missing param values");
			return;
		}
		/*
		 * User user = (User) session.getAttribute("user"); for (Map.Entry<String,
		 * String[]> entry : responses.entrySet()) {
		 * rspService.createResponse(user.getId(), Integer.parseInt(entry.getKey()),
		 * entry.getValue()[0]); }
		 */

		Map<Integer, String> responseMap = new HashMap();
		for (String questionId : responses.keySet()) {
			responseMap.put(Integer.parseInt(questionId), responses.get(questionId)[0]);
		}

		// load in session
		session.setAttribute("responses", responseMap);

		// redirect to statistical questions
		String statisticalQuestionsPath = getServletContext().getContextPath() + "/GoToStatisticalQuestionsPage";
		response.sendRedirect(statisticalQuestionsPath);

	}
	
	public void destroy() {}
}
