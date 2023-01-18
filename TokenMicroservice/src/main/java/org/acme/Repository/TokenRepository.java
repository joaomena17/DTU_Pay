package org.acme.Repository;

import org.acme.RequestSingleToken;
import org.acme.TokenService.tokenService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TokenRepository implements ITokenRepository {

   // public Response requestSingleToken(RequestSingleToken T) {
  // public Response requestToken(TokenRequest T) {
  // public Response deleteToken(RequestSingleToken T) {
   tokenService tService = new tokenService();
   // public Response createUser(CreateUser user) {
   @Override
   public void getToken(RequestSingleToken T) {

       return tService.createUser(T);
   }


}
