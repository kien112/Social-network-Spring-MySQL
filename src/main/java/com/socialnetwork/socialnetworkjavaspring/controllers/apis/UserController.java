package com.socialnetwork.socialnetworkjavaspring.controllers.apis;

import com.socialnetwork.socialnetworkjavaspring.controllers.ApplicationController;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.services.sessions.SessionService;
import com.socialnetwork.socialnetworkjavaspring.services.users.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController extends ApplicationController {

    @Autowired
    private IUserService userService;

    @GetMapping("/get-user-friends")
    public ResponseEntity<?> getUserFriends(){
        try{
            return ResponseEntity.ok(userService.getUserFriends(currentUser.getUserId()));
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
