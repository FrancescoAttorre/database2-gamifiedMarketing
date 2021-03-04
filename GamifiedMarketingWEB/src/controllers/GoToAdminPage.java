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

import entities.Product;
import entities.Questionnaire;
import entities.User;
import services.ProductService;
import services.QuestionnaireService;
import services.UserService;

@WebServlet("/GoToAdminPage")
public class GoToAdminPage extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@EJB(name = "services/QuestionnaireService")
	private QuestionnaireService qstService;
	@EJB(name = "services/ProductService")
	private ProductService prdService;
	@EJB(name = "services/UserService")
	private UserService userService;

	private TemplateEngine templateEngine;

	public GoToAdminPage() {
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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// If the user is not logged in (not present in session) redirect to the login
		String loginpath = getServletContext().getContextPath() + "/index.html";
		HttpSession session = request.getSession();
		if (session.isNew() || session.getAttribute("user") == null) {
			response.sendRedirect(loginpath);
			return;
		}else if(!((User)session.getAttribute("user")).isAdmin()) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "You are not admin");
		}
		
		List<Questionnaire> questionnaires = qstService.findAllQuestionnaire();
		List<Product> products = prdService.findAllProductIds();

		String path = "WEB-INF/AdminPage.html";

		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("topquestionnaires", questionnaires);
		ctx.setVariable("products", products);

		templateEngine.process(path, ctx, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

}
