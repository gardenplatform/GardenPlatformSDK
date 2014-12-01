package swssm.garden.sdk;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import swssm.garden.sdk.AuthorizationCodeFlow;
import swssm.garden.sdk.AuthorizationCodeRequestUrl;

public abstract class AbstractAuthorizationCodeServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	  private final Lock lock = new ReentrantLock();

	  private AuthorizationCodeFlow flow;
	  
	  @Override
	  protected void service(HttpServletRequest req, HttpServletResponse resp)
	      throws IOException, ServletException {
		  
	    lock.lock();
	    try {
	    	flow = initializeFlow();
	      	HttpSession session = req.getSession();
			String accessToken = (String)session.getAttribute("access_token");
			
	      // Authorization Flow로 Redirect
	      AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
	      authorizationUrl.setRedirectUri(getRedirectUri(req));
	   
	      onAuthorization(req, resp, authorizationUrl);
	    } finally {
	      lock.unlock();
	    }
	  }

	  protected abstract AuthorizationCodeFlow initializeFlow() throws ServletException, IOException;

	  protected abstract String getRedirectUri(HttpServletRequest req)
	      throws ServletException, IOException;

	  protected void onAuthorization(HttpServletRequest req, HttpServletResponse resp,
	      AuthorizationCodeRequestUrl authorizationUrl) throws ServletException, IOException {
	    PrintWriter out = resp.getWriter();
	      out.println("<html><body>");
		    out.println("<script type=\"text/javascript\">");
		    out.println("url = '" +authorizationUrl.build() +"'");
		    out.println("newwindow=window.open(url,'name','height=600,width=400');");
		    out.println("if (window.focus) {newwindow.focus()}");
		    out.println("</script>");
		    out.println("</body></html>");
	  }
}
