package swssm.garden.sdk;

import java.util.Collection;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

/**
 * 
 *  
 *  Refresh Token을 얻기위한 Request 클래스 
 * 
 * @author SW
 * @version 1.0
 * 
 * */
@Deprecated
public class RefreshTokenRequest extends TokenRequest{

	@Key("refresh_token")
	private String refreshToken;

	@Override
	public RefreshTokenRequest setRequestInitializer(
			HttpRequestInitializer requestInitializer) {
		return (RefreshTokenRequest) super.setRequestInitializer(requestInitializer);
	}

	@Override
	public RefreshTokenRequest setClientAuthentication(
			HttpExecuteInterceptor clientAuthentication) {
		return (RefreshTokenRequest) super.setClientAuthentication(clientAuthentication);
	}

	@Override
	public RefreshTokenRequest setGrantType(String grantType) {
		return (RefreshTokenRequest) super.setGrantType(grantType);
	}

	@Override
	public RefreshTokenRequest setScopes(Collection<String> scopes) {
		return (RefreshTokenRequest) super.setScopes(scopes);
	}
	@Override
	public RefreshTokenRequest set(String fieldName, Object value) {
		return (RefreshTokenRequest) super.set(fieldName, value);
	}
	

	public RefreshTokenRequest(HttpTransport transport,
			JsonFactory jsonFactory, GenericUrl tokenServerUrl, String refreshToken) {
		super(transport, jsonFactory, tokenServerUrl, "refresh_token");
		setRefreshToken(refreshToken);
	}
	
	public RefreshTokenRequest setRefreshToken(String refreshToken){
		this.refreshToken = Preconditions.checkNotNull(refreshToken);
		return this;
	}

}
