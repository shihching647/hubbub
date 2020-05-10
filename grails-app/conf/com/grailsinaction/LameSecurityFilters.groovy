package com.grailsinaction

import spock.util.mop.Use

class LameSecurityFilters {

    def filters = {
        secureActions(controller:'post', action: '(addPost|deletePost)') {
            before = {
                if(params.impersonateId){
                    session.user = User.findByLoginId(params.impersonateId)
                }
                if(!session.user){
                    redirect(controller: "login", action: "form")
                    return false //(必加)直接中止程式!!
                }
            }
            after = { Map model ->

            }
            afterView = { Exception e ->
                log.debug("Finished running ${controllerName} – ${actionName}")
            }
        }
    }
}
