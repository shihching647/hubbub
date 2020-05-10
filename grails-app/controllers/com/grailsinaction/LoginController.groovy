package com.grailsinaction

class LoginController {

    def form () {

    }

    def signIn(String loginId, String password) {
        def user = User.findByLoginIdAndPassword(loginId, password)
        if(user){
            session.user = user
            redirect(controller: "post", action: "timeline", id: user.loginId)
        }else{
            redirect(action: "form")
            flash.message = "Invalid login ID or password"
        }
    }
}
