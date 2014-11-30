package swssm.garden.sdk;

import java.io.IOException;
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
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;

import swssm.garden.sdk.Credential.AccessMethod;

/**
 * 사용자의 Credential을 저장하고 관리하기 위한 Authorization Code Flow
 * @author Garden
 */
public class AuthorizationCodeFlow {

	/**
	 * resource server에 알리는 access token의 method (BearerToken)
	 */
	private final AccessMethod method;
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
	// /// Credentail Data 저장
	private final DataStore<StoredCredential> credentialDataStore;
	// /// HTTP Request Initializer
	private final HttpRequestInitializer requestInitializer;
	// /// Credential을 위한 시간
	private final Clock clock;
	// /// 범위
	private final Collection<String> scopes;
	// /// Credential Created Listener
	private final CredentialCreatedListener credentialCreatedListener;
	// /// 클라이언트에게 제공되는 Refresh Listener
	private final Collection<CredentialRefreshListener> refreshListeners;

	public AuthorizationCodeFlow(AccessMethod method, HttpTransport transport,
			JsonFactory jsonFactory,HttpExecuteInterceptor clientAuthentication, String clientId) {
		this(new Builder(method, transport, jsonFactory,
				clientAuthentication, clientId));
	}

	protected AuthorizationCodeFlow(Builder builder) {
		method = Preconditions.checkNotNull(builder.method);
		transport = Preconditions.checkNotNull(builder.transport);
		jsonFactory = Preconditions.checkNotNull(builder.jsonFactory);
		tokenServerEncodedUrl = "http://211.189.127.73:8000/o/token/";
		clientAuthentication = builder.clientAuthentication;
		clientId = Preconditions.checkNotNull(builder.clientId);
		authorizationServerEncodedUrl = "http://211.189.127.73:8000/o/authorize";
		requestInitializer = builder.requestInitializer;
		credentialDataStore = builder.credentialDataStore;
		scopes = Collections.unmodifiableCollection(builder.scopes);
		clock = Preconditions.checkNotNull(builder.clock);
		credentialCreatedListener = builder.credentialCreatedListener;
		approvalPrompt = builder.approvalPrompt;
		refreshListeners = Collections.unmodifiableCollection(builder.refreshListeners);
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

	// /// 주어진 User ID에 따른 새로운 Credential을 생성하는 클래스
	@SuppressWarnings("deprecation")
	public Credential createAndStoreCredential(TokenResponse response, String clientId) throws IOException {
		Credential credential = newCredential(clientId).setFromTokenResponse(response);
		if (credentialDataStore != null) {
			credentialDataStore.set(clientId, new StoredCredential(credential));
		}
		if (credentialCreatedListener != null) {
			credentialCreatedListener.onCredentialCreated(credential, response);
		}
		return credential;
	}

	// /// Client ID를 이용하여 Credential을 Credential이 저장된 곳에서 불러옴. 
	@SuppressWarnings("deprecation")
	public Credential loadCredential(String clientId) throws IOException {
		
		if (credentialDataStore == null) {
			System.out.println("Credential Store is Empty");
			return null;
		}
		Credential credential = newCredential(clientId);
		if (credentialDataStore != null) {
			StoredCredential stored = credentialDataStore.get(clientId);
			System.out.println("Load Credential from Credential Store for ID : " + stored);
			if (stored == null) {
				return null;
			}
			credential.setAccessToken(stored.getAccessToken());
			credential.setRefreshToken(stored.getRefreshToken());
			credential.setExpirationTimeMilliseconds(stored.getExpirationTimeMilliseconds());
		}
		return credential;
	}

	// /// Client ID 기반 새로운 Credential Instance를 만듬. 
	@SuppressWarnings("deprecation")
	private Credential newCredential(String clientId) {
		Credential.Builder builder = new Credential.Builder(method)
				.setTransport(transport).setJsonFactory(jsonFactory)
				.setTokenServerEncodedUrl("http://211.189.127.73:8000/o/token/")
				.setClientAuthentication(clientAuthentication)
				.setRequestInitializer(requestInitializer).setClock(clock);
		if (credentialDataStore != null) {
			builder.addRefreshListener(new DataStoreCredentialRefreshListener(clientId, credentialDataStore));
		}
		builder.getRefreshListeners().addAll(refreshListeners);
		return builder.build();
	}

	public final AccessMethod getMethod() {
		return method;
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

	public final DataStore<StoredCredential> getCredentialDataStore() {
		return credentialDataStore;
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

	public final Collection<CredentialRefreshListener> getRefreshListeners() {
		return refreshListeners;
	}

	// /// Typical use is to parse additional fields from the credential
	// created, such as an ID token.
	public interface CredentialCreatedListener {
		void onCredentialCreated(Credential credential,
				TokenResponse tokenResponse) throws IOException;
	}

	public static class Builder {

		AccessMethod method;

		HttpTransport transport;

		JsonFactory jsonFactory;

		GenericUrl tokenServerUrl;

		HttpExecuteInterceptor clientAuthentication;

		String clientId;
		
		String approvalPrompt;

		String authorizationServerEncodedUrl;

		DataStore<StoredCredential> credentialDataStore;

		HttpRequestInitializer requestInitializer;

		Collection<String> scopes = Lists.newArrayList();

		Clock clock = Clock.SYSTEM;

		CredentialCreatedListener credentialCreatedListener;

		Collection<CredentialRefreshListener> refreshListeners = Lists
				.newArrayList();

		public Builder(AccessMethod method, HttpTransport transport,
				JsonFactory jsonFactory,HttpExecuteInterceptor clientAuthentication, String clientId) {
			setMethod(method);
			setTransport(transport);
			setJsonFactory(jsonFactory);
			setTokenServerUrl(new GenericUrl("http://211.189.127.73:8000/o/token/"));
			setClientAuthentication(clientAuthentication);
			setClientId(clientId);
			setAuthorizationServerEncodedUrl("http://211.189.127.73:8000/o/authorize");
			setApprovalPrompt(approvalPrompt);
			setCredentialDataStore(credentialDataStore);
		}

		// /// authorization code flow 빌더에 의해 새로운 instance가 생김
		public AuthorizationCodeFlow build() {
			return new AuthorizationCodeFlow(this);
		}

		public final AccessMethod getMethod() {
			return method;
		}

		public Builder setMethod(AccessMethod method) {
			this.method = Preconditions.checkNotNull(method);
			return this;
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

		public final DataStore<StoredCredential> getCredentialDataStore() {
			return credentialDataStore;
		}

		
		public final Clock getClock() {
			return clock;
		}

		public Builder setClock(Clock clock) {
			this.clock = Preconditions.checkNotNull(clock);
			return this;
		}

		public Builder setDataStoreFactory(DataStoreFactory dataStoreFactory)
				throws IOException {
			return setCredentialDataStore(StoredCredential.getDefaultDataStore(dataStoreFactory));
		}

		public Builder setCredentialDataStore(	DataStore<StoredCredential> credentialDataStore) {
			this.credentialDataStore = credentialDataStore;
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

		public Builder setCredentialCreatedListener(CredentialCreatedListener credentialCreatedListener) {
			this.credentialCreatedListener = credentialCreatedListener;
			return this;
		}

		public Builder addRefreshListener(
				CredentialRefreshListener refreshListener) {
			refreshListeners.add(Preconditions.checkNotNull(refreshListener));
			return this;
		}

		public final Collection<CredentialRefreshListener> getRefreshListeners() {
			return refreshListeners;
		}

		public Builder setRefreshListeners(Collection<CredentialRefreshListener> refreshListeners) {
			this.refreshListeners = Preconditions
					.checkNotNull(refreshListeners);
			return this;
		}

		public final CredentialCreatedListener getCredentialCreatedListener() {
			return credentialCreatedListener;
		}

	}

}
