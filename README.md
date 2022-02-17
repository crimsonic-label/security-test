# Spring Security tests

https://www.tutorialspoint.com/spring_security/spring_security_form_login_with_database.htm

### Form Login with Database

H2 database from file ./db/test.mv.db
Configuration in application.yml - jdbc:h2:file:./db/testdb


http://localhost:8080/h2-console

Disable authentication in SecurityConfig to use h2-console
also add .headers().frameOptions().disable();

### Components
 
- **AuthenticationFilter** - filter that intercepts requests and attempts to authenticate it. In Spring Security, 
it converts the request to an Authentication Object and delegates the authentication to the AuthenticationManager.


- **AuthenticationManager** - the main strategy interface for authentication. 
  It uses authenticate() to authenticate the request, performs the authentication and returns an 
  Authentication Object on successful authentication or throw an AuthenticationException. 
  If the method can’t decide, it will return null. 
  The process of authentication in this process is delegated to the AuthenticationProvider.


- **AuthenticationProvider** - AuthenticationManager is implemented by the ProviderManager 
  which delegates the process to one or more AuthenticationProvider instances. 
  class implementing the AuthenticationProvider interface implements the two methods – authenticate() and supports(). 
  First, let us talk about the supports() method. 
  It is used to check if the particular authentication type is supported by our AuthenticationProvider implementation class. 
  If it is supported it returns true or else false. 
  Next, authenticate() is where the authentication occurs. If the authentication type is supported, 
  the process of authentication is started. 
  Can use the loadUserByUsername() method of the UserDetailsService implementation. 
  If the user is not found, it can throw a UsernameNotFoundException.

  If the user is found, then the authentication details of the user are used to authenticate the user. 
  For example, in the basic authentication scenario, the password provided by the user may be checked with the password in the database.  
  If they are found to match with each other, it is a success scenario. 
  Then we can return an Authentication object from this method which will be stored in the Security Context.


- **UserDetailsService** - core interfaces of Spring Security. 
  The authentication of any request mostly depends on the implementation of the UserDetailsService interface. 
  Used in database backed authentication to retrieve user data. 
  The data is retrieved of loadUserByUsername() where we logic to fetch the user details for a user is provided.
  The method will throw a UsernameNotFoundException if the user is not found.


- **PasswordEncoder** - from Spring Security 5 it is mandatory the use of PasswordEncoder to store passwords. 
  This encodes the user’s password. The most common is the BCryptPasswordEncoder. 
  We can use NoOpPasswordEncoder for our development purposes.


- **Spring Security Context** - details of authenticated user are stored on successful authentication. 
  The authentication object is then available throughout the application for the session. 
  To get username or any other user details, we need SecurityContext. 
  This is done with the SecurityContextHolder, which provides access to the security context. 
  We can use the setAuthentication() and getAuthentication() methods for storing and retrieving the user details respectively.


- **Form Login** - Adding Spring Security to application it adds a login form and sets up a dummy user in auto-configuration mode
  it also sets up the default filters, authentication-managers, authentication-providers