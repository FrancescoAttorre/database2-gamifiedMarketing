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

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import entities.Product;
import entities.Questionnaire;
import services.ProductService;
import services.QuestionnaireService;

@WebServlet("/GoToAdminPage")
public class GoToAdminPage extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	@EJB(name = "services/QuestionnaireService")
	private QuestionnaireService qstService;
	@EJB(name = "services/ProductService")
	private ProductService prdService;
	
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
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Questionnaire> questionnaires = qstService.findAllQuestionnaire();
		List<Product> products = prdService.findAllProductIds();
		
		String path = "WEB-INF/AdminPage.html";
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		ctx.setVariable("topquestionnaires", questionnaires);
		ctx.setVariable("products", products);
		
		templateEngine.process(path, ctx, response.getWriter());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
}
