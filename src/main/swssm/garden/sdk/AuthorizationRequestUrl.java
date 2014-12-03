package swssm.garden.sdk;

import java.util.Collection;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;

/**
*
*  사용자에게 어플리케이션의 보호된 리소스에 접근하는 것에 허가하기 위한  
*  Oauth 2.0 URL Builder 
* 
* @author SW
* @version 1.0
*
*
*/

public class AuthorizationRequestUrl extends GenericUrl{
	
	
	/** Access Token을 얻기위한 Authorization code를 request하기 위한 Response 타입 */
	@Key("response_type")
	private String responseTypes;
	
	/** 성공적인 허가 승인 후 redirect할 Uri */
	@Key("redirect_uri")
	private String redirectUri;
	
	/** 스페이스로 구분 된 범위의 list*/
	@Key("scope")
	private String scopes;
	
	/** 클라이언트 구분을 위한 ID*/
	@Key("client_id")
	private String clientId;
	
	@Key("approval_prompt")
	private String approvalPrompt;
	
	/** request와 callback 사이에서 클라이언트의 state를 유지하기 위한 값 */
	@Key
	private String state;
	
	/**
	 * 
	 * @param authorizationServerEncodedUrl
	 * @param clientId
	 * @param responseTypes
	 * @param approvalPrompt
	 */
	 public AuthorizationRequestUrl(
		      String authorizationServerEncodedUrl, String clientId, Collection<String> responseTypes, String approvalPrompt) {
		    super(authorizationServerEncodedUrl);
		    Preconditions.checkArgument(getFragment() == null);
		    setClientId(clientId);
		    setResponseTypes(responseTypes);
		    setApprovalPrompt(approvalPrompt);
	}
	 
	 /**
	  * response type을 리턴 
	  * @return responseTypes
	  */
	 public final String getResponseTypes(){
		 return responseTypes;
	 }
	 
	 /**
	  * response type을 셋팅 
	  * @param responseTypes
	  * @return responseTypes
	  */
	 public AuthorizationRequestUrl setResponseTypes(Collection<String> responseTypes){
		 this.responseTypes = Joiner.on(' ').join(responseTypes);
		 return this;
	 }
	 
	 /**
	  * redirectUri를 리턴 
	  * @return redirectUri
	  */
	 public final String getRedirectUri(){
		 return redirectUri;
	 }
	 /**
	  * redirectUri를 셋팅 
	  * @return redirectUri
	  */
	 public AuthorizationRequestUrl setRedirectUri(String redirectUri){
		 this.redirectUri =redirectUri;
		 return this;
	 }
	 /**
	  * scope를 리턴 
	  * @return scopes
	  */
	 public final String getScopes(){
		 return scopes;
	 }
	 /**
	  * scopes를 셋팅 
	  * @param scopes
	  * @return scopes
	  */
	 public AuthorizationRequestUrl setScopes(Collection<String> scopes){
		 this.scopes = scopes == null || !scopes.iterator().hasNext() ? null : Joiner.on(' ').join(scopes);
		 return this;
	 }
	 /**
	  * client id를 리턴 
	  * @return clientId
	  */
	 public final String getClientId(){
		 return clientId;
	 }
	 /**
	  * client id를 셋팅 
	  * @param clientId
	  * @return clientId
	  */
	 public AuthorizationRequestUrl setClientId(String clientId){
		 this.clientId = Preconditions.checkNotNull(clientId);
		 return this;
	 }
	 /**
	  * state를 리턴 
	  * @return state
	  */
	 public final String getState(){
		 return state;
	 }
	 /**
	  * state를 셋팅 
	  * @param state
	  * @return state
	  */
	 public AuthorizationRequestUrl setState(String state){
		 this.state=state;
		 return this;
	 }
	 /**
	  * approval prompt를 리턴 
	  * @return approvalPrompt
	  */
	 public final String getApprovalPrompt(){
		 return approvalPrompt;
	 }
	 /**
	  * approval prompt를 셋팅 
	  * @param approvalPrompt
	  * @return approvalPrompt
	  */
	 public AuthorizationRequestUrl setApprovalPrompt(String approvalPrompt){
		 this.approvalPrompt=approvalPrompt;
		 return this;
	 }
	 
	 @Override
	 public AuthorizationRequestUrl set(String fieldName, Object value){
		 return (AuthorizationRequestUrl) super.set(fieldName,value);
	 }
	 
	 @Override
	 public AuthorizationRequestUrl clone(){
		 return (AuthorizationRequestUrl) super.clone();
	 }
}

