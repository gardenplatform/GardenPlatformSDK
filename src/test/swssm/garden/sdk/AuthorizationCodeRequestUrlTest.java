package swssm.garden.sdk;

import java.util.Arrays;

import junit.framework.TestCase;

/** HTTP Parameter :  client_id , redirect_uri, scope, state, response_type */
public class AuthorizationCodeRequestUrlTest extends TestCase {

	private static final String SAMPLE_REQUEST_URL =
		      "https://server.garden.com/authorize"
				  + "?approval_prompt=force"
		    	  + "&client_id=s6BhdRkqt3"
		          + "&redirect_uri=https://client.garden.com/rd"
		          + "&response_type=code"
		          + "&scope=a%20b%20c"
		          + "&state=xyz";
	public AuthorizationCodeRequestUrlTest(String name){
		super(name);
	}
	
	public void testBuild(){
		AuthorizationRequestUrl url =
				new AuthorizationCodeRequestUrl("https://server.garden.com/authorize", "s6BhdRkqt3")
						.setRedirectUri("https://client.garden.com/rd")
						.setScopes(Arrays.asList("a","b","c"))
						.setState("xyz")
						.setApprovalPrompt("force");
		
				/**Google Test*/
//				new AuthorizationCodeRequestUrl("https://accounts.google.com/o/oauth2/auth", "912839437069-2sun1k7gsvqio4782qmhhkcakcu6946n.apps.googleusercontent.com")
//				.setResponseTypes(Arrays.asList("code")).setScopes(Arrays.asList("https://www.googleapis.com/auth/userinfo.profile")).setRedirectUri("http://localhost:8080/oauth2callback");
				
				/**Drop Box Test*/
//		new AuthorizationCodeRequestUrl("https://www.dropbox.com/1/oauth2/authorize", "qc4nk6eyu6xlmzp")
//        .setRedirectUri("http://localhost:8080/GardenSampleApp_4/login_success.jsp").setState("xyz");
		
					
					
		assertEquals(SAMPLE_REQUEST_URL, url.build());
	}
}

//public class AuthorizationCodeRequestUrlTest extends TestCase {
//
//	private static final String SAMPLE_REQUEST_URL =
//		      "https://accounts.google.com/o/oauth2/auth?"
//			+"scope=email%20profile&"
//			 +"state=security_token%3D138r5719ru3e1%26url%3Dhttps://oa2cb.example.com/myHome&"
//			 +"redirect_uri=http://localhost:8080/oauth2callback"
//			 +"response_type=code&"
//			 +"client_id=812741506391.apps.googleusercontent.com&"
//			 +"approval_prompt=force";
//	public AuthorizationCodeRequestUrlTest(String name){
//		super(name);
//	}
//	
//	public void testBuild(){
//		AuthorizationRequestUrl url =
//				new AuthorizationCodeRequestUrl("https://accounts.google.com/o/oauth2/auth","812741506391.apps.googleusercontent.com")
//						.setScopes(Arrays.asList("email","profile"))
//						.setState("security_token%3D138r5719ru3e1%26url%3Dhttps://oa2cb.example.com/myHome&")
//						.setRedirectUri("http://localhost:8080/oauth2callback")
//						.setResponseTypes(Arrays.asList("code"))
//						.setClientId("812741506391.apps.googleusercontent.com");
//						
//					
//					
//		assertEquals(SAMPLE_REQUEST_URL, url.build());
//	}
//}
