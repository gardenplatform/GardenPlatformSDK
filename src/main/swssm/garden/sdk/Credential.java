package swssm.garden.sdk;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.repackaged.com.google.common.base.Objects;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Lists;

/** 
 * 
 * 
 * access token을 이용하여 보호된 리소스에 접근하기 위한 클래스
 * access token이 만료되었을 때, refresh token을 이용하여 새로 발급 받음. 
 * 
 * @author Garden
 * @version 1.0
 * 
 * */
@Deprecated
public class Credential implements HttpExecuteInterceptor,
														HttpRequestInitializer, 
														HttpUnsuccessfulResponseHandler{
	
	static final Logger LOGGER = Logger.getLogger(Credential.class.getName());

	/**
	 * access token을 resource 서버에 전달하기 위한 메소드 
	 *
	 */
	public interface AccessMethod{
		/**
		 * 
		 * @param request
		 * @param accessToken
		 * @throws IOException
		 */
		void intercept(HttpRequest request, String accessToken) throws IOException;
		/**
		 * 
		 * intercept를 통해 주어진 HTTP request의 access token을 리턴 
		 * @param request
		 * @return
		 */
		String getAccessTokenFromRequest(HttpRequest request);
	}
	
	/**
	 * Token Response의 정보를 Lock
	 */
	private final Lock lock = new ReentrantLock();
	
	/**
	 * access token을 resource 서버에 전달하기 위한 메소드
	 */
	private final AccessMethod method;
	
	/**
	 * 현재시간을 나타내기 위한 시계 
	 */
	private final Clock clock;
	/**
	 * Authorization Server로부터 받아온 Access Token 
	 */
	private String accessToken;

	/**
	 * access token 만료시간 
	 */
	private Long expirationTimeMilliseconds;
	
	/**
	 * 새로운 access token을 얻기 위한 토큰 
	 */
	private String refreshToken;
	
	/**
	 * refresh token rerquest를 실행하기 위한 HTTP transport
	 */
	private final HttpTransport transport;
	
	/**
	 * client authentication
	 */
	private final HttpExecuteInterceptor clientAuthentication;
	
	/**
	 * refresh token request의 response를 파싱하기 위한 JSON Factory
	 */
	public final JsonFactory jsonFactory;
	
	/**
	 * 암호화된 Token Server URL
	 */
	private final String tokenServerEncodedUrl;
	
	/**
	 * refresh token의 결과에 대한 리스너
	 */
	private final Collection<CredentialRefreshListener> refreshListeners;
	
	/**
	 * Token Server에 refresh token request를 하기위한 HTTP request initializer
	 */
	private final HttpRequestInitializer requestInitializer;
	
	public Credential(AccessMethod method){
		this(new Builder(method));
	}
	

	/**
	 * 
	 * @param builder
	 */
	protected Credential(Builder builder){
		method = Preconditions.checkNotNull(builder.method);
		transport = builder.transport;
		jsonFactory = builder.jsonFactory;
		tokenServerEncodedUrl = builder.tokenServerUrl == null ? null : builder.tokenServerUrl.build();
		clientAuthentication = builder.clientAuthentication;
		requestInitializer = builder.requestInitializer;
		refreshListeners = Collections.unmodifiableCollection(builder.refreshListeners);
		clock = Preconditions.checkNotNull(builder.clock);
	}

	/**
	 * refresh token이 Bearer Token인지 확인 
	 */
	@Override
	public boolean handleResponse(HttpRequest request, HttpResponse response,boolean supportsRetry) {
		boolean refreshToken = false;
		boolean bearer = false;
		
		List<String> authenticateList = response.getHeaders().getAuthenticateAsList();
		
		if(authenticateList != null){
			for(String authenticate : authenticateList){
				if(authenticate.startsWith(BearerToken.AuthorizationHeaderAccessMethod.HEADER_PREFIX)){
					bearer = true;
					refreshToken = BearerToken.INVALID_TOKEN_ERROR.matcher(authenticate).find();
					break;
				}
			}
		}
		   // if "Bearer" wasn't found, we will refresh the token, if we got 401
		if(!bearer)
			refreshToken = response.getStatusCode() == HttpStatusCodes.STATUS_CODE_UNAUTHORIZED;
		
		if(refreshToken){
			try{
				lock.lock();
				try{
					return !Objects.equal(accessToken, method.getAccessTokenFromRequest(request))
							|| refreshToken();
				} finally{
					lock.unlock();
				}
			} catch(IOException exception){
				LOGGER.log(Level.SEVERE, "unable to refresh token",exception);
			}
		}
		return false;
	}

	@Override
	public void initialize(HttpRequest request) throws IOException {
		request.setInterceptor(this);
		request.setUnsuccessfulResponseHandler(this);
	}

	/**
	 * 
	 * access token이 없거나 남은시간이 1분 이내이면 access token을 refresh 
	 */
	@Override
	public void intercept(HttpRequest request) throws IOException {
		lock.lock();
		try{
			Long expiresIn = getExpiresInSeconds();
			if(accessToken == null || expiresIn != null && expiresIn <= 60){
				refreshToken();
				if(accessToken == null){
					return;
				}
			}
			method.intercept(request, accessToken);
		} finally{
			lock.unlock();
		}
		
	}
	
	/**
	 * access token을 리턴 
	 * @return access token 혹은 {@code null}
	 */
	public String getAccessToken() {
	    lock.lock();
	    try {
	      return accessToken;
	    } finally {
	      lock.unlock();
	    }
	  }
	/**
	 * access token을 셋팅 
	 * @param accessToken
	 * @return access token
	 */
	public Credential setAccessToken(String accessToken){
		lock.lock();
		try{
			this.accessToken = accessToken;
		} finally{
			lock.unlock();
		}
		return this;
	}
	
	/**
	 * access token을 resource server로 보내기 위한 method를 리턴 
	 * @return method 혹은 {@code null}
	 */
	public final AccessMethod getMethod(){
		return method;
	}
	
	/**
	 * Credential 만료시간을 체크하기 위한 시계를 리턴  
	 * @return clock 혹은 {@code null}
	 */
	public final Clock getClock(){
		return clock;
	}
	/**
	 * refresh token request를 실행하기위한 HTTP transport를 리턴
	 * @return transport 혹은 {@code null}
	 */
	public final HttpTransport getTransport(){
		return transport;
	}
	
	/**
	 * refresh token을 request하고 받은 response를 파싱하기 위한 jsonFactory를 리턴 
	 * @return jsonFactory 혹은 {@code null}
	 */
	public final JsonFactory getJsonFactory(){
		return jsonFactory;
	}
	
	/**
	 * toeknServerEncodedUrl을 리턴 
	 * @return tokenServerEncodedUrl 혹은 {@code null}
	 */
	public final String getTokenServerEncodedUrl(){
		return tokenServerEncodedUrl;
	}
	
	/**
	 * refresh token을 리턴 
	 * @return refresh token 혹은 {@code null}
	 */
	public final String getRefreshToken(){
		lock.lock();
		try{
			return refreshToken;
		} finally{
			lock.unlock();
		}
	}
	
	/**
	 * refresh token을 셋팅 
	 * @param refreshToken
	 * @return refreshToken
	 */
	public Credential setRefreshToken(String refreshToken){
		lock.lock();
		try{
			if(refreshToken != null){
				Preconditions.checkArgument(jsonFactory != null && tokenServerEncodedUrl != null,
						"Builder를 사용해서 setJsonFactory, setTransport, setClientAuthentication, "
						+ "setTokenServerUrl/setTokenServerEncodedUrl을 사용하세요.");
			}
			this.refreshToken = refreshToken;
		} finally {
			lock.unlock();
		}
		return this;
	}
	
	/**
	 * expirationTimeMilliseconds를 리턴   
	 * @return expirationTimeMilliseconds
	 */
	public final Long getExpirationTimeMilliseconds(){
		lock.lock();
		try{
			return expirationTimeMilliseconds;
		} finally{
			lock.unlock();
		}
	}
	
	/**
	 * expirationTimeMilliseconds를 셋팅 
	 * @param expirationTimeMilliseconds
	 * @return expirationTimeMilliseconds
	 */
	public Credential setExpirationTimeMilliseconds(Long expirationTimeMilliseconds){
		lock.lock();
		try{
			this.expirationTimeMilliseconds = expirationTimeMilliseconds;
		} finally{
			lock.unlock();
		}
		return this;
	}
	
	/**
	 * access token 만료될 때까지 남은 시간을 리턴 (초) 
	 * @return access token이 만료될 때까지 남은시간 혹은 {@code null}
	 */
	public final Long getExpiresInSeconds(){
		lock.lock();
		try{
			if(expirationTimeMilliseconds == null)
				return null;
			return (expirationTimeMilliseconds - clock.currentTimeMillis()) / 1000;
		} finally {
			lock.unlock();
		}
	}

	/**
	 * access token이 만료될 때까지 남은 시간을 셋팅
	 * @param expiresIn
	 * @return access token이 만료될 때까지 남은 시간 혹은 {@code null}
	 */
	public Credential setExpiresInSeconds(Long expiresIn){
		return setExpirationTimeMilliseconds(
				expiresIn == null? null : clock.currentTimeMillis() + expiresIn * 1000);
	}
	
	/**
	 * clientAuthentication을 리턴 
	 * @return clientAuthentication 혹은 {@code null}
	 */
	public final HttpExecuteInterceptor getClientAuthentication(){
		return clientAuthentication;
	}
	
	/**
	 * refresh token을 token server에 request하기 위한 HTTP request initializer 혹은 혹은 {@code null} 리턴 
	 * @return
	 */
	public final HttpRequestInitializer getReqeustInitializer(){
		return requestInitializer;
	}
	
	/**
	 * Authorization endpoint로부터 새로운 access token을 Request 
	 * @return
	 * @throws IOException
	 */
	public final boolean refreshToken() throws IOException{
		lock.lock();
	    try {
	      try {
	        TokenResponse tokenResponse = executeRefreshToken();
	        if (tokenResponse != null) {
	          setFromTokenResponse(tokenResponse);
	          for (CredentialRefreshListener refreshListener : refreshListeners) {
	            refreshListener.onTokenResponse(this, tokenResponse);
	          }
	          return true;
	        }
	      } catch (TokenResponseException e) {
	        boolean statusCode4xx = 400 <= e.getStatusCode() && e.getStatusCode() < 500;
	        // normal error response인지 체크
	        if (e.getDetails() != null && statusCode4xx) {
	        	// 새로운 access token을 얻지 못하게 됨. 
	          setAccessToken(null);
	          setExpiresInSeconds(null);
	        }
	        for (CredentialRefreshListener refreshListener : refreshListeners) {
	          refreshListener.onTokenErrorResponse(this, e.getDetails());
	        }
	        if (statusCode4xx) {
	          throw e;
	        }
	      }
	      return false;
	    } finally {
	      lock.unlock();
	    }
	}
	
	/**
	 * token response로부터 온 값을 기반으로 access token을 셋팅 
	 * @param tokenResponse
	 */
	public Credential setFromTokenResponse(TokenResponse tokenResponse){
		setAccessToken(tokenResponse.getAccessToken());
		if(tokenResponse.getRefreshToken() != null){
			setRefreshToken(tokenResponse.getRefreshToken());
		}
		setExpiresInSeconds(tokenResponse.getExpiresInSeconds());
		return this;
		
	}
	
	/**
	 * Token Server로부터 새로운 Credential을 얻기위해 request를 실행  
	 * @return Token Server로 부터 Response
	 * @throws IOException
	 */
	protected TokenResponse executeRefreshToken() throws IOException{
		if(refreshToken == null)
			return null;
		
		return new RefreshTokenRequest(transport, jsonFactory, new GenericUrl(tokenServerEncodedUrl),
		        refreshToken).setClientAuthentication(clientAuthentication)
		        .setRequestInitializer(requestInitializer).execute();
	}
	
	/**
	 * refresh token의 결과에 대한 리스너를 리턴 
	 * @return refreshListeners
	 */
	public final Collection <CredentialRefreshListener> getRefreshListeners(){
		return refreshListeners;
	}
	
	/**
	 * Credential을 만드는 Builder
	 * @author Garden
	 *
	 */
	public static class Builder{
		
		/**
		 * access token을 resource 서버에 전달하기 위한 메소드
		 */
		final AccessMethod method;
		/**
		 * refresh token rerquest를 실행하기 위한 HTTP transport
		 */
		HttpTransport transport;
		/**
		 * refresh token request의 response를 파싱하기 위한 JSON Factory
		 */
		JsonFactory jsonFactory;
		/**
		 * Token Server Url
		 */
		GenericUrl tokenServerUrl;
		/**
		 * 만료시간을 체크하기 위한 시계 
		 */
		Clock clock = Clock.SYSTEM;
		/**
		 * client authentication
		 */
		HttpExecuteInterceptor clientAuthentication;
		/**
		 * Token Server에 refresh token request를 하기위한 HTTP request initializer
		 */
		HttpRequestInitializer requestInitializer;
		/**
		 * refresh token의 결과에 대한 리스너
		 */
		Collection<CredentialRefreshListener> refreshListeners = Lists.newArrayList();
		
		public Builder(AccessMethod method){
			this.method=Preconditions.checkNotNull(method);
		}
		
		public Credential build(){
			return new Credential(this);
		}
		
		public final AccessMethod getMethod(){
			return method;
		}
		
		public final HttpTransport getTransport(){
			return transport;
		}
		
		public Builder setTransport(HttpTransport transport){
			this.transport = transport;
			return this;
		}
		
		
		public final Clock getClock(){
			return clock;
		}
		
		public Builder setClock(Clock clock){
			this.clock = Preconditions.checkNotNull(clock);
			return this;
		}
		public final JsonFactory getJsonFactory(){
			return jsonFactory;
		}
		
		public Builder setJsonFactory(JsonFactory jsonFactory){
			this.jsonFactory = jsonFactory;
			return this;
		}
		
		public final GenericUrl getTokenServerUrl(){
			return tokenServerUrl;
		}
		public Builder setTokenServerUrl(GenericUrl tokenServerUrl){
			this.tokenServerUrl = tokenServerUrl;
			return this;
		}
		public Builder setTokenServerEncodedUrl(String tokenServerEncodedUrl){
			this.tokenServerUrl = tokenServerEncodedUrl == null ? null : new GenericUrl(tokenServerEncodedUrl);
			return this;
		}
		
		public final HttpExecuteInterceptor getClientAuthentication(){
			return clientAuthentication;
		}
		
		public Builder setClientAuthentication(HttpExecuteInterceptor clientAuthentication){
			this.clientAuthentication = clientAuthentication;
			return this;
		}
		
		public final HttpRequestInitializer getRequestInitializer(){
			return requestInitializer;
		}
		public Builder setRequestInitializer (HttpRequestInitializer requestInitializer){
			this.requestInitializer = requestInitializer;
			return this;
		}
		
		public Builder addRefreshListener(CredentialRefreshListener refreshListener){
			refreshListeners.add(Preconditions.checkNotNull(refreshListener));
			return this;
		}
		
		public final Collection<CredentialRefreshListener> getRefreshListeners(){
			return refreshListeners;
		}
		
		public Builder setRefreshListeners(Collection<CredentialRefreshListener> refreshListeners){
			this.refreshListeners = Preconditions.checkNotNull(refreshListeners);
			return this;
		}
		
	}

	}

