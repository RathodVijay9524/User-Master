package com.vijay.User_Master.controller;

import com.vijay.User_Master.Helper.ExceptionUtil;
import com.vijay.User_Master.service.HomeService;
import com.vijay.User_Master.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/home")
@AllArgsConstructor
public class HomeController {


    private HomeService homeService;

    private UserService userService;

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUserAccount(@RequestParam Long uid, @RequestParam String code) throws Exception {
        Boolean verifyAccount = homeService.verifyAccount(uid, code);
        if (verifyAccount)
            return ExceptionUtil.createBuildResponseMessage("Account verification success", HttpStatus.OK);
        return ExceptionUtil.createErrorResponseMessage("Invalid Verification link", HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/welcome")
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
