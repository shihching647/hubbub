package com.grailsinaction

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
@Mock([User,Profile])
class UserControllerSpec extends Specification {
    //不知道是三小,測不過!!!!
    def "Registering a user with known good parameter" () {
        given: "a set of user parameters"
        params.with {
            loginId = "Jim"
            password = "123456"
        }

        and: "a set of profile parameters"
        params['profile.fullName'] = "Jimmy butler"
        params['profile.email'] = "shihching@gmail.com"
        params['profile.homepage'] = "https://www.google.com"

        when: "the user is registered"
        request.method = "POST"
        controller.register(params)

        then: "the user is created, and browser redirected"
        User.count() == 1
        Profile.count() == 1
        response.redirectedUrl == "/"
    }

    @Unroll
    def "Registration command object for #loginId validate correctly"() {

        given: "a mocked command object"
        def urc = mockCommandObject(UserRegistrationCommand) //使用mockCommandObject後就可使用.validate()方法,也會產生errors property

        and: "a set of initial values from the spock test"
        urc.loginId = loginId
        urc.password = password
        urc.passwordRepeat = passwordRepeat
        urc.fullName = "Jim"
        urc.email = "someone@nowhere.net"

        when: "the validator is invoked"
        def isValidRegistration = urc.validate()

        then: "the appropriate fields are flagged as errors"
        isValidRegistration == anticipatedValid
        urc.errors.getFieldError(fieldInError)?.code == errorCode

        where:
        loginId  | password   | passwordRepeat| anticipatedValid    | fieldInError       | errorCode
        "glen"   | "password" | "no-match"    | false               | "passwordRepeat"   | "validator.invalid"
        "peter"  | "password" | "password"    | true                | null               | null
        "a"      | "password" | "password"    | false               | "loginId"          | "size.toosmall"

    }

    //不知道是三小,測不過!!!!
    def "Invoking the new register action via a command object" () {
        given: "A configured command object"
        def urc = mockCommandObject(UserRegistrationCommand) //mockCommandObject
        urc.with {
            loginId = "Jim"
            password = "123456"
            passwordRepeat = "123456"
            fullName = "Jimmy butler"
            email = "shihching@gmail.com"
            homepage = "https://www.google.com"
        }

        and: "which has been validated"
        urc.validate()

        when: "the register action is invoked"
        controller.register2(urc)

        then: "the user is created, and browser redirected"
        !urc.hasErrors()
        User.count() == 1
        Profile.count() == 1
        response.redirectedUrl == "/"
    }
}
