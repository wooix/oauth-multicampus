package com.multi.oauth10client.util;

import java.util.Map;

public class AccessTokenVO extends RequestTokenVO  {

    private String screenName;
    private long userId;
    
    public AccessTokenVO(){
    	this(null);
    }
    
	public AccessTokenVO(String twitterMsg) {
		
		super();
		
		if(twitterMsg  == null){
			return;
		}
		
		Map<String, String> dataMap = OAuthUtil.setAccessData(twitterMsg);
		
		this.setRequestOauthToken(dataMap.get("oauth_token"));
		this.setVerifier(dataMap.get("oauth_verifier"));
		
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return super.toString()+",\nscreenName=" + screenName + ", \nuserId=" + userId;
	}
	
	@Override
	public void sign(){
		
		StringBuilder builder = new StringBuilder();
		builder.append(this.getMethod() +"&");
		builder.append(OAuthUtil.encode(this.getRequestURL())+"&");
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_CONSUMER_KEY+"="+this.getConsumerKey()+"&"));
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_NONCE+"=" + this.getNonce()+"&"));
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_SIGNATURE_METHOD+"="+this.getSignatureMethod()+"&"));
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_TIMESTAMP+"="+this.getTimestamp()+"&"));
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_TOKEN+"="+this.getRequestOauthToken()+"&"));
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_VERIFIER+"="+this.getVerifier()+"&"));
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_VERSION+"="+this.getVersion()));
		
		System.out.println("@@@ Client BaseString : " + builder.toString());
		HmacSHA1 sha1 = new HmacSHA1();
	
		System.out.println("client Consumer Secret key" + this.getConsumerSecretKey());
		System.out.println("client Consumer token Secret key" + this.getRequestOauthTokenSecret());
		
		sha1.setConsumerSecret(this.getConsumerSecretKey());
		sha1.setTokenSecret(this.getRequestOauthTokenSecret());
		
		try {
			String signed = sha1.getSignature(builder.toString());
			this.setSignature(signed);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setBaseString(builder.toString());
	}

}
