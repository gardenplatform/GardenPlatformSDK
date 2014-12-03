package swssm.garden.sdk;

import java.util.Collection;
import java.util.Collections;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Lists;

/**
 * Authorization Code를 이용하여 인받는 플로우을 쉽게하는 클래스 
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
	/**
	 * approvalPromt
	 */
	private final String approvalPrompt;
	/**
	 * Authorization Server Encoded URL
	 */
	private final String authorizationServerEncodedUrl;
	/**
	 * HTTP Request Initializer
	 */
	private final HttpRequestInitializer requestInitializer;
	/**
	 * scope
	 */
	private final Collection<String> scopes;
/**
 * 
 * @param clientAuthentication
 * @param clientId
 */
	public AuthorizationCodeFlow( HttpExecuteInterceptor clientAuthentication, String clientId) {
		this(new Builder(clientAuthentication, clientId));
	}

	/**
	 * 빌더를 이용하여 셋팅 
	 * @param builder
	 */
	protected AuthorizationCodeFlow(Builder builder) {
		transport = new NetHttpTransport();
		jsonFactory = new JacksonFactory();
		tokenServerEncodedUrl = "http://211.189.127.73:8000/o/token/";
		clientAuthentication = builder.clientAuthentication;
		clientId = Preconditions.checkNotNull(builder.clientId);
		authorizationServerEncodedUrl = "http://211.189.127.73:8000/o/authorize";
		requestInitializer = builder.requestInitializer;
		scopes = Collections.unmodifiableCollection(builder.scopes);
		approvalPrompt = builder.approvalPrompt;
	}
	/**
	 * AuthorizationCodeRequestUrl을 생성 
	 * @return AuthorizationCodeRequestUrl
	 */
	public AuthorizationCodeRequestUrl newAuthorizationUrl() {
		return (AuthorizationCodeRequestUrl) new AuthorizationCodeRequestUrl(authorizationServerEncodedUrl,
				clientId).setScopes(scopes).setApprovalPrompt(approvalPrompt);
	}
	/**
	 * Access Token을 얻기 위한 Request를 생
	 * @param authorizationCode
	 * @return AuthorizationCodeTokenRequest
	 */
	public AuthorizationCodeTokenRequest newTokenRequest(
			String authorizationCode) {
		return new AuthorizationCodeTokenRequest(transport, jsonFactory,
				new GenericUrl("http://211.189.127.73:8000/o/token/"), authorizationCode)
				.setClientAuthentication(clientAuthentication)
				.setRequestInitializer(requestInitializer).setScopes(scopes);
	}
	/**
	 * transport를 리턴
	 * @return transport
	 */
	public final HttpTransport getTransport() {
		return transport;
	}
	/**
	 * jsonFactory를 리턴 
	 * @return jsonFactory
	 */
	public final JsonFactory getJsonFactory() {
		return jsonFactory;
	}
	/**
	 * token server encoded url을 리턴 
	 * @return tokenServerEncodedUrl
	 */
	public final String getTokenServerEncodedUrl() {
		return tokenServerEncodedUrl;
	}

	/**
	 * clientAuthentication를 리턴 
	 * @return clientAuthentication
	 */
	public final HttpExecuteInterceptor getClientAuthentication() {
		return clientAuthentication;
	}
	/**
	 * client id를 리턴 
	 * @return clientId
	 */
	public final String getClientId() {
		return clientId;
	}
	/**
	 * authorizationServerEncodedUrl을 리턴 
	 * @return authorizationServerEncodedUrl
	 */
	public final String getAuthorizationServerEncodedUrl() {
		return authorizationServerEncodedUrl;
	}
	/**
	 * requestInitializer를 리턴 
	 * @return requestInitializer
	 */
	public final HttpRequestInitializer getRequestInitializer() {
		return requestInitializer;
	}
	/**
	 * Scopes를 String 타입으로 리턴 
	 * @return String타입의 scopes
	 */
	public final String getScopesAsString() {
		return Joiner.on(' ').join(scopes);
	}
	/**
	 * Scopes를 리턴 
	 * @return scopes
	 */
	public final Collection<String> getScopes() {
		return scopes;
	}

	/**
	 * approval prompt를 리턴 
	 * @return approvalPrompt
	 */
	public final String getApprovalPrompt() {
		return approvalPrompt;
	}

	/**
	 * AuthorizationCodeFlow를 쉽게 만들기 위한 빌더 클래스 
	 * @author Garden
	 *
	 */
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

		/**
		 * 
		 * @param clientAuthentication
		 * @param clientId
		 */
		public Builder(HttpExecuteInterceptor clientAuthentication, String clientId) {
			setTransport(new NetHttpTransport());
			setJsonFactory(new JacksonFactory());
			setTokenServerUrl(new GenericUrl("http://211.189.127.73:8000/o/token/"));
			setClientAuthentication(clientAuthentication);
			setClientId(clientId);
			setAuthorizationServerEncodedUrl("http://211.189.127.73:8000/o/authorize");
			setApprovalPrompt(approvalPrompt);
		}

		/**
		 * authorization code flow 빌더에 의해 새로운 instance를 만듦.
		 */
		public AuthorizationCodeFlow build() {
			return new AuthorizationCodeFlow(this);
		}
		/**
		 * transport를 리턴 
		 * @return transport
		 */
		public final HttpTransport getTransport() {
			return transport;
		}

		/**
		 * transport를 셋팅 
		 * @param transport
		 * @return transport
		 */
		public Builder setTransport(HttpTransport transport) {
			this.transport = Preconditions.checkNotNull(transport);
			return this;
		}
		/**
		 * jsonFactory를 리턴 
		 * @return jsonFactory
		 */
		public final JsonFactory getJsonFactory() {
			return jsonFactory;
		}
		/**
		 * jsonFactory를 셋팅 
		 * @param jsonFactory
		 * @return jsonFactory
		 */
		public Builder setJsonFactory(JsonFactory jsonFactory) {
			this.jsonFactory = Preconditions.checkNotNull(jsonFactory);
			return this;
		}
		/**
		 * Token Server Url을 리턴 
		 * @return tokenServerUrl
		 */
		public final GenericUrl getTokenServerUrl() {
			return tokenServerUrl;
		}
		/**
		 * Token Server Url을 셋팅  
		 * @param tokenServerUrl
		 * @return tokenServerUrl
		 */
		public Builder setTokenServerUrl(GenericUrl tokenServerUrl) {
			this.tokenServerUrl = Preconditions.checkNotNull(tokenServerUrl);
			return this;
		}
		/**
		 * clientAuthentication를 리턴 
		 * @return clientAuthentication
		 */
		public final HttpExecuteInterceptor getClientAuthentication() {
			return clientAuthentication;
		}
		/**
		 * clientAuthentication를 셋팅 
		 * @param clientAuthentication
		 * @return clientAuthentication
		 */
		public Builder setClientAuthentication(
				HttpExecuteInterceptor clientAuthentication) {
			this.clientAuthentication = clientAuthentication;
			return this;
		}

		/**
		 * Client Id를 리턴 
		 * @return clientId
		 */
		public final String getClientId() {
			return clientId;
		}
		/**
		 * Client Id를 셋팅 
		 * @param clientId
		 * @return clientId 
		 */
		public Builder setClientId(String clientId) {
			this.clientId = Preconditions.checkNotNull(clientId);
			return this;
		}

		/**
		 * authorizationServerEncodedUrl를 리턴 
		 * @return authorizationServerEncodedUrl
		 */
		public final String getAuthorizationServerEncodedUrl() {
			return authorizationServerEncodedUrl;
		}
		/**
		 * authorizationServerEncodedUrl를 셋팅 
		 * @param authorizationServerEncodedUrl
		 * @return authorizationServerEncodedUrl
		 */
		public Builder setAuthorizationServerEncodedUrl(
				String authorizationServerEncodedUrl) {
			this.authorizationServerEncodedUrl = Preconditions
					.checkNotNull(authorizationServerEncodedUrl);
			return this;
		}

		/**
		 * requestInitializer를 리턴 
		 * @return requestInitializer
		 */
		public final HttpRequestInitializer getRequestInitializer() {
			return requestInitializer;
		}
		/**
		 * requestInitializer를 셋팅 
		 * @param requestInitializer
		 * @return requestInitializer
		 */
		public Builder setRequestInitializer(
				HttpRequestInitializer requestInitializer) {
			this.requestInitializer = requestInitializer;
			return this;
		}

		/**
		 * scopes를 셋팅  
		 * @param scopes
		 * @return scopes
		 */
		public Builder setScopes(Collection<String> scopes) {
			this.scopes = Preconditions.checkNotNull(scopes);
			return this;
		}
		/**
		 * scopes를 리턴 
		 * @return scopes
		 */
		public final Collection<String> getScopes() {
			return scopes;
		}
		/**
		 * approval prompt를 셋팅 
		 * @param approvalPrompt
		 * @return approvalPrompt
		 */
		public Builder setApprovalPrompt(String approvalPrompt) {
			this.approvalPrompt = approvalPrompt;
			return this;
		}
		/**
		 * approval promt를 리턴 
		 * @return approvalPrompt
		 * 
		 */
		public final String getApprovalPrompt() {
			return approvalPrompt;
		}

	}

}
