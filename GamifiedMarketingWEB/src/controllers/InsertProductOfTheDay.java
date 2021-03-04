package controllers;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import entities.Product;
import services.ProductService;
import services.QuestionnaireService;

@WebServlet("/InsertProductOfTheDay")
public class InsertProductOfTheDay extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;

	@EJB(name = "services/QuestionnaireService")
	private QuestionnaireService qstService;

	public InsertProductOfTheDay() {
		super();
	}

	/*
	 * public void init() throws ServletException { ServletContext servletContext =
	 * getServletContext(); ServletContextTemplateResolver templateResolver = new
	 * ServletContextTemplateResolver(servletContext);
	 * templateResolver.setTemplateMode(TemplateMode.HTML); this.templateEngine =
	 * new TemplateEngine();
	 * this.templateEngine.setTemplateResolver(templateResolver);
	 * templateResolver.setSuffix(".html"); }
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		LocalDate date = null;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		date = LocalDate.parse(request.getParameter("date").toString(), formatter);

		int productid = Integer.parseInt(request.getParameter("productId"));

		Integer questionnaireId = qstService.createQuestionnaire(productid, LocalDateTime.of(date, LocalTime.now()));
		
		//TODO: complete with errors
		//if(questionnaireId == null) //cannot insert
		
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToAdminPage";
		response.sendRedirect(path);

	}

}
