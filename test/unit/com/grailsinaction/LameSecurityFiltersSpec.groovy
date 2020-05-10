package com.grailsinaction

import grails.test.mixin.Mock
import spock.lang.Specification

@Mock(LameSecurityFilters)
class LameSecurityFiltersSpec extends Specification {


    //測試LameSecurityFilters
    def "Exercising security filter for unauthenticated user"() {

    }
}
