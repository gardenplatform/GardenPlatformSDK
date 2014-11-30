package swssm.garden.sdk;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Key;

/**
*
*
* Access Token Response에 대한 JSON Model 
* 
* @author SW
* @version 1.0
*
*/
public class TokenResponse extends GenericJson{

	/** */
	@Key("access_token")
	private String accessToken;
	
	/** RFC6749#section-7.1에 정의 된 토큰 타입*/
	@Key("token_type")
	private String tokenType;
	
	/** RefreshTokenRequest를 사용하여 새로 얻은 accessToekn*/
	@Key("refresh_token")
	private String refreshToken;
	
	/** accessToken의 수명 (초 단위)*/
	@Key("expires_in")
	private Long expiresInSeconds;
	
	/** RFC6749#section3.3에 정의 된 accessToken의 범위*/
	@Key
	private String scope;
	
	/** Authorization server로 부터 발행된 accessToken을 리턴*/
	public final String getAccessToken(){
		return accessToken;
	}
	
	/** Authorization server로 부터 발행된 accessToken을 세팅*/
	public TokenResponse setAccessToken(String accessToekn){
		this.accessToken = Preconditions.checkNotNull(accessToken);
		return this;
	}
	
	/** 토큰 타입을 리턴*/
	public final String getTokenType(){
		return tokenType;
	}
	
	/** 토큰 타입을 세팅*/
	public TokenResponse setTokenType(){
		this.tokenType = Preconditions.checkNotNull(tokenType);
		return this;
	}
	
	/** accessToken의 수명을 리턴*/
	public final long getExpiresInSeconds(){
		return expiresInSeconds;
	}
	/** accessToken의 수명을 세팅*/
	public TokenResponse setExpiresInSeconds(Long expiresInSeconds){
		this.expiresInSeconds = expiresInSeconds;
		return this;
	}
	/** refresToken을 리턴*/
	public final String getRefreshToken(){
		return refreshToken;
	}
	
	/** refreshToken을 세팅*/
	public TokenResponse setRefreshToekn(String refreshToken){
		this.refreshToken = refreshToken;
		return this;
	}
	
	/** scope를 리턴*/
	public final String getScope(){
		return scope;
	}
	
	/** scope를 셋팅*/
	public TokenResponse setScope(String scope){
		this.scope=scope;
		return this;
	}
	
	@Override
	public TokenResponse set(String fieldName, Object value){
		return (TokenResponse) super.set(fieldName, value);
	}
	
	public TokenResponse clone(){
		return (TokenResponse) super.clone();
	}
	
}
