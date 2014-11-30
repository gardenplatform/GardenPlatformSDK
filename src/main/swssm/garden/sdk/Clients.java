package swssm.garden.sdk;

import java.io.IOException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Key;


public class Clients {
	private final JsonFactory jsonFactory;	
	private String accessToken;
	private  String HEADER_PREFIX = "Bearer";
	private HttpTransport transport = new NetHttpTransport();
	private HttpRequestInitializer requestInitializer;
	private HttpRequestFactory requestFactory;
	private HttpHeaders headers = new HttpHeaders();
	private HttpRequest request;
	
	 
	
	public Clients(String accessToken) {
		this.accessToken= accessToken;
		this.jsonFactory =new JacksonFactory();
		requestFactory =
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
			              }
			            });
			          }
		        });
		
		headers.setAuthorization(HEADER_PREFIX + accessToken);
		
	}
	
	public Profile getClients() throws IOException{
		request = requestFactory.buildGetRequest(new GenericUrl("https://graph.facebook.com/v2.2/me"));
		request.setParser(new JsonObjectParser(new JacksonFactory()));
		request.getHeaders().setAuthorization(HEADER_PREFIX +' '+ accessToken);
		HttpResponse response = request.execute();
		Profile  profile = response.parseAs(Profile.class);
		return profile;
	}
	
	public Apps getApps() throws IOException{
		Profile user = getClients();
		request = requestFactory.buildGetRequest(new GenericUrl("https://graph.facebook.com/v2.2/"+user.getName()+"/apps"));
		request.setParser(new JsonObjectParser(new JacksonFactory()));
		request.getHeaders().setAuthorization(HEADER_PREFIX +' '+ accessToken);
		HttpResponse response = request.execute();
		Apps  app= response.parseAs(Apps.class);
		return app;
	}
	

	public static class Profile extends GenericJson {
	  /** Profile id */
	  @Key
	  private String name;
	  @Key
	  private String bio;
	  @Key("first_name")
	  private String firstName;
	  
	  
	  public String getName(){
		  return name;
	  }
	  
	  public String getBio(){
		  return bio;
	  }
	  
	  public String getFirstName(){
		  return firstName;
	  }
	}
	
	public static class Apps extends GenericJson {

		  /** Profile id */
		  @Key
		  private String name;
		  @Key
		  private String bio;
		  @Key("first_name")
		  private String firstName;
		  
		  
	}
}
