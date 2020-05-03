package com.grailsinaction

class PostController {
    static scaffold = true

    def timeline(){
        def user = User.findByLoginId(params.id) //預設URL mapping -> /controller/action/id

        if(user){
            return [user: user]
        }else{
            response.sendError(404)
        }
    }

    def addPost () {
        def user = User.findByLoginId(params.id)
        if(user){
            def post = new Post(params)
            user.addToPosts(post)
            if(user.save(flush: true)){ //要加flush: true才會馬上執行
                flash.message = "Successfully created Post"
            }else{
                flash.message = "Invalid or empty post"
            }
        }else{
            flash.message = "Invalid User Id"
        }
//        def size = Post.where {
//            user == user
//        }.count()
//        System.out.println("**************************************************${size}")
        redirect(action: "timeline", id: params.id)
    }
}
