package swssm.garden.sdk;

import java.io.IOException;
import java.util.Collection;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;


/**
 *
 *
 * Access Token을 얻기 위한 Request 클래스.
 * execute()의 결과에 따라 TokenResponse를 리턴하거나, TokenResponseException을 리턴한다.
 * 
 * @author Garden
 * @version 1.0
 *
 */
public class TokenRequest extends GenericData {

	/** HTTP Request를 Initialize 하기 위한 변수 */
	HttpRequestInitializer requestInitializer;
	
	/** 클라이언트 인증을 위한 변수 */
	HttpExecuteInterceptor clientAuthentication;
	
	/**  HTTP Transport */
	private final HttpTransport transport;
	
	/** json 파싱을 위한 변수 */
    private final JsonFactory jsonFactory;
    
    /** Token Server의 URL */
    private GenericUrl tokenServerUrl;
    /** 범위 */
    @Key("scope")
    private String scopes;
    /** 승인 종류 */
    @Key("grant_type")
    private String grantType;
    
    
	///// transport, jsonFactory가 null인지 확인 후 tokenServerUrl, grantType 셋팅
    /**
     * 
     * @param transport
     * @param jsonFactory
     * @param tokenServerUrl
     * @param grantType
     */
    public TokenRequest(HttpTransport transport, JsonFactory jsonFactory, GenericUrl tokenServerUrl, String grantType) {
    	    this.transport = Preconditions.checkNotNull(transport);
    	    this.jsonFactory = Preconditions.checkNotNull(jsonFactory);
    	    setTokenServerUrl(tokenServerUrl);
    	    setGrantType(grantType);
    	  }
    /**
     * 
     * JSON factory를 리턴
     * @return jsonFacory 혹은 {@code null}
     */
	public JsonFactory getJsonFactory() {
		return jsonFactory;
	}
	/**
	 * transport를 리턴
	 * @return transport 혹은 {@code null}
	 */
	public HttpTransport getTransport() {
		return transport;
	}
	
	/**
	 * Http request initializer를 리턴
	 * @return requestInitializer 혹은 {@code null}
	 */
  public final HttpRequestInitializer getRequestInitializer() {
	    return requestInitializer;
	  }
  
  /**
   * 
   * Http request initializer를 셋팅 
   * @param requestInitializer
   * @return requestInitializer 
   */
  public TokenRequest setRequestInitializer(HttpRequestInitializer requestInitializer) {
	    this.requestInitializer = requestInitializer;
	    return this;
	  }
	  
  /**
   * clientAuthentication을 리턴
   * @return clientAuthentication 혹은 {@code null}
   */
  public final HttpExecuteInterceptor getClientAuthentication() {
	    return clientAuthentication;
	  }
  
  /**
   * clientAuthentication을 셋팅 
   * @param clientAuthentication
   * @return clientAuthentication
   */
  public TokenRequest setClientAuthentication(HttpExecuteInterceptor clientAuthentication) {
	    this.clientAuthentication = clientAuthentication;
	    return this;
	  }
  
  /**
   * tokenServerUrl을 리턴 
   * @return tokenServerUrl 혹은 {@code null}
   */
	public GenericUrl getTokenServerUrl() {
		return tokenServerUrl;
	}
	/**
	 * tokenServer을 셋팅
	 * @param tokenServerUrl
	 * @return tokenServerUrl
	 */
	public TokenRequest setTokenServerUrl(GenericUrl tokenServerUrl) {
		this.tokenServerUrl = tokenServerUrl;
		Preconditions.checkArgument(tokenServerUrl.getFragment() == null);
		return this;
	}

	/**
	 * grantType을 리턴 
	 * @return grantType 혹은 {@code null}
	 */
	public String getGrantType() {
		return grantType;
	}

	/**
	 * grantType을 셋팅 
	 * @param grantType
	 * @return grantType
	 */
	public TokenRequest setGrantType(String grantType) {
	    this.grantType = Preconditions.checkNotNull(grantType);
	    return this;
	  }

	/**
	 * scopes를 리 
	 * @return scopes
	 */
	public String getScopes() {
		return scopes;
	}
	
	/**
	 * scopes collection의 scope를 space로 구분.
	 * scopes를 셋팅 
	 * @param scopes
	 * @return scopes
	 */
	public TokenRequest setScopes(Collection<String> scopes) {
	    this.scopes = scopes == null ? null : Joiner.on(' ').join(scopes);
	    return this;
	  }
    

	
	///// Access Token에 대해 Request를 날리고, 파싱되지 않은 HTTP Response를 리턴 함.
	///// TokenResponse에 파싱된 response를 리턴하고 싶으면 execute() 사용.
	///// 리턴 된 HTTP Response가 더이상 필요하지 않으면 Disconnect를 해주어야 함.
	/**
	 *access token을 얻기 위해 Request를 실행하고, HTTP response를 리턴한다.
	 * 
	 * @return {link {@link HttpResponse#parseAs(Class)}에 의해 파싱 될 (파싱되지 않은) access token response
	 * @throws IOException
	 */
	  public final HttpResponse executeUnparsed() throws IOException {
		    HttpRequestFactory requestFactory =
		        transport.createRequestFactory(new HttpRequestInitializer() {

		          public void initialize(HttpRequest request) throws IOException {
		            if (requestInitializer != null) {
		              requestInitializer.initialize(request);
		            }
		            final HttpExecuteInterceptor interceptor = request.getInterceptor();
		            request.setInterceptor(new HttpExecuteInterceptor() {
		              public void intercept(HttpRequest request) throws IOException {
		                if (interceptor != null) {
		                  interceptor.intercept(request);
		                }
		                if (clientAuthentication != null) {
		                  clientAuthentication.intercept(request);
		                }
		              }
		            });
		          }
		        });
		    // Request 생성
		    HttpRequest request = requestFactory.buildPostRequest(tokenServerUrl, new UrlEncodedContent(this));
		    request.setParser(new JsonObjectParser(jsonFactory));
		    request.setThrowExceptionOnExecuteError(false);
		    HttpResponse response = request.execute();
		    if (response.isSuccessStatusCode()) {
		      return response;
		    }
		    throw TokenResponseException.from(jsonFactory, response);
		  }
	  
	  /**
	   * access token을 얻기 위해 Request를 실행하고, 파싱 된 HTTP response를 리턴한다.
	   * @return 파싱 된 access token response
	   * @throws IOException
	   */
	  public TokenResponse execute() throws IOException {
		    return executeUnparsed().parseAs(TokenResponse.class);
		  }
	  
	  @Override
	  public TokenRequest set(String fieldName, Object value) {
	    return (TokenRequest) super.set(fieldName, value);
	  }
}