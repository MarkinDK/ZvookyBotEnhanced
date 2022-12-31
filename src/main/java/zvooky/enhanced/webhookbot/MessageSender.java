package zvooky.enhanced.webhookbot;

import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.concurrent.CompletableFuture;

public interface MessageSender {
    CompletableFuture<?> sendAudio(SendAudio audio);
    void sendTextMessage(SendMessage message);

    SendMessage constructTextMessage(String text, Long chatId);
}
