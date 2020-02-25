package com.test.basicstudytest.basicstudytest.security.oauth2.user;

import com.test.basicstudytest.basicstudytest.entity.AuthProvider;
import com.test.basicstudytest.basicstudytest.exception.OAuth2AuthenticationProcessingException;


import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String,Object> attributes){
        if(registrationId.equalsIgnoreCase(AuthProvider.google.toString())){
            return new GoogleOAuth2UserInfo(attributes);
        }
        else{
            throw new OAuth2AuthenticationProcessingException("Login with the provided "+registrationId+" cannot be done");
        }
    }
}
