package swssm.garden.sdk;

import java.util.Collection;


@Deprecated
public class BrowserClientRequestUrl  extends AuthorizationRequestUrl{

	@Override
	public BrowserClientRequestUrl setResponseTypes(
			Collection<String> responseTypes) {
		return (BrowserClientRequestUrl) super.setResponseTypes(responseTypes);
	}

	@Override
	public BrowserClientRequestUrl setRedirectUri(String redirectUri) {
		return (BrowserClientRequestUrl) super.setRedirectUri(redirectUri);
	}

	@Override
	public BrowserClientRequestUrl setScopes(Collection<String> scopes) {
		return (BrowserClientRequestUrl) super.setScopes(scopes);
	}

	@Override
	public BrowserClientRequestUrl setClientId(String clientId) {
		return (BrowserClientRequestUrl) super.setClientId(clientId);
	}

	@Override
	public BrowserClientRequestUrl setState(String state) {
		return (BrowserClientRequestUrl) super.setState(state);
	}

	@Override
	public BrowserClientRequestUrl set(String fieldName, Object value) {
		return (BrowserClientRequestUrl) super.set(fieldName, value);
	}

	@Override
	public BrowserClientRequestUrl clone() {
		return (BrowserClientRequestUrl) super.clone();
	}

	public BrowserClientRequestUrl(String authorizationServerEncodedUrl,
			String clientId, Collection<String> responseTypes) {
		super(authorizationServerEncodedUrl, clientId, responseTypes,"force");
	}
	
	

}
