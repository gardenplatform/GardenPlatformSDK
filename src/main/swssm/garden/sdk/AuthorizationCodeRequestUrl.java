package swssm.garden.sdk;

/**
 * 
 *  Authorization Code를 얻기 위한 Request URL을 만들어주는 클래스
 *  
 *  
 *  @author SW
 *  @version 1.0
 *  
 *  */
import java.util.Collection;
import java.util.Collections;


public class AuthorizationCodeRequestUrl extends AuthorizationRequestUrl{

	@Override
	public AuthorizationRequestUrl setApprovalPrompt(String approvalPrompt) {
		return (AuthorizationCodeRequestUrl)super.setApprovalPrompt(approvalPrompt);
	}

	@Override
	public AuthorizationCodeRequestUrl setResponseTypes(
			Collection<String> responseTypes) {
		return (AuthorizationCodeRequestUrl) super.setResponseTypes(responseTypes);
	}

	@Override
	public AuthorizationCodeRequestUrl setRedirectUri(String redirectUri) {
		return (AuthorizationCodeRequestUrl) super.setRedirectUri(redirectUri);
	}

	@Override
	public AuthorizationCodeRequestUrl setScopes(Collection<String> scopes) {
		return (AuthorizationCodeRequestUrl) super.setScopes(scopes);
	}

	@Override
	public AuthorizationCodeRequestUrl setClientId(String clientId) {
		return (AuthorizationCodeRequestUrl) super.setClientId(clientId);
	}

	@Override
	public AuthorizationCodeRequestUrl setState(String state) {
		return (AuthorizationCodeRequestUrl) super.setState(state);
	}

	@Override
	public AuthorizationCodeRequestUrl set(String fieldName, Object value) {
		return (AuthorizationCodeRequestUrl) super.set(fieldName, value);
	}

	@Override
	public AuthorizationCodeRequestUrl clone() {
		return (AuthorizationCodeRequestUrl) super.clone();
	}

	public AuthorizationCodeRequestUrl(String authorizationServerEncodedUrl,
			String clientId) {
		super(authorizationServerEncodedUrl, clientId,Collections.singleton("code"), "force");
	}
	
}
