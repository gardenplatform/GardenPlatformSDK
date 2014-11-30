package swssm.garden.sdk;

import java.io.IOException;

import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;

/** refresh token response를 Credential Data Store에 저장하기 위한 클래
 * */

/**
 * Credential Data Store안에 있는 refresh token에 대한 리스너 
 * @author Garden
 *
 */

@Deprecated
public final class DataStoreCredentialRefreshListener implements CredentialRefreshListener{

	/**
	 * 저장 된 Credential Data Store
	 */
	private final DataStore<StoredCredential> credentialDataStore;
	
	/**
	 * 업데이트 될 Credential의 user id 
	 */
	private final String userId;
	
	/**
	 * 
	 * @param userId
	 * @param dataStoreFactory
	 * @throws IOException
	 */
	  public DataStoreCredentialRefreshListener(String userId, DataStoreFactory dataStoreFactory)
		      throws IOException {
		    this(userId, StoredCredential.getDefaultDataStore(dataStoreFactory));
		  }
	  /**
	   * 
	   * @param userId
	   * @param credentialDataStore
	   */
	public DataStoreCredentialRefreshListener( 
			String userId, DataStore<StoredCredential> credentialDataStore) {
		    this.userId = Preconditions.checkNotNull(userId);
		    this.credentialDataStore = Preconditions.checkNotNull(credentialDataStore);
	}
	
	
	/**
	 *  저장 된 Credential Data Store를 리턴. 
	 * @return credentialDataStore 혹은 {@code null}
	 */
	  public DataStore<StoredCredential> getCredentialDataStore() {
	    return credentialDataStore;
	  }

	  /**
	   *  Credential Data Store에 업데이트 된 Credential을 저장 
	   * @param credential
	   * @throws IOException
	   */
	  public void makePersistent(Credential credential) throws IOException {
	    credentialDataStore.set(userId, new StoredCredential(credential));
	  }
	@Override
	public void onTokenResponse(Credential credential,
			TokenResponse tokenResponse) throws IOException {
		makePersistent(credential);
		
	}

	@Override
	public void onTokenErrorResponse(Credential credential,
			TokenErrorResponse tokenErrorResponse) throws IOException {
		makePersistent(credential);		
	}
	
}
