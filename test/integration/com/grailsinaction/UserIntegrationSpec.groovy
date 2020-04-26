package com.grailsinaction


import spock.lang.*

class UserIntegrationSpec extends Specification {

    //測試完後會把資料庫roll back成原來的
    def "Saving our first user to the database"() {
        given: "A brand new user"
        def joe = new User(loginId: 'joe', password: 'secret',
                homepage: 'http://www.grailsinaction.com')

        when: "the user is saved"
        joe.save() //return User物件, 並設定一個errors物件給他

        then: "it saved successfully and can be found in the database"
        joe.errors.errorCount == 0
        joe.id != null
        User.get(joe.id).loginId == joe.loginId
    }

    def "Updating a saved user changes its properties"() {

        given: "An existing user"
        def existingUser = new User(loginId: 'joe', password: 'secret',
                homepage: 'http://www.grailsinaction.com')
        existingUser.save(failOnError: true)

        when: "A property is changed"
        def foundUser = User.get(existingUser.id)
        foundUser.password = "sesame"
        foundUser.save(failOnError: true) //Setting the failOnError:true option to save() means Grails will throw an exception if the object fails any validation tests

        then: "The change is reflected in the database"
        User.get(existingUser.id).password == "sesame"

    }

    def "Deleting an existing user removes it from the database"() {

        given: "An existing user"
        def existingUser = new User(loginId: 'joe', password: 'secret',
                homepage: 'http://www.grailsinaction.com')
        existingUser.save(failOnError: true)

        when: "The user is deleted"
        def foundUser = User.get(existingUser.id)
        foundUser.delete(flush: true) //We use the flush:true option because we want your test to delete it from the database immediately and not batch up the change.

        then: "The change is reflected in the database"
        !User.exists(existingUser.id) //即使資料庫的資料被刪除, 物件參考沒有被記憶體回收
        !User.exists(foundUser.id)    //即使資料庫的資料被刪除, 物件參考沒有被記憶體回收
    }

    //測試validate
    def "Saving a user with invalid properties causes an error"() {
        given: "A user which fails several field validations"
        def user = new User(loginId: 'joe',
                password: 'tiny', homepage: 'not-a-url')

        when: "The user is validated"
        user.validate() //return true/false

        then:
        user.hasErrors()
        "size.toosmall" == user.errors.getFieldError("password").code
        "tiny" == user.errors.getFieldError("password").rejectedValue
        "url.invalid" == user.errors.getFieldError("homepage").code
        "not-a-url" == user.errors.getFieldError("homepage").rejectedValue
        !user.errors.getFieldError("loginId")
        /*user有一個error Object, 可以由getFieldError("property名稱")得到fieldError Object
        * fieldError裡面有code(不合法原因), rejectedValue(不合法的值)*/
    }

    def "Recovering from a failed save by fixing invalid properties"() {
        given: "A user that has invalid properties"
        def chuck = new User(loginId: 'chuck',
                password: 'tiny', homepage: 'not-a-url')
        assert chuck.save() == null
        assert chuck.hasErrors()

        when: "We fix the invalid properties"
        chuck.password = "fistfist"
        chuck.homepage = "http://www.chucknorrisfacts.com"
        chuck.validate()

        then: "The user saves and validates fine"
        !chuck.hasErrors()
        chuck.save()
    }

    def "Test"() {
        given: "帳號密碼相同"
        def testUser = new User(loginId: '123', password: '123', homepage: 'www.yahoo.com')

        when: "驗證"
        testUser.validate()

        then: "印出結果"
        System.out.print(testUser.errors.getFieldErrors())
        //System.out.print(testUser.errors.getFieldError("password").rejectedValue)
    }

    def "Ensure a user can follow other users"() {

        given: "A set of baseline users"
        def joe = new User(loginId: 'joe', password:'password').save()
        def jane = new User(loginId: 'jane', password:'password').save()
        def jill = new User(loginId: 'jill', password:'password').save()

        when: "Joe follows Jane & Jill, and Jill follows Jane"
        joe.addToFollowing(jane)
        joe.addToFollowing(jill)
        jill.addToFollowing(jane)

        then: "Follower counts should match following people"
        2 == joe.following.size()
        1 == jill.following.size()
    }
}