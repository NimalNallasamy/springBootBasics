package com.test.basicstudytest.basicstudytest.security.oauth2;

import com.test.basicstudytest.basicstudytest.entity.AuthProvider;
import com.test.basicstudytest.basicstudytest.entity.UserDAO;
import com.test.basicstudytest.basicstudytest.exception.OAuth2AuthenticationProcessingException;
import com.test.basicstudytest.basicstudytest.repository.UserRepository;
import com.test.basicstudytest.basicstudytest.security.UserPrincipal;
import com.test.basicstudytest.basicstudytest.security.oauth2.user.OAuth2UserInfo;
import com.test.basicstudytest.basicstudytest.security.oauth2.user.OAuth2UserInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.StringUtils;

import javax.naming.AuthenticationException;
import java.util.Optional;


/**
 *
 * The CustomOAuth2UserService extends Spring Security’s DefaultOAuth2UserService and implements its loadUser() method.
 * This method is called after an access token is obtained from the OAuth2 provider.
 * In this method, we first fetch the user’s details from the OAuth2 provider.
 * If a user with the same email already exists in our database then we update his details, otherwise, we register a new user.
 *
 * */

public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try{
            return processOAuth2User(oAuth2UserRequest,oAuth2User);
        }
//        catch (AuthenticationException ex) {
//            throw ex;
//        }
        catch (Exception ex){
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User){

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(),oAuth2User.getAttributes());

        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())){
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<UserDAO> userDAOOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        UserDAO userDAO;

        if(userDAOOptional.isPresent()){
            userDAO = userDAOOptional.get();

            if(! userDAO.getAuthProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))){
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        userDAO.getAuthProvider() + " account. Please use your " + userDAO.getAuthProvider() +
                        " account to login.");
            }

            userDAO = updateExistingUser(userDAO, oAuth2UserInfo);
        }
        else{
            userDAO = registerNewUser(oAuth2UserRequest,oAuth2UserInfo);
        }

        return UserPrincipal.create(userDAO,oAuth2User.getAttributes());
    }

    public UserDAO registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo){

        UserDAO userDAO = new UserDAO();
        userDAO.setName(oAuth2UserInfo.getName());
        userDAO.setEmail(oAuth2UserInfo.getEmail());
        userDAO.setImageUrl(oAuth2UserInfo.getImageUrl());
        userDAO.setAuthProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        userDAO.setProviderId(oAuth2UserInfo.getId());
        return userRepository.save(userDAO);
    }

    public UserDAO updateExistingUser(UserDAO existingUserDAO, OAuth2UserInfo oAuth2UserInfo){
        existingUserDAO.setName(oAuth2UserInfo.getName());
        existingUserDAO.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUserDAO);
    }
}
