import javazoom.jl.player.Player
import kotlinx.coroutines.*
import java.net.http.HttpClient
import java.net.http.HttpResponse

const val GENERATE_SOUND_URL = "https://nextup.com/ivona/php/nextup-polly/CreateSpeech/CreateSpeechGet3.php?voice=Maxim&language=ru-RU&text="

val textLines = listOf(
    "А, мой, мальчик, едет на девятке\n",
    "По автостраде, вдоль ночных дорог\n",
    "Я, круужилась, с ним на танцплощадке\n",
    "А ты и дальше, будешь одинок\n"
)

suspend fun main() = withContext(Dispatchers.IO) {
    val words = textLines.flatMap { line -> line.split(" ") }
    val sounds = with(HttpClient.newHttpClient()) {
        textLines.map { line ->
            val soundUrl = load(
                uri = (GENERATE_SOUND_URL + line.toXWwwFormUrl()).toURI(),
                bodyHandler = HttpResponse.BodyHandlers.ofString()
            )
            load(uri = soundUrl.toURI(), bodyHandler = HttpResponse.BodyHandlers.ofInputStream())
        }
    }
    coroutineScope {
        launch {
            words.executeWithDelay(
                action = { word -> println("$word ") },
                delayTime = { index, _ -> writeWordDelays.getOrNull(index) }
            )
        }
        launch {
            sounds.executeWithDelay(
                action = { sound -> Player(sound).play() },
                delayTime = { _, _ -> 400L }
            )
        }
    }
    pictureLines.executeWithDelay(action = ::println, delayTime = { _, _ -> 75L })
}

private val pictureLines = listOf(
    "+++++++++++++==+++++++++++++++++++++================+======+=+===+============++++++=++++++++++++++++====++=============",
    "+++++++++++++++++++=+++++++++==========================%%%%%%===========================================================",
    "+++++++++++++++++++++=++++++====================%@@###############@%====================================================",
    "++++++++++++++===+======+++==================@################@#######%=================================================",
    "++++++++++==+===+++++=+++=+===============%@############################@%==============================================",
    "+++++++++=======++++=+++++==============%@################################@%============================================",
    "+++++=+======++++====++==+=============@########################@@##########%%==========================================",
    "++++=++=+====+======+++==============%####################%=======%@@@#######@%=========================================",
    "+++++++==++=++===+++====+===========%###################%=+++++++++==%@########%========================================",
    "+++++++==+++=======================%##################@%+******++++++=%@########%=======================================",
    "++++++++++++=+====================%#################@%=+*********++++++%#########@%=====================================",
    "++++++++++++=====================%@###############@%==+***********+++++=@##########%====================================",
    "++++++++++=======================@###############@==++*************++++=%@##########%===================================",
    "+++++++++=======================@###############@==+*******:******+==%%==%###########@%=================================",
    "++++++++++++======+============@@##############@@%=++********+%@@%========%###########@%================================",
    "+++++++++++==================%%%###############@%=====++****+==%@#####%++=%############@%===============================",
    "+++++++++++=================%%%%##############@%####@%=++**+====+=%%%=+++=%@###########@%===============================",
    "+++++++++++===============%%%%%###############%%@%=%%==+++*+==++++*****++=%@############@%==============================",
    "++++++++++===============%@@@#################@===+++++*++*+==++*******++=%@############@%==============================",
    "+++++++++==============%%%####################@=+++******+++=%=+******++=%@##############@%=============================",
    "+++++++++=============%%@######################%++******++++==%+******+==%@##############@%=============================",
    "+++++++++===========%%@#########################=+*******+++=%=+*****++=%%@###############@%============================",
    "+++++++++++========%=%###########################=+*******+**+++*****++=%%@################@@%==========================",
    "+++++++++++========%=@###########################@=+******++===+++**+++==%##################@@@%========================",
    "++++++++++++=======%%@#############################=+***+=%%==%@%%++++==%#####################@@@%======================",
    "+++++++++++=========%###############################%+***+========+++==%#######################@@@@@@@%=================",
    "+++++++++=========%###################################=+*****++*++++=%%@#########################@@@@@@@%===============",
    "+++++++++=======@#######################################=********+====%%@#########################@@@@@@@@%=============",
    "++++++++========%#######################################@%=+++++=======%@##########################@@@@@@@%=============",
    "+++++++========%%######################################@@%==============@###########################@@@@@@%=============",
    "++++++========%@#####################################@@@%%==============@#############################@@@@@%============",
    "+++++++======%@#####################################@%%%%==============%###############################@@@@@@@@%========",
    "+++++++=====%@######################################%%=%%====+++++++===%################################@@@@@@@@%=======",
    "++++++===%%#########################################%======++++++++++++%################################@@@@@@@%@%======",
    "++++++==%#@#########################################%====%==++++++++++=@#################################@@@@@@%@%======",
    "+++++===@##########################################@%=================+##################################@@@@@@@@%======",
    "+++++==%###########################################@====++++++++++++==%##################################@@@@@@@@@@@%===",
    "+++++=%############################################@==++++++++++++++++####################################@@@@@@@@@@%===",
    "++++==#############################################@=++++++*****+++++%#####################################@@@@@@@@@%==="
)

private val writeWordDelays = longArrayOf(
    400,
    400,
    800,
    450,
    200,
    700,
    500,
    1000,
    600,
    600,
    700,
    500,
    900,
    600,
    600,
    600,
    700,
    500,
    400,
    400,
    600,
    600
)