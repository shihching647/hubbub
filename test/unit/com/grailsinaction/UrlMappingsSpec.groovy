package com.grailsinaction

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.web.mapping.UrlMappings
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(UrlMappings)
@Mock(PostController)
class UrlMappingsSpec extends Specification {
    //測不過!!!!
    def "Ensure basic mapping operations for user permalink"() {

        expect:
        assertForwardUrlMapping(url, controller: expectCtrl, action: expectAction) {
            id = expectId
        }

        where:
        url                      | expectCtrl | expectAction | expectId
        '/users/glen'            | 'post'     | 'timeline'   | 'glen'
        '/timeline/chuck_norris' | 'post'     | 'timeline'   | 'chuck_norris'
    }

}
