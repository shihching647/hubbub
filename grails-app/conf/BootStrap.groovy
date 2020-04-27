import com.grailsinaction.Post
import com.grailsinaction.Profile
import com.grailsinaction.User

import static java.util.Calendar.APRIL
import static java.util.Calendar.AUGUST
import static java.util.Calendar.DECEMBER
import static java.util.Calendar.FEBRUARY
import static java.util.Calendar.MAY
import static java.util.Calendar.NOVEMBER
import static java.util.Calendar.OCTOBER

class BootStrap {

    //關閉Server時執行一次
    def destroy = {
    }

    //啟動Server時執行一次
    def init = { servletContext ->
        environments {
            development {
                if (!Post.count()) createSampleData() //藉由確認Post裡面有沒有資料, 來檢查假資料是否被創建過(可檢查其他Domain class)
            }
            test {
                if (!Post.count()) createSampleData()
            }
        }

        // Admin user is required for all environments
//        createAdminUserIfRequired()
    }

    //除了用environments{} 處理執行環境外, 也可用Environment.current.name得到現在的環境名稱
//    def init = { servletContext ->
//        if (Environment.current != Environment.DEVELOPMENT) {
//            // Set up reference data for
//            // non-development environments
//            . . .
//        }
//        else if (Environment.current.name == "staging") {
//            . . .
//        }
//    }

    /*解釋.save(failOnError: true)意義
    By default, the save() method quietly fails and returns null if the domain instance does not satisfy the class’s validation
    constraints. The failOnError argument solves this issue by throwing an exception if the validation fails.*/
    private createSampleData() {
        println "Creating sample data"

        def now = new Date()
        def chuck = new User(
                loginId: "chuck_norris",
                password: "highkick",
                profile: new Profile(fullName: "Chuck Norris", email: "chuck@nowhere.net"),
                dateCreated: now).save(failOnError: true)
        def glen = new User(
                loginId: "glen",
                password: "sheldon",
                profile: new Profile(fullName: "Glen Smith", email: "glen@nowhere.net"),
                dateCreated: now).save(failOnError: true)
        def peter = new User(
                loginId: "peter",
                password: "mandible",
                profile: new Profile(fullName: "Peter Ledbrook", email: "peter@nowhere.net"),
                dateCreated: now).save(failOnError: true)
        def frankie = new User(
                loginId: "frankie",
                password: "testing",
                profile: new Profile(fullName: "Frankie Goes to Hollywood", email: "frankie@nowhere.net"),
                dateCreated: now).save(failOnError: true)
        def sara = new User(
                loginId: "sara",
                password: "crikey",
                profile: new Profile(fullName: "Sara Miles", email: "sara@nowhere.net"),
                dateCreated: now - 2).save(failOnError: true)
        def phil = new User(
                loginId: "phil",
                password: "thomas",
                profile: new Profile(fullName: "Phil Potts", email: "phil@nowhere.net"),
                dateCreated: now)
        def dillon = new User(loginId: "dillon",
                password: "crikey",
                profile: new Profile(fullName: "Dillon Jessop", email: "dillon@nowhere.net"),
                dateCreated: now - 2).save(failOnError: true)

        chuck.addToFollowing(phil)
        chuck.addToPosts(content: "Been working my roundhouse kicks.")
        chuck.addToPosts(content: "Working on a few new moves. Bit sluggish today.")
        chuck.addToPosts(content: "Tinkering with the hubbub app.")
        chuck.save(failOnError: true)

        phil.addToFollowing(frankie)
        phil.addToFollowing(sara)
        phil.save(failOnError: true)

        phil.addToPosts(content: "Very first post")
        phil.addToPosts(content: "Second post")
        phil.addToPosts(content: "Time for a BBQ!")
        phil.addToPosts(content: "Writing a very very long book")
        phil.addToPosts(content: "Tap dancing")
        phil.addToPosts(content: "Pilates is killing me")
        phil.save(failOnError: true)

        sara.addToPosts(content: "My first post")
        sara.addToPosts(content: "Second post")
        sara.addToPosts(content: "Time for a BBQ!")
        sara.addToPosts(content: "Writing a very very long book")
        sara.addToPosts(content: "Tap dancing")
        sara.addToPosts(content: "Pilates is killing me")
        sara.save(failOnError: true)

        dillon.addToPosts(content: "Pilates is killing me as well")
        dillon.save(failOnError: true, flush: true)

        // We have to update the 'dateCreated' field after the initial save to
        // work around Grails' auto-timestamping feature. Note that this trick
        // won't work for the 'lastUpdated' field.
        def postsAsList = phil.posts as List
        postsAsList[0].addToTags(user: phil, name: "groovy")
        postsAsList[0].addToTags(user: phil, name: "grails")
        postsAsList[0].dateCreated = now.updated(year: 2004, month: MAY)

        postsAsList[1].addToTags(user: phil, name: "grails")
        postsAsList[1].addToTags(user: phil, name: "ramblings")
        postsAsList[1].addToTags(user: phil, name: "second")
        postsAsList[1].dateCreated = now.updated(year: 2007, month: FEBRUARY, date: 13)

        postsAsList[2].addToTags(user: phil, name: "groovy")
        postsAsList[2].addToTags(user: phil, name: "bbq")
        postsAsList[2].dateCreated = now.updated(year: 2009, month: OCTOBER)

        postsAsList[3].addToTags(user: phil, name: "groovy")
        postsAsList[3].dateCreated = now.updated(year: 2011, month: MAY, date: 1)

        postsAsList[4].dateCreated = now.updated(year: 2011, month: DECEMBER, date: 4)
        postsAsList[5].dateCreated = now.updated(year: 2012, date: 10)
        phil.save(failOnError: true)

        postsAsList = sara.posts as List
        postsAsList[0].dateCreated = now.updated(year: 2007, month: MAY)
        postsAsList[1].dateCreated = now.updated(year: 2008, month: APRIL, date: 13)
        postsAsList[2].dateCreated = now.updated(year: 2008, month: APRIL, date: 24)
        postsAsList[3].dateCreated = now.updated(year: 2011, month: NOVEMBER, date: 8)
        postsAsList[4].dateCreated = now.updated(year: 2011, month: DECEMBER, date: 4)
        postsAsList[5].dateCreated = now.updated(year: 2012, month: AUGUST, date: 1)

        sara.dateCreated = now - 2
        sara.save(failOnError: true)

        dillon.dateCreated = now - 2
        dillon.save(failOnError: true, flush: true)

    }

    private createAdminUserIfRequired() {
        println "Creating admin user"
        if (!User.findByLoginId("admin")) {
            println "Fresh Database. Creating ADMIN user."

            def profile = new Profile(email: "admin@yourhost.com", fullName: "Administrator")
            new User(loginId: "admin", password: "secret", profile: profile).save(failOnError: true)
        }
        else {
            println "Existing admin user, skipping creation"
        }
    }
}
