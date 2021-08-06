import com.google.gson.Gson
import javafx.application.Application.launch
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

suspend fun majMessage(text:VBox, root:StackPane, chatBox:TextField){
        var user = newUserChatBean(null, null, CODE)

        var printedMessage = 0
        val gson = Gson()
        while (true) {
            val m: Message? = gson.fromJson(
                sendPostOkHttpRequest("http://127.0.0.1:8080/getNewChat", gson.toJson(user)),
                Message::class.java
            )
            m ?: throw Exception("mauvais mot de passe")

            if (m.message.size != printedMessage) {
                text.children.clear()
                m.message.forEach { text.children.add(Text(it.toString())) }
                printedMessage = m.message.size


            }
            chatBox.minWidth = root.widthProperty().value - 70
            delay(1000)
        }



}