package com.grailsinaction

class Post {

    String content
    Date dateCreated
    static constraints = {
        content blank: false
    }

    /*This property is vitally important in both 1:m and m:n relationships because it tells GORM how
    to implement cascading operations. In particular,*/
    static belongsTo = [ user : User ]

    /*確保每次讀取都是降冪排列*/
    static mapping = {
        sort dateCreated:"desc" // desc / asce
    }

    static hasMany = [ tags : Tag ]

    String getDisplayString() { return content } //Creates a read-only displayString property for the scaffolding(需修改template才行使用)

}
