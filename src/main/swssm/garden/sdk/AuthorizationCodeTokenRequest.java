package swssm.garden.sdk;

import java.util.Collection;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.google.api.client.util.Key;


/**
*
*  Authorization Code을 이용하여  Access Token을 얻기 위하여 request 보내는 클래스 
*  
* 
* @author SW
* @version 1.0
*
*
*/

public class AuthorizationCodeTokenRequest extends TokenRequest{

	/** Authorization Server로부터 받아온 Authorization code*/
	@Key
	private String code;
	/**
	 * redirect uri
	 */
	@Key("redirect_uri")
	private String redirectUri;
	
	
	/**
	 * 
	 * @param transport
	 * @param jsonFactory
	 * @param tokenServerUrl
	 * @param code
	 */
	public AuthorizationCodeTokenRequest(HttpTransport transport,
			JsonFactory jsonFactory, GenericUrl tokenServerUrl, String code) {
		super(transport, jsonFactory, new GenericUrl("http://211.189.127.73:8000/o/token/"), "authorization_code");
		setCode(code);
	}
	@Override
	public AuthorizationCodeTokenRequest setRequestInitializer(HttpRequestInitializer requestInitializer){
		return (AuthorizationCodeTokenRequest) super.setRequestInitializer(requestInitializer);
	}
	@Override
	public AuthorizationCodeTokenRequest setScopes(Collection<String> scopes){
		return (AuthorizationCodeTokenRequest) super.setScopes(scopes);
	}
	@Override
	public AuthorizationCodeTokenRequest setGrantType(String grantType){
		return (AuthorizationCodeTokenRequest) super.setGrantType(grantType);
	}
	@Override
	public AuthorizationCodeTokenRequest setClientAuthentication(HttpExecuteInterceptor clientAuthentication){
		return (AuthorizationCodeTokenRequest) super.setClientAuthentication(clientAuthentication);
	}

	/**
	 * code를 리턴 
	 * @return code
	 */
	public final String getCode(){
		return code;
	}
	/**
	 * code를 셋팅 
	 * @param code
	 * @return code
	 */
	private AuthorizationCodeTokenRequest setCode(String code) {
		this.code = Preconditions.checkNotNull(code);
		return this;
	}
	/**
	 * redirect uri를 리턴 
	 * @return redirectUri
	 */
	public final String getRedirectUri(){
		return redirectUri;
	}
	/**
	 * redirect uri를 셋팅 
	 * @param redirectUri
	 * @return redirectUri
	 */
	public AuthorizationCodeTokenRequest setRedirectUri(String redirectUri){
		this.redirectUri = redirectUri;
		return this;
	}
	
	@Override
	public AuthorizationCodeTokenRequest set(String fieldName, Object value){
		return (AuthorizationCodeTokenRequest) super.set(fieldName, value);
	}
	

}
