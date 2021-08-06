data class Message(val message:List<UserMessageBean>){
    override fun toString(): String {
        var d = ""
        message.forEach { d += "${it.pseudo}: ${it.message}\n" }
        return d
    }
}

data class UserMessageBean(var pseudo:String?, var message:String?){
    override fun toString(): String = "$pseudo : $message"
}

class newUserChatBean(var pseudo:String?, var message:String?, val password: String?)