package com.grailsinaction

class ImageController {

    def upload (PhotoUploadCommand puc) {
        def user = User.findByLoginId(puc.loginId)
        user.profile.photo = puc.photo
        user.save(flush: true)
        redirect controller: "user", action: "profile", id: puc.loginId
    }

    def form() {
        //直接回傳所有user
        [userList: User.list()]
    }

    def renderImage(String id){
        def user = User.findByLoginId(id)
        if(user?.profile?.photo){
            response.setContentLength(user.profile.photo.size())
            response.outputStream.write(user.profile.photo)
        }else{
            response.sendError(404)
        }
    }
}

class PhotoUploadCommand{
    String loginId
    byte[] photo
}
