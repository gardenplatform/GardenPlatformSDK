package swssm.garden.sdk;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Key;

/**
 * 
 *  Authorization Server로부터 Response받은 URL을 파싱해주는 클래스
 * 
 * 
 * @author SW
 * 	@version 1.0
 *
 */

public class AuthorizationCodeResponseUrl extends GenericUrl{
	
	@Key
	private String code;
	@Key
	private String error;
	@Key
	private String state;
	@Key("error_description")
	private  String errorDescription;
	
	public AuthorizationCodeResponseUrl(String encodedResponseUrl){
		super(encodedResponseUrl);
		System.out.println("code : "+code);
		System.out.println("error : "+error);
		Preconditions.checkArgument((code==null) != (error == null));
	}
	
	public final String getCode(){
		return code;
	}
	public AuthorizationCodeResponseUrl setCode(String code){
		this.code = code;
		return this;
	}
	
	public final String getState(){
		return state;
	}
	
	public AuthorizationCodeResponseUrl setState(String state){
		this.state = state;
		return this;
	}
	
	public final String getError(){
		return error;
	}
	
	public AuthorizationCodeResponseUrl setError(String error){
		this.error = error;
		return this;
	}
	
	public final String getErrorDescription(){
		return errorDescription;
	}
	public AuthorizationCodeResponseUrl setErrorDescription(String errorDescription){
		this.errorDescription = errorDescription;
		return this;
	}
	
	@Override
	public AuthorizationCodeResponseUrl set(String fieldName, Object value){
		return (AuthorizationCodeResponseUrl) super.set(fieldName,value);
	}
	
	@Override
	public AuthorizationCodeResponseUrl clone(){
		return (AuthorizationCodeResponseUrl) super.clone();
	}
}
