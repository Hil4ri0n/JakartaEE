package com.hil4ri0n.carrental.config;

import com.hil4ri0n.carrental.model.UserRoles;
import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@ApplicationScoped
@FormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                errorPage = "/login_error.xhtml",
                useForwardToLogin = false
        )
)
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/CarRentalDS",
        callerQuery = "select password from users where login = ?",
        groupsQuery = "select role from users where login = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class
)
@DeclareRoles({UserRoles.ADMIN, UserRoles.USER})
public class SecurityConfig {
}
