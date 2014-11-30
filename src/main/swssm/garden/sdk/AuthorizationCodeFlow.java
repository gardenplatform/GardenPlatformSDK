package swssm.garden.sdk;

import java.util.Collection;
import java.util.Collections;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Lists;

/**
 * 
 * @author Garden
 */
public class AuthorizationCodeFlow {

	/**
	 * HTTP Transport
	 */
	private final HttpTransport transport;
	/**
	 * JSON factory
	 */
	private final JsonFactory jsonFactory;
	/**
	 * Token Server Encoded URL
	 */
	private final String tokenServerEncodedUrl;
	/**
	 * Client Authentication
	 */
	private final HttpExecuteInterceptor clientAuthentication;
	/**
	 * clientId
	 */
	private final String clientId;
	// /// approvalPromt
	private final String approvalPrompt;
	// /// Authorization Server Encoded URL
	private final String authorizationServerEncodedUrl;
	// /// HTTP Request Initializer
	private final HttpRequestInitializer requestInitializer;
	// /// Credential을 위한 시간
	private final Clock clock;
	// /// 범위
	private final Collection<String> scopes;

	public AuthorizationCodeFlow( HttpTransport transport,
			JsonFactory jsonFactory,HttpExecuteInterceptor clientAuthentication, String clientId) {
		this(new Builder(transport, jsonFactory,
				clientAuthentication, clientId));
	}

	protected AuthorizationCodeFlow(Builder builder) {
		transport = Preconditions.checkNotNull(builder.transport);
		jsonFactory = Preconditions.checkNotNull(builder.jsonFactory);
		tokenServerEncodedUrl = "http://211.189.127.73:8000/o/token/";
		clientAuthentication = builder.clientAuthentication;
		clientId = Preconditions.checkNotNull(builder.clientId);
		authorizationServerEncodedUrl = "http://211.189.127.73:8000/o/authorize";
		requestInitializer = builder.requestInitializer;
		scopes = Collections.unmodifiableCollection(builder.scopes);
		clock = Preconditions.checkNotNull(builder.clock);
		approvalPrompt = builder.approvalPrompt;
	}

	public AuthorizationCodeRequestUrl newAuthorizationUrl() {
		return (AuthorizationCodeRequestUrl) new AuthorizationCodeRequestUrl(authorizationServerEncodedUrl,
				clientId).setScopes(scopes).setApprovalPrompt(approvalPrompt);
	}

	public AuthorizationCodeTokenRequest newTokenRequest(
			String authorizationCode) {
		return new AuthorizationCodeTokenRequest(transport, jsonFactory,
				new GenericUrl("http://211.189.127.73:8000/o/token/"), authorizationCode)
				.setClientAuthentication(clientAuthentication)
				.setRequestInitializer(requestInitializer).setScopes(scopes);
	}

	public final HttpTransport getTransport() {
		return transport;
	}

	public final JsonFactory getJsonFactory() {
		return jsonFactory;
	}

	public final String getTokenServerEncodedUrl() {
		return tokenServerEncodedUrl;
	}

	public final HttpExecuteInterceptor getClientAuthentication() {
		return clientAuthentication;
	}

	public final String getClientId() {
		return clientId;
	}

	public final String getAuthorizationServerEncodedUrl() {
		return authorizationServerEncodedUrl;
	}

	public final HttpRequestInitializer getRequestInitializer() {
		return requestInitializer;
	}

	public final String getScopesAsString() {
		return Joiner.on(' ').join(scopes);
	}

	public final Collection<String> getScopes() {
		return scopes;
	}

	public final Clock getClock() {
		return clock;
	}
	
	public final String getApprovalPrompt() {
		return approvalPrompt;
	}

	public static class Builder {

		HttpTransport transport;

		JsonFactory jsonFactory;

		GenericUrl tokenServerUrl;

		HttpExecuteInterceptor clientAuthentication;

		String clientId;
		
		String approvalPrompt;

		String authorizationServerEncodedUrl;

		HttpRequestInitializer requestInitializer;

		Collection<String> scopes = Lists.newArrayList();

		Clock clock = Clock.SYSTEM;

		public Builder(HttpTransport transport,
				JsonFactory jsonFactory,HttpExecuteInterceptor clientAuthentication, String clientId) {
			setTransport(transport);
			setJsonFactory(jsonFactory);
			setTokenServerUrl(new GenericUrl("http://211.189.127.73:8000/o/token/"));
			setClientAuthentication(clientAuthentication);
			setClientId(clientId);
			setAuthorizationServerEncodedUrl("http://211.189.127.73:8000/o/authorize");
			setApprovalPrompt(approvalPrompt);
		}

		// /// authorization code flow 빌더에 의해 새로운 instance가 생김
		public AuthorizationCodeFlow build() {
			return new AuthorizationCodeFlow(this);
		}

		public final HttpTransport getTransport() {
			return transport;
		}

		public Builder setTransport(HttpTransport transport) {
			this.transport = Preconditions.checkNotNull(transport);
			return this;
		}

		public final JsonFactory getJsonFactory() {
			return jsonFactory;
		}

		public Builder setJsonFactory(JsonFactory jsonFactory) {
			this.jsonFactory = Preconditions.checkNotNull(jsonFactory);
			return this;
		}

		public final GenericUrl getTokenServerUrl() {
			return tokenServerUrl;
		}

		public Builder setTokenServerUrl(GenericUrl tokenServerUrl) {
			this.tokenServerUrl = Preconditions.checkNotNull(tokenServerUrl);
			return this;
		}

		public final HttpExecuteInterceptor getClientAuthentication() {
			return clientAuthentication;
		}

		public Builder setClientAuthentication(
				HttpExecuteInterceptor clientAuthentication) {
			this.clientAuthentication = clientAuthentication;
			return this;
		}

		public final String getClientId() {
			return clientId;
		}

		public Builder setClientId(String clientId) {
			this.clientId = Preconditions.checkNotNull(clientId);
			return this;
		}

		public final String getAuthorizationServerEncodedUrl() {
			return authorizationServerEncodedUrl;
		}

		public Builder setAuthorizationServerEncodedUrl(
				String authorizationServerEncodedUrl) {
			this.authorizationServerEncodedUrl = Preconditions
					.checkNotNull(authorizationServerEncodedUrl);
			return this;
		}

		
		public final Clock getClock() {
			return clock;
		}

		public Builder setClock(Clock clock) {
			this.clock = Preconditions.checkNotNull(clock);
			return this;
		}

		public final HttpRequestInitializer getRequestInitializer() {
			return requestInitializer;
		}

		public Builder setRequestInitializer(
				HttpRequestInitializer requestInitializer) {
			this.requestInitializer = requestInitializer;
			return this;
		}

		public Builder setScopes(Collection<String> scopes) {
			this.scopes = Preconditions.checkNotNull(scopes);
			return this;
		}

		public final Collection<String> getScopes() {
			return scopes;
		}
		
		public Builder setApprovalPrompt(String approvalPrompt) {
			this.approvalPrompt = approvalPrompt;
			return this;
		}

		public final String getApprovalPrompt() {
			return approvalPrompt;
		}

	}

}
