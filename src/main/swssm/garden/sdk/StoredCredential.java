package swssm.garden.sdk;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.api.client.repackaged.com.google.common.base.Objects;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;

/** 
 * 
 * access token과 refresh token을 저장하여 한번 Authorize되면 Authorization Page로 
 * 가지 않게 하기 위해 Credential 정보를 저장하기 위한 클래스
 * 
 *  @author Garden
 *  @version 1.0
 * 
 * */

@Deprecated
public class StoredCredential implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * Default DATA_STORE_ID
	 */
	public static final String DEFAULT_DATA_STORE_ID = StoredCredential.class.getSimpleName();
	
	/**
	 * data store에 접근하는 데 access를 lock 
	 */
	private final Lock lock = new ReentrantLock();
	
	/**
	 * Access Token
	 */
	private String accessToken;
	
	/**
	 * access token 만료 시간
	 */
	private Long expirationTimeMilliseconds;
	
	/**
	 * refresh token
	 */
	private String refreshToken;
	
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(
				new Object[] {getAccessToken(), getRefreshToken(), getExpirationTimeMilliseconds()});
	}

	@Override
	public boolean equals(Object other) {
		if(this == other)
			return true;
		if(!(other instanceof StoredCredential))
			return false;
		
		StoredCredential o = (StoredCredential) other;
		return Objects.equal(getAccessToken(), o.getAccessToken()) &&
				Objects.equal(getRefreshToken(), o.getRefreshToken()) &&
				Objects.equal(getExpirationTimeMilliseconds(), o.getExpirationTimeMilliseconds());
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(StoredCredential.class)
				.add("accessToken", getAccessToken())
				.add("refreshToken", getRefreshToken())
				.add("expirationTimeMilliseconds", getExpirationTimeMilliseconds())
				.toString();
	}
	public StoredCredential(){
		
	}
	
	/**
	 * 
	 * @param credential
	 */
	public StoredCredential(Credential credential){
		setAccessToken(credential.getAccessToken());
		setRefreshToken(credential.getRefreshToken());
		setExpirationTimeMilliseconds(credential.getExpirationTimeMilliseconds());
	}
	
	/**
	 * access token을 리턴 
	 * @return  access token 혹은 {@code null}
	 */
	public String getAccessToken(){
		lock.lock();
		try{
			return accessToken;
		} finally{
			lock.unlock();
		}
	}
	
	/**
	 * access token을 셋팅 
	 * @param accessToken
	 * @return access token
	 */
	public StoredCredential setAccessToken(String accessToken){
		lock.lock();
		try{
			this.accessToken = accessToken;
		} finally{
			lock.unlock();
		}
		return this;
	}
	
	/**
	 * access token 만료 시간을 리턴 
	 * @return expirationTimeMilliseconds 혹은 {@code null}
	 */
	public Long getExpirationTimeMilliseconds(){
		lock.lock();
		try{
			return expirationTimeMilliseconds;
		} finally{
			lock.unlock();
		}
	}
	
	/**
	 * access token 만료시간을 셋팅 
	 * @param expirationTimeMilliseconds
	 * @return expirationTimeMilliseconds
	 */
	public StoredCredential setExpirationTimeMilliseconds(Long expirationTimeMilliseconds){
		lock.lock();
		try{
			this.expirationTimeMilliseconds = expirationTimeMilliseconds;
		} finally{
			lock.unlock();
		}
		return this;
	}
	
	/**
	 * refresh token을 리턴 
	 * @return refreshToken
	 */
	public String getRefreshToken(){
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
	 * @return refreshToken 혹은 {@code null}
	 */
	public StoredCredential setRefreshToken(String refreshToken){
		lock.lock();
		try{
			this.refreshToken = refreshToken;
		} finally{
			lock.unlock();
		}
		return this;
	}
	
	/**
	 * ID를 이용하여 저장된 credential data store를 리턴 
	 * @param dataStoreFactory
	 * @return stored credential data store
	 * @throws IOException
	 */
	public static DataStore<StoredCredential> getDefaultDataStore(DataStoreFactory dataStoreFactory)
	      throws IOException {
	    return dataStoreFactory.getDataStore(DEFAULT_DATA_STORE_ID);
	  }
	
 	
}
