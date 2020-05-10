package com.grailsinaction

import com.grailsinaction.PostService.PostException

class PostController {
    static scaffold = true
    def postService

    def index () {
        if(!params.id){
            params.id = "chuck_norris"
        }
        redirect(action : "timeline", params: params) //action要用字串
    }

    def timeline(String id){
//        def user = User.findByLoginId(params.id) //預設URL mapping -> /controller/action/id
        def user = User.findByLoginId(id)
        if(user){
            return [user: user]
        }else{
            response.sendError(404)
        }
    }

    def addPost (String id, String content) {
//        def user = User.findByLoginId(id)
//        if(user){
//            def post = new Post(params)
//            user.addToPosts(post)
//            if(user.save(flush: true)){ //要加flush: true才會馬上執行
//                flash.message = "Successfully created Post"
//            }else{
//                flash.message = "Invalid or empty post"
//            }
//        }else{
//            flash.message = "Invalid User Id"
//        }
//        def size = Post.where {
//            user == user
//        }.count()
//        System.out.println("**************************************************${size}")

        //上面整段用Service改寫
        try{
            def newPost = postService.createPost(id, content)
            flash.message = "Added new post: ${newPost.content}"
        } catch(PostException pe){
            flash.message = pe.message
        }
        redirect(action: "timeline", id: id)
    }

    def personal () {
        User user = session.user
        if(user){
            redirect(action: "timeline", id: user.loginId)
        }else{
            redirect(controller: "login", action: "form")
        }
    }
}
