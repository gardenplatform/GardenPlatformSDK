package swssm.garden.sdk;

import java.io.IOException;
import java.util.List;

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

import org.json.*;
import org.json.simple.JSONObject;

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
	
	public Profile getProfiles() throws IOException{
		request = requestFactory.buildGetRequest(new GenericUrl("http://211.189.127.73:8000/api/v1/me?format=json"));
		request.setParser(new JsonObjectParser(new JacksonFactory()));
		request.getHeaders().setAuthorization(HEADER_PREFIX +' '+ accessToken);
		HttpResponse response = request.execute();
		Profile  profile = response.parseAs(Profile.class);
		return profile;
	}
	
	public List<Apps> getAppList() throws IOException{
		Profile user = getProfiles();
		request = requestFactory.buildGetRequest(new GenericUrl("http://211.189.127.73:8000/api/v1/users/"+user.getUserId()+"/apps?format=json"));
		request.setParser(new JsonObjectParser(new JacksonFactory()));
		request.getHeaders().setAuthorization(HEADER_PREFIX +' '+ accessToken);
		HttpResponse response = request.execute();
		AppList appList= response.parseAs(AppList.class);
		return appList.getAppList();
	}
	
	public List<Apps> getProjectList() throws IOException{
		Profile user = getProfiles();
		request = requestFactory.buildGetRequest(new GenericUrl("http://211.189.127.73:8000/api/v1/users/"+user.getUserId()+"/projects?format=json"));
		request.setParser(new JsonObjectParser(new JacksonFactory()));
		request.getHeaders().setAuthorization(HEADER_PREFIX +' '+ accessToken);
		HttpResponse response = request.execute();
		ProjectList prjList= response.parseAs(ProjectList.class);
		return prjList.getAppList();
	}

	public static class Profile extends GenericJson {
		
		  /** Profile id */
		  @Key("username")
		  private String userId;
		  @Key
		  private String gender;
		  @Key("real_name")
		  private String realName;
		  @Key("phone")
		  private String phoneNumber;
		  @Key("profile_img")
		  private String profileImg;
		  @Key("class_num")
		  private String classNum;
		  @Key
		  private String email;
		  
		  public String getUserId(){
			  return userId;
		  }
		  
		  public String getGender(){
			  return gender;
		  }
		  public String getName(){
			  return realName;
		  }
		  public String getPhoneNumber(){
			  return phoneNumber;
		  }
		  
		  public String getProfileImg() {
				return profileImg;
		  }
		  
		  public String getClassNum(){
			  return classNum;
		  }
		  public String getEmail(){
			  return email;
		  }
	  
	}
	
	public static class ProjectList {
		  @Key
		  private List<Apps> projects;
		  public List<Apps> getAppList() {
		    return projects;
		  }
	}
	
	public static class AppList {
		  @Key
		  private List<Apps> apps;
		  public List<Apps> getAppList() {
		    return apps;
		  }
	}
	
	public static class Apps extends GenericJson {

		  @Key("client_name")
		  private String clientName;
		  @Key("display_name")
		  private String displayName;
		  @Key
		  private String url;
		  @Key
		  private String category;
		  @Key("short_description")
		  private String description;
		  @Key("app_icon")
		  private String appIcon;
		  
		public String getClientName() {
			return clientName;
		}
		public String getDisplayName() {
			return displayName;
		}
		public String getUrl() {
			return url;
		}
		public String getCategory() {
			return category;
		}
		public String getDescription() {
			return description;
		}
		public String getAppIcon() {
			return appIcon;
		}


	}
	
}
