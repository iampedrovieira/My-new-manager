package Classes

import java.io.Serializable
import java.util.*

class Tarefa(): Serializable {
    lateinit var nome :String
    lateinit var desc :String
    lateinit var dataEntrega :String
    lateinit var idProjeto : String
    lateinit var idTarefa : String


}