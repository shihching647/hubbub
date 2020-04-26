package com.grailsinaction

class Tag {

    String name
    User user
    static constraints = {
        name blank: false
    }
    static hasMany = [ posts : Post ]

    /*The belongsTo field controls where the dynamic addTo*() methods can be used
    from. We can call User.addToTags() because Tag belongsTo User.
    We can also call Post.addToTags() because Tag belongsTo Post. But Post
    doesn’t belongTo Tag, so we can’t call Tag.addToPosts().*/
    static belongsTo = [ User, Post ]

    String getDisplayString() { return name } //Creates a read-only displayString property for the scaffolding(需修改template才行使用)

}
