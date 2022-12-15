package zvooky.enhanced.webhookbot;

import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface MessageSender {
    void sendAudio(SendAudio audio);
    void sendTextMessage(SendMessage message);

    SendMessage constructTextMessage(String text, Long chatId);
}
