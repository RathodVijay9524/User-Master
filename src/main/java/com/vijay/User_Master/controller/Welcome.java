package com.vijay.User_Master.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/welcome")
public class Welcome {

    @GetMapping
    public String hi(){
        return "Welcome to java Programing";
    }
    @GetMapping("/csrf")
    public CsrfToken getCsrfToken(HttpServletRequest request){
        /*
         * This endpoint retrieves the CSRF token from the request attributes.
         *
         * CSRF tokens are used to prevent Cross-Site Request Forgery attacks by ensuring
         * that state-changing requests (POST, PUT, DELETE, etc.) originate from the
         * authenticated client.
         *
         * The token is fetched from the "_csrf" attribute, set by Spring Security, and
         * returned to the client so it can include the token in headers or request bodies
         * when making state-changing API calls.
         */
       return (CsrfToken) request.getAttribute("_csrf");
    }
}
