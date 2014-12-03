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
/**
 * Access Token을 이용하여 API를 호출하기 위한 클래스 
 * @author Garden
 *
 */
public class Clients {
	/**
	 * json 파싱을 위한 jsonFactory
	 */
	private final JsonFactory jsonFactory;
	/**
	 * API 호출을 위한 access token 
	 */
	private String accessToken;
	/**
	 * API를 호출할 때 Authorization의 헤더 
	 */
	private  String HEADER_PREFIX = "Bearer";
	/**
	 * Http Transport
	 */
	private HttpTransport transport = new NetHttpTransport();
	/**
	 * Http Request Initializer
	 */
	private HttpRequestInitializer requestInitializer;
	/**
	 * Http Request Factory
	 */
	private HttpRequestFactory requestFactory;
	/**
	 * Http Request
	 */
	private HttpRequest request;
	
	 
	/**
	 * 
	 * @param accessToken
	 */
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
		
//		headers.setAuthorization(HEADER_PREFIX + accessToken);
		
	}
	/**
	 * Access Token을 토큰 서버에 Request 보내어 사용자의 프로필을 불러옴. 
	 * @return
	 * @throws IOException
	 */
	public Profile getProfiles() throws IOException{
		request = requestFactory.buildGetRequest(new GenericUrl("http://211.189.127.73:8000/api/v1/me?format=json"));
		request.setParser(new JsonObjectParser(new JacksonFactory()));
		request.getHeaders().setAuthorization(HEADER_PREFIX +' '+ accessToken);
		HttpResponse response = request.execute();
		Profile  profile = response.parseAs(Profile.class);
		return profile;
	}
	/**
	 * Access Token을 토큰 서버에 Request 보내어 사용자의 앱 목록을 불러옴. 
	 * @return
	 * @throws IOException
	 */
	public List<Apps> getAppList() throws IOException{
		Profile user = getProfiles();
		request = requestFactory.buildGetRequest(new GenericUrl("http://211.189.127.73:8000/api/v1/users/"+user.getUserId()+"/apps?format=json"));
		request.setParser(new JsonObjectParser(new JacksonFactory()));
		request.getHeaders().setAuthorization(HEADER_PREFIX +' '+ accessToken);
		HttpResponse response = request.execute();
		AppList appList= response.parseAs(AppList.class);
		return appList.getAppList();
	}
	
	/**
	 * Access Token을 토큰 서버에 Request 보내어 사용자의 프로젝트 목록을 불러옴. 
	 * @return
	 * @throws IOException
	 */
	public List<Apps> getProjectList() throws IOException{
		Profile user = getProfiles();
		request = requestFactory.buildGetRequest(new GenericUrl("http://211.189.127.73:8000/api/v1/users/"+user.getUserId()+"/projects?format=json"));
		request.setParser(new JsonObjectParser(new JacksonFactory()));
		request.getHeaders().setAuthorization(HEADER_PREFIX +' '+ accessToken);
		HttpResponse response = request.execute();
		ProjectList prjList= response.parseAs(ProjectList.class);
		return prjList.getAppList();
	}

	/**
	 * 프로필을 파싱하기 위한 Json Model
	 * @author Garden
	 *
	 */
	public static class Profile extends GenericJson {
		
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
	/**
	 * 프로젝트 리스트를 파싱하기 위한 Json Model
	 * @author Garden
	 *
	 */
	public static class ProjectList {
		  @Key
		  private List<Apps> projects;
		  public List<Apps> getAppList() {
		    return projects;
		  }
	}
	/**
	 * 앱 리스트를 파싱하기 위한 Json Model
	 * @author Garden
	 *
	 */
	public static class AppList {
		  @Key
		  private List<Apps> apps;
		  public List<Apps> getAppList() {
		    return apps;
		  }
	}
	/**
	 * 앱, 프로젝트의 상세 정보 파싱하기 위한 Json Model
	 * @author Garden
	 *
	 */
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
