package com.grailsinaction

class Profile {

//    User user //資料庫裡的profile table會存user的id
    static belongsTo = [user: User] //與上面的區別-->確保validation會cascades(當register時User與Profile會一起驗證(p.167))
    byte[] photo //在資料庫會建立type為BLOB的欄位
    String fullName
    String bio
    String homepage
    String email
    String timezone
    String country
    String jabberAddress

    static constraints = {
        fullName blank: false
        bio nullable: true, maxSize: 1000
        homepage url: true, nullable: true
        email email: true, blank: false
        photo nullable: true, maxSize: 2 * 1024 * 1024
        country nullable: true
        timezone nullable: true
        jabberAddress email: true, nullable: true
    }

    String toString() { return "Profile of $fullName (id: $id)" } //可直接用$(變數名稱)直接取得值

    String getDisplayString() { return fullName } //Creates a read-only displayString property for the scaffolding(需修改template才行使用)

}
