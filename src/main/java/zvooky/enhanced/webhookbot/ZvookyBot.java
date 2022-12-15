package zvooky.enhanced.webhookbot;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import zvooky.enhanced.services.update.UpdateHandlerService;

@Slf4j
public class ZvookyBot extends TelegramWebhookBot implements MessageSender {
    @Getter
    @Setter
    private String username;
    @Getter
    @Setter
    private String token;
    @Getter
    @Setter
    private String path;
    private UpdateHandlerService updateHandlerService;

    public ZvookyBot() {
        super();
    }

    public ZvookyBot(UpdateHandlerService updateHandlerService) {
        super();
        this.updateHandlerService = updateHandlerService;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return updateHandlerService.handleUpdate(this, update);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotPath() {
        return path;
    }

    @Override
    public SendMessage constructTextMessage(String text, Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }

    @Override
    public void sendAudio(SendAudio audio) {
        try {
            executeAsync(audio);
        } catch (RuntimeException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public void sendTextMessage(SendMessage message) {
        try {
            executeAsync(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
    }


}
