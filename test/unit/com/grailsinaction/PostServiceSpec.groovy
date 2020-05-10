package com.grailsinaction

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PostService)
@Mock([User, Post])
class PostServiceSpec extends Specification {

    def "Valid posts get saved and added to the user " (){
        given: "A new user in db"
        def user = new User(loginId: "Jim", password: "12345678").save(failOnError: true, flush: true)

        when: "A new post is created by postService"
        def newPost = service.createPost("Jim", "A test post")

        then: "The post is returned and added to the user"
        newPost.content == "A test post"
        Post.countByUser(user) == 1
        User.findByLoginId("Jim").posts.size() == 1
    }

    def "Invalid posts generate exceptional out come" (){
        given: "A new user in db"
        def user = new User(loginId: "Jim", password: "12345678").save(failOnError: true, flush: true)

        when: "An invalid new post is attempted"
        def newPost = service.createPost("Jim", "")

        then: "an exception is thrown and no post is saved"
        thrown(PostService.PostException)  //預期會產生PostException
    }

    def "錯誤使用者名稱,會產生Postexception" () {
        given: "A new user in db"
        def user = new User(loginId: "Jim", password: "12345678").save(failOnError: true, flush: true)

        when: "錯誤使用者名稱"
        def newPost = service.createPost("Jimmmmmmm","A test post")

        then: "產生Postexception"
        thrown(PostService.PostException)
    }
}
