package zvooky.enhanced.services.update;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import zvooky.enhanced.configs.properties.CommandPropertiesHolder;
import zvooky.enhanced.services.download.DownloadAndSendTrackService;
import zvooky.enhanced.utils.UrlManager;
import zvooky.enhanced.webhookbot.MessageSender;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class UpdateHandlerServiceImpl implements UpdateHandlerService {

    private final Set<String> users = new HashSet<>();

    private final int MY_CHAT_ID_1 = 1;
    private final int MY_CHAT_ID_2 = 2;

    private final ExecutorService executorService;
    private final CommandPropertiesHolder commandHolder;

    private final UrlManager urlManager;

    @Value("${replies.insert_an_url}")
    private String INSERT_AN_URL;
    @Value("${replies.bad_url}")
    private String BAD_URL;
    @Value("${replies.download_started}")
    private String DOWNLOAD_STARTED;

    @Value("${replies.welcome}")
    private String WELCOME;

    @Value("${replies.error}")
    private String ERROR;

    public UpdateHandlerServiceImpl(CommandPropertiesHolder commandHolder, UrlManager urlManager) {
        this.commandHolder = commandHolder;
        executorService = Executors.newCachedThreadPool();
        this.urlManager = urlManager;
    }

    @Override
    public BotApiMethod<?> handleUpdate(MessageSender bot, Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            User user = update.getMessage().getFrom();
            long chatId = update.getMessage().getChatId();
            if ((chatId == MY_CHAT_ID_1 || chatId == MY_CHAT_ID_2) && update.getMessage().getText().equals("Get logs")) {
                bot.sendDocument(new SendDocument(String.valueOf(chatId), new InputFile(new File("app.log"))));
            } else if (!users.contains(user.getUserName())) {//check if first request from user
                users.add(user.getUserName());
                log.info("New user username=@{} firstname={} added", user.getUserName(), user.getFirstName());

                bot.sendTextMessage(bot.constructTextMessage(INSERT_AN_URL, chatId));
            } else {
                String url = urlManager.refineUrl(update.getMessage().getText());

                if (url.equals("")) {
                    bot.sendTextMessage(bot.constructTextMessage(BAD_URL + INSERT_AN_URL, chatId));
                } else {
                    log.info("New thread \"DownloadAndSendTrackService\" is about to start");
                    executorService.submit(new DownloadAndSendTrackService(url, bot, commandHolder, chatId));
                }
            }
        }
        return null;
    }

}
