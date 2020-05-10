package com.grailsinaction

import grails.transaction.Transactional

@Transactional
class PostService {

    class PostException extends RuntimeException{
        String message
        Post post
    }

    Post createPost (String loginId, String content) {
        def user = User.findByLoginId(loginId)
        if(user){
            def post = new Post(content: content)
            user.addToPosts(post)
            if(post.validate() && user.save(flush: true)){
                return post
            }else{
                throw new PostException(message: "Invalid or empty post", post: post)
            }
        }else{
            throw new PostException(message: "Invalid user ID")
        }
    }
}
