package top.harumill.getto.function.functions

import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import top.harumill.getto.function.Function

object Repeat : Function() {
    override suspend fun onAllEvent(event: Event) {
        //
    }

    override suspend fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is GroupMessageEvent && (1..1000).random() < 10) {
            val subject = messageEvent.subject
            val msg = messageEvent.message
            subject.sendMessage(msg)
        }
    }


}