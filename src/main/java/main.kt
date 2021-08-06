import javafx.application.Platform
import javafx.beans.NamedArg
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import javafx.stage.Stage
import kotlinx.coroutines.*
import kotlinx.coroutines.javafx.JavaFx
import kotlin.coroutines.CoroutineContext

class Main : javafx.application.Application(),CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.JavaFx
    val HEIGHT = 300.0
    val WIDTH = 300.0
    var pseudo = ""

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val root = StackPane()

        val see = ScrollPane()

        val text = VBox()
        val menuBar = MenuBar()
        val menu = Menu("File")
        val menuItem = MenuItem("Config")
        menuItem.setOnAction {
            val window = StackPane()
            val form = VBox()
            val inputLabel = Label("Entrez votre pseudo")
            val input = TextField()
            val bg = Rectangle(HEIGHT,(0.4 * WIDTH))
            val btn = Button("valider")
            bg.fill = Color.WHITE
            input.maxWidth = HEIGHT
            form.children.addAll(inputLabel,input,btn)
            window.children.addAll(bg,form)
            root.children.add(window)
            window.alignment = Pos.CENTER
            form.alignment = Pos.CENTER
            input.setOnKeyPressed { if(it.code == KeyCode.ENTER){pseudo = input.text;root.children.remove(window)} }
            btn.setOnAction { pseudo = input.text;root.children.remove(window) }
        }
        val quit = MenuItem("Quit")
        quit.setOnAction{ Platform.exit()}
        menu.items.addAll(menuItem,quit)
        menuBar.menus.add(menu)

        val chatBox = TextField()
        val send = HBox()

        send.minWidth(HEIGHT)
        val btnSend = Button("Envoyer")
        btnSend.maxWidth = 100.0
        btnSend.setOnAction {
            send(pseudo, chatBox, see, text)
        }
        send.children.addAll(chatBox,btnSend)
        see.isFitToWidth
        see.padding = Insets(5.0)
        see.content = text
        send.alignment = Pos.BOTTOM_CENTER
        val chat = BorderPane(see,menuBar,null,send,null)
        root.children.add(chat)

        primaryStage.title = "Hello World"
        primaryStage.scene = Scene(root, HEIGHT, WIDTH)
        primaryStage.show()

        chatBox.setOnKeyPressed {
            if (it.code == KeyCode.ENTER){
                send(pseudo, chatBox, see, text)
            }
        }
        launch{
            majMessage(text, root, chatBox)
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Main::class.java)
        }
    }
}
fun send(pseudo:String, chatBox:TextField, see:ScrollPane, text:VBox){
    val user = newUserChatBean(pseudo,null,CODE)
    if (pseudo.isBlank()){
        val error = Text("Systeme: Erreur! Pseudo invalide!")
        text.children.add(error)

    }
    else{
        user.message = chatBox.text
        sendPostOkHttpRequest("http://127.0.0.1:8080/newChat", gson.toJson(user))
        chatBox.text = ""
    }

}
