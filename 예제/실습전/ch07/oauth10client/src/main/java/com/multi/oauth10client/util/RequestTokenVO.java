package com.multi.oauth10client.util;


public class RequestTokenVO extends BasicTokenVO implements ISign{

	//result save 
	private String requestOauthToken;
	private String requestOauthTokenSecret;
	
	//request info
	private String requestURL;
	private String callbackURL;
	private String verifier;
	private String returnTo;
	private String baseString;


	public void setBaseString(String baseString) {
		this.baseString = baseString;
	}
	public String getVerifier() {
		return verifier;
	}
	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}
	public String getBaseString() {
		return baseString;
	}
	public String getReturnTo() {
		return returnTo;
	}
	public void setReturnTo(String returnTo) {
		this.returnTo = returnTo;
	}
	public String getRequestURL() {
		return requestURL;
	}
	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}
	public String getCallbackURL() {
		return callbackURL;
	}
	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}
	public String getRequestOauthToken() {
		return requestOauthToken;
	}
	public void setRequestOauthToken(String requestOauthToken) {
		this.requestOauthToken = requestOauthToken;
	}
	public String getRequestOauthTokenSecret() {
		return requestOauthTokenSecret;
	}
	public void setRequestOauthTokenSecret(String requestOauthTokenSecret) {
		this.requestOauthTokenSecret = requestOauthTokenSecret;
	}
	
	@Override
	public String toString() {
		return "requestOauthToken=" + requestOauthToken
				+ ", \nrequestTokenSecret=" + requestOauthTokenSecret
				+ ", \nrequestURL=" + requestURL + ", \ncallbackURL="
				+ callbackURL + ", \nreturnTo=" + returnTo + ", \nbaseString="
				+ baseString;
	}
	
	@Override
	public void sign(){
		
		StringBuilder builder = new StringBuilder();
		builder.append(this.getMethod() +"&");
		
		builder.append(OAuthUtil.encode(requestURL)+"&");		
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_CALLBACK+"="+OAuthUtil.encode(callbackURL)+"&"));
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_CONSUMER_KEY+"="+this.getConsumerKey()+"&"));
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_NONCE+"="+this.getNonce()+"&"));
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_SIGNATURE_METHOD+"="+this.getSignatureMethod()+"&"));
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_TIMESTAMP+"="+this.getTimestamp()+"&"));
		builder.append(OAuthUtil.encode(OAuthConstant.OAUTH_VERSION+"="+this.getVersion()));

		HmacSHA1 sha1 = new HmacSHA1();
		sha1.setConsumerSecret(this.getConsumerSecretKey());
		sha1.setTokenSecret(null);
		
		try {
			String signed = sha1.getSignature(builder.toString());
			this.setSignature(signed);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.baseString=builder.toString();
	}
	
	
}
