package com.test.basicstudytest.basicstudytest.security.oauth2;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.test.basicstudytest.basicstudytest.util.CookieUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 *
 * The OAuth2 protocol recommends using a state parameter to prevent CSRF attacks.
 * During authentication, the application sends this parameter in the authorization request, and the OAuth2 provider returns this parameter unchanged in the OAuth2 callback.
 * The application compares the value of the state parameter returned from the OAuth2 provider with the value that it had sent initially.
 * If they don’t match then it denies the authentication request.
 * To achieve this flow, the application needs to store the state parameter somewhere so that it can later compare it with the state returned from the OAuth2 provider.
 * We’ll be storing the state as well as the redirect_uri in a short-lived cookie.
 * The following class provides functionality for storing the authorization request in cookies and retrieving it.
 *
 * */

public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository {
    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private static final Integer cookieExpireSeconds = 180;
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest httpServletRequest) {
        return CookieUtils.getCookie(httpServletRequest, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest oAuth2AuthorizationRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        if(oAuth2AuthorizationRequest == null){
            CookieUtils.deleteCookie(httpServletRequest,httpServletResponse,OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(httpServletRequest,httpServletResponse,REDIRECT_URI_PARAM_COOKIE_NAME);
        }
        CookieUtils.addCookie(httpServletResponse,OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,CookieUtils.serialize(oAuth2AuthorizationRequest),cookieExpireSeconds);
        String redirectionUrlAfterLogin = httpServletRequest.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if(StringUtils.isNotBlank(redirectionUrlAfterLogin)){
            CookieUtils.addCookie(httpServletResponse,REDIRECT_URI_PARAM_COOKIE_NAME,redirectionUrlAfterLogin,cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest httpServletRequest) {
        return this.loadAuthorizationRequest(httpServletRequest);
    }


    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}
