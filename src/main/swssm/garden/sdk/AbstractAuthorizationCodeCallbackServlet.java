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

/**
 * AbstractAuthorizationCodeServlet에 대한 콜백함수로,
 * Authorization Code를 기반으로 Access Token을 얻기 위한 서블릿 클래스 
 * @author Garden
 *
 */
public abstract class AbstractAuthorizationCodeCallbackServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private final Lock lock = new ReentrantLock();

	private AuthorizationCodeFlow flow;

	/**
	 * Authorization Code를 기반으로 Request를 보내 Access Token을 얻고, Access Token을 쎄션에 저장. 
	 */
	@Override
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		StringBuffer buf = req.getRequestURL();
		if (req.getQueryString() != null) {
			buf.append('?').append(req.getQueryString());
		}
		AuthorizationCodeResponseUrl responseUrl = new AuthorizationCodeResponseUrl(buf.toString());
		String code = responseUrl.getCode();
		if (responseUrl.getError() != null) {
			onError(req, resp, responseUrl);
		} else if (code == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			resp.getWriter().print(" authorization code");
		} else {
			lock.lock();
			try {
				if (flow == null) {
					flow = initializeFlow();
				}
				String redirectUri = getRedirectUri(req);
				TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
				HttpSession session = req.getSession();
				session.setAttribute("access_token", response.getAccessToken());
				session.setMaxInactiveInterval(3000000);
				System.out.println(session.getAttribute("access_token"));
				onSuccess(req, resp, response);
			} finally {
				lock.unlock();
			}
		}
	}
	/**
	 * Oauth를 위한 파라미터 초기화
	 * @return flow
	 * @throws ServletException
	 * @throws IOException
	 */
	protected abstract AuthorizationCodeFlow initializeFlow()
			throws ServletException, IOException;
	  /**
	   * redirectUri를 리턴 
	   * @param req
	   * @throws ServletException
	   * @throws IOException
	   */
	protected abstract String getRedirectUri(HttpServletRequest req)
			throws ServletException, IOException;

	/**
	 * Oauth 인증 과정이 성공 했을 시 실행되는 메소드 
	 * @param req
	 * @param resp
	 * @param tokenResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void onSuccess(HttpServletRequest req, HttpServletResponse resp,	TokenResponse tokenResponse) throws ServletException, IOException {	}
	/**
	 * Oauth 인증 과정이 실패 했을 시 실행되는 메소드 
	 * @param req
	 * @param resp
	 * @param errorResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void onError(HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse) throws ServletException, IOException {	}
	/**
	 * 성공적인 인증과정 후 지정한 URL로 redirect보내는 메소드 
	 * @param req
	 * @param resp
	 * @param url
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void redirectAfterSuccessAuth(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
		System.out.println("redirect success");
		PrintWriter out = resp.getWriter();
	    out.println("<html><body>");
	    out.println("<script type=\"text/javascript\">");
	    out.println("if(window.opener == null) {");
	    out.println("window.location.href= " +" ' " + url + " ' ");
	    out.println("} else {");
	    out.println("window.close();");
	    out.println("window.opener.location.href = " +" ' " + url +  " '  }");
	    out.println("</script>");
	    out.println("</body></html>");
	}
	/**
	 * 인증과정이 실패한 이 후 지정한 URL로 redirect보내는 메소드 
	 * @param req
	 * @param resp
	 * @param url
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void redirectAfterErrorAuth(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
	    out.println("<html><body>");
	    out.println("<script type=\"text/javascript\">");
	    out.println("if(window.opener == null) {");
	    out.println("window.location.href= " +" ' " + url + " ' ");
	    out.println("} else {");
	    out.println("window.close();");
	    out.println("window.opener.location.href = " +" ' " + url +  " '  }");
	    out.println("</script>");
	    out.println("</body></html>");

	}
	
}