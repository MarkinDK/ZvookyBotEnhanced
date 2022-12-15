package zvooky.enhanced.services.update;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import zvooky.enhanced.webhookbot.MessageSender;

public interface UpdateHandlerService {
    BotApiMethod<?> handleUpdate(MessageSender bot, Update update);
}
