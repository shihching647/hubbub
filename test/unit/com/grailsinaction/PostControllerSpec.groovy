package com.grailsinaction

import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
//有TestFor()自動產生params和controller物件
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

    def "Adding a valid new post to the timeline"() {
        given: "A user with posts in the db"
        User chuck = new User(
                loginId: "chuck_norris",
                password: "password").save(failOnError: true)
        and: "A loginId parameter"
        params.id = chuck.loginId
        and: "Some content for the post"
        params.content = "Chuck Norris can unit test entire applications with a single assert."
        when: "addPost is invoked"
        def model = controller.addPost()
        then: "our flash message and redirect confirms the success"
        flash.message == "Successfully created Post"
        response.redirectedUrl == "/post/timeline/${chuck.loginId}"
        Post.countByUser(chuck) == 1
    }

    def "新增一篇空白的貼文"(){
        given: "新增一位使用者"
        User jim = new User(
                loginId: "Jim",
                password: "123456"
        ).save(failOnError: true)

        and: "使用者loginId"
        params.id = jim.loginId

        and: "新增一篇空白貼文"
        params.content = ""

        when: "呼叫PostController的addPost()方法"
        def model = controller.addPost()

        then: "message產生內容為空白的錯誤訊息"
        flash.message == "Invalid or empty post"
        response.redirectedUrl == "/post/timeline/${jim.loginId}"
        Post.countByUser(jim) == 0
    }

    @spock.lang.Unroll //讓不同where條件的測試結果分開顯示
    def "測試index方法正確轉交" (){
        given:
        params.id = suppliedId

        when:
        controller.index()

        then:
        response.redirectedUrl == expectedUrl

        where:
        suppliedId | expectedUrl
        null       | '/post/timeline/chuck_norris'
        'joe_cool' | '/post/timeline/joe_cool'
    }

    //測試postController by postService
    def "Adding a valid new post to the timeline by postService" () {
        given: "a mock post Service"
        //Mock(Class)
        def mockPostService = Mock(PostService)
        //當此postService.createPost被呼叫時,會回傳new Post(content: "Mock Post"), 參數(_, _)代表指隨意的兩個參數都可以傳入, 前面的 1* 代表此service只會被呼叫一次,
        1 * mockPostService.createPost(_, _) >> new Post(content: "Mock Post")
        controller.postService = mockPostService //inject postService into the controller

        when: "controller is invoked"
        def result = controller.addPost("joe_cool", "Posting up a storm")

        then: "redirected to timeline, flash message tells us all is well"
        flash.message ==~ "Added new post: Mock Post" //==~ The regex match operator
        response.redirectedUrl == "/post/timeline/joe_cool"
    }


}
