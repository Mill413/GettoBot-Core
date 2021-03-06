package top.harumill.getto.bot.handler

import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.content
import top.harumill.getto.bot.handler.GettoCommandManager.NameToCommand
import top.harumill.getto.bot.logger.GettoLogger
import top.harumill.getto.bot.logger.Priority
import top.harumill.getto.function.Command
import top.harumill.getto.tools.getObjectByName

/**
 * CommandManager负责监听消息事件并解析消息内容，若消息为Command则将参数传递给对应的Command
 * @property NameToCommand 指令名与指令的映射，指令名为配置文件中"commandName"的值
 */
object GettoCommandManager {
    private val NameToCommand = mutableMapOf<String, Command>()

    private const val prefix = "/"

    private const val delims = " "

    private fun flushCommandMap() {
        val allFunction = GettoConfigManager.getFunctionsList()
        allFunction.forEach { function ->
            val config = GettoConfigManager.getFunctionConfig(function)
            requireNotNull(config) {
                GettoLogger.log(Priority.ERROR, "Config of Function-${function} Cannot be Null!", "CommandManager")
            }
            if (config.commandName.isNotBlank() && config.commandName.isNotEmpty()) {
                NameToCommand["${if (config.prefixEnable) prefix else ""}${config.commandName}"] =
                    getObjectByName("top.harumill.getto.function.functions.$function") as Command
            }
        }
    }

    private fun isCommand(str: String): Boolean {
        return str in NameToCommand
    }

    private fun splitCommand(cmd: String): List<String> {
        return cmd.removePrefix(prefix).split(delims)
    }

    fun onEnable() {
        flushCommandMap()

        GettoEventHandler.listenEvent<MessageEvent> { event ->
            val msg = event.message.content
            val list = splitCommand(msg)
            val cmd = list[0]

            if (isCommand(cmd)) {

                val params = if (list.size >= 2) list.subList(1, list.size) else emptyList()
                val command = NameToCommand[cmd]
                requireNotNull(command) {
                    GettoLogger.log(
                        Priority.ERROR,
                        "$cmd cannot find correspondent Command Object!",
                        "CommandManager"
                    )
                }
                command.invoke(params)

                command.onEnable(event.subject, event.sender)
            }

        }
    }
}