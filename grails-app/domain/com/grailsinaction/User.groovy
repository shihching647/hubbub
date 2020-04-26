package com.grailsinaction

class User {

    String loginId
    String password
    Date dateCreated

    /*hasOne預設是Eager(就是在讀user資料時自動讀取profile的資料)*/
    static hasOne = [ profile : Profile ] //1:1 relation(It needs to be a set (or list) of Profiles to be 1:m.)

    static hasMany = [posts: Post, tags: Tag, following: User] //呼叫user.posts -->回傳Set<Post>

    static mapping = {
        posts sort:'dateCreated'  /*確保每次user.posts都是照著dateCreated排列(與Post那邊的mapping做比較)*/
    }

    static constraints = {
        loginId size: 3..20, unique: true, blank: false //nullable預設就是false, 故可以不寫
        password size: 6..8, blank: false, validator: { passwd, user ->
            passwd != user.loginId
        }
        profile nullable: true
        /*控制Scaffold View Form表單的順序*/
        tags()
        posts()
    }

    String getDisplayString123() { return loginId } //Creates a read-only displayString property for the scaffolding(需修改template才行使用)


}
