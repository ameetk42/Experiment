package com.experiment;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="control")

public class PropertiesControl {
	
private String token;
private String emailTo;
private String[] currArray;
public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}
public String getEmailTo() {
	return emailTo;
}
public void setEmailTo(String emailTo) {
	this.emailTo = emailTo;
}
public String[] getCurrArray() {
	return currArray;
}
public void setCurrArray(String[] currArray) {
	this.currArray = currArray;
}
	
	

}//PropertiesControl
