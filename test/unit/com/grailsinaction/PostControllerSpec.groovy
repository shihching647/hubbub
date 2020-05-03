package com.grailsinaction

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PostController)
@Mock([User,Post])
class PostControllerSpec extends Specification {

    def "Get a users timeline given their id" (){
        given: "A user with posts in the db"
        User chuck = new User(
                loginId: "chuck_norris",
                password: "password"
        )
        chuck.addToPosts(new Post(content: "A first post"))
        chuck.addToPosts(new Post(content: "A second post"))
        chuck.save(failOnError: true)

        and: "A loginId parameter"
        params.id = chuck.loginId

        when: "the time line is invoked"
        def model = controller.timeline()

        then: "the user is in the returned model"
        model.user.loginId == "chuck_norris"
        model.user.posts.size() == 1
    }


    def "Check that non-existent users are handled with an error"() {
        given: "the id of a non-existent user"
        params.id = "this-user-id-does-not-exist"
        when: "the timeline is invoked"
        controller.timeline()
        then: "a 404 is sent to the browser"
        response.status == 404
    }
}
