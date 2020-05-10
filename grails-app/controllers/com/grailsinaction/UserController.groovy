package com.grailsinaction

class UserController {
    static scaffold = true //如果要用Dynamic scaffolding不能定義任何方法

    def search() {

    }

    def results(String loginId){ //Argument name matches name of the text field in the form
        def users = User.where{
//            loginId =~ loginId //
            loginId =~ "%${loginId}%" // % %為SQL的萬用字源符號, =~ SQL的ILIKE comparison(不計大小寫)
        }.list()
//        System.out.print request.getParameter("loginId")
//        System.out.print params.loginId
        return [ users: users,
                 term: params.loginId,
                 totalUsers: User.count() ]
        //User.count()直接得到總數量
        //params.loginId等於 requset.getParameter("loginId")
    }

    def advSearch() {}

    def advResults() {
        def profileProps = Profile.metaClass.properties*.name
        def profiles = Profile.withCriteria {
            "${params.queryType}" {

                params.each { field, value ->

                    if (profileProps.grep(field) && value) {
                        ilike(field, value)
                    }
                }

            }

        }
        [ profiles : profiles ]

    }

    def register () {
        if (request.method == "POST") {
            def user = new User(params)
            System.out.print(user.validate())
            if (user.validate()) {
                user.save(flush: true)
                flash.message = "Successfully Created User"
                redirect(uri: '/') //回到首頁
            } else {
                flash.message = "Error Registering User"
                return [user: user]
            }
        }
    }

    def register2 (UserRegistrationCommand urc) { //Grails會自動把params與UserRegistrationCommand相同名稱的entities bind在一起
        if(urc.hasErrors()){
            render view: "register", model: [user:urc]
        } else {
            def user = new User(urc)
            user.profile = new Profile(urc)
            if(user.validate() && user.save(flash: true)){
                flash.message = "Welcome aboard, ${urc.fullName ?: urc.loginId}"
                redirect(uri: "/")
            } else {
                // maybe not unique loginId?
                flash.message = "失敗"
                return [user: urc]
            }
        }
    }

    def profile (String id) {
        def user = User.findByLoginId(id)
        if(user?.profile){
            [profile: user.profile]
        } else{
            response.sendError(404)
        }
    }
}

//This class is a command object and the purpose is to validate User class
class UserRegistrationCommand {
    String loginId
    String password
    String passwordRepeat //確認密碼
    byte[] photo
    String fullName
    String bio
    String homepage
    String email
    String timezone
    String country
    String jabberAddress

    static constraints = {
        importFrom Profile //可以直接從其他class import constraints
        importFrom User
        password(size: 6..8, blank: false,
                validator: { passwd, urc ->
                    return passwd != urc.loginId
                })
        passwordRepeat(nullable: false,
                validator: { passwd2, urc ->
                    return passwd2 == urc.password
                })
    }
}
