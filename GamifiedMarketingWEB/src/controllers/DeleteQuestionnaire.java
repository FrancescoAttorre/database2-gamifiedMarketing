package controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.QuestionnaireService;

@WebServlet("/DeleteQuestionnaire")
public class DeleteQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	@EJB(name = "services/QuestionnaireService")
	private QuestionnaireService qService;
	
	public DeleteQuestionnaire() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}

	//TODO : fix delete questionnaire action

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int qid;
		qid = Integer.parseInt(request.getParameter("questionnaireid"));
		qService.deleteQuestionnaire(qid);
		String ctxpath = getServletContext().getContextPath();
		String path = ctxpath + "/GoToAdminPage";
		response.sendRedirect(path);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
	
	public void destroy() {
	}
}
