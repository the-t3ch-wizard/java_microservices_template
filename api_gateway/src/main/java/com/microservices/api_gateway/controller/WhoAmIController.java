// package com.microservices.api_gateway.controller;

// import org.springframework.http.ResponseCookie;
// import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// public class WhoAmIController {

//     @GetMapping("/who-am-i")
//     public String whoAmI() {

//         ResponseCookieBuilder a = ResponseCookie.from("AuthToken");

//         System.out.println("cookie builder " + a);
//         System.out.println("cookie built string " + a.toString());
//         System.out.println("cookie build " + a.build());

//         return "Hello, authenticated user!";
//     }

// }
