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
        return[users : users, term : params.loginId, totalUsers: User.count()]
        //User.count()直接得到總數量
        //params.loginId等於 requset.getParameter("loginId")
    }
}
