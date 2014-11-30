package swssm.garden.sdk;

import java.util.List;
import java.util.regex.Pattern;

import com.google.api.client.http.HttpRequest;

/**
 * 
 * 
 * 보호된 리소스를 접근하기 위한 Bearer Token 클래스
 * 
 * @author Garden
 * @version 1.0
 * 
 */
@Deprecated
public class BearerToken {
	
	/** Query를 보내기 위한 파라미터 이름 */
	static final String PARAM_NAME = "access_token";
	
	  /** refresh token요청을 할 때 사하기 위한 에러패턴 */
	  static final Pattern INVALID_TOKEN_ERROR =
	      Pattern.compile("\\s*error\\s*=\\s*\"?invalid_token\"?");
	  
	/**
	 * HTTP Authorization 헤더를 이용하여 보호된 리소스에 접근하는 경우 메소드
	 */
	static final class AuthorizationHeaderAccessMethod implements Credential.AccessMethod {
		
		/** Authorization header prefix */
		static final String HEADER_PREFIX = "Bearer";
		
		AuthorizationHeaderAccessMethod(){}

		
		public void intercept(HttpRequest request, String accessToken){
			request.getHeaders().setAuthorization(HEADER_PREFIX + accessToken);
		}

		public String getAccessTokenFromRequest(HttpRequest request) {
			List<String> authorizationAsList = request.getHeaders().getAuthenticateAsList();
			if(authorizationAsList != null){
				for(String header : authorizationAsList){
					if(header.startsWith(HEADER_PREFIX)){
						return header.substring(HEADER_PREFIX.length());
					}
				}
			}
			return null;
		}
	}

	/**
	 * 
	 * @return HTTP Authorization 헤더를 이용하여 보호된 리소스에 접근하는 경우 메소드
	 */
	public static Credential.AccessMethod authorizationHeaderAccessMethod(){
		return new AuthorizationHeaderAccessMethod();
	}
	
	/**************
	 사용 X
	///// form-urlencoded 방식으로 보호된 리소스에 접근하는 경우 메소드 ( key=value&key=value )
	static final class FormEncodedBodyAccessMethod implements Credential.AccessMethod{

		FormEncodedBodyAccessMethod(){
		}

		public static Map<String, Object> getData(HttpRequest request){
			return Data.mapOf(UrlEncodedContent.getContent(request).getData());
		}
		
		public void intercept(HttpRequest request, String accessToken){
			Preconditions.checkArgument(!HttpMethods.GET.equals(request.getRequestMethod()),
					"HTTP GET method is not supoorted");
			getData(request).put(PARAM_NAME,accessToken);
		}

		public String getAccessTokenFromRequest(HttpRequest request) {
			Object bodyParam = getData(request).get(PARAM_NAME);
			return bodyParam == null ? null : bodyParam.toString();
		}
	}
	
	
	///// URI Query Parameter를 이용하여 보호된 리소스에 접근하는 경우 메소드
	static final class QueryParameterAccessMethod implements Credential.AccessMethod{
		
		QueryParameterAccessMethod(){
		}
		
		public void intercept(HttpRequest request, String accessToken) throws IOException{
			request.getUrl().set(PARAM_NAME,accessToken);
		}
		public String getAccessTokenFromRequest(HttpRequest request) {
			Object param = request.getUrl().get(PARAM_NAME);
			return param == null ? null : param.toString();
		}
	}

	public static Credential.AccessMethod FormEncodedBodyAccessMethod(){
		return new FormEncodedBodyAccessMethod();
	}

	public static Credential.AccessMethod QueryParameterAccessMethod(){
		return new QueryParameterAccessMethod();
	}
	*/
}
