package zvooky.enhanced.services.download;

import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import zvooky.enhanced.configs.properties.CommandPropertiesHolder;
import zvooky.enhanced.webhookbot.MessageSender;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class DownloadAndSendTrackService implements Runnable {

    private final String url;
    private final MessageSender messageSender;
    private final CommandPropertiesHolder commandHolder;
    private String filename;
    private final long chatId;

    private final ProcessBuilder processBuilder;

    public DownloadAndSendTrackService(String url,
                                       MessageSender messageSender,
                                       CommandPropertiesHolder commandHolder,
                                       long chatId) {
        this.url = url;
        this.messageSender = messageSender;
        this.commandHolder = commandHolder;
        this.chatId = chatId;
        processBuilder = new ProcessBuilder();
    }

    @Override
    public void run() {
        try {
            getFilenameByUrl(url);
            if (filename != null) {
                downloadTrackByUrl();
                CompletableFuture<?> future = sendAudio();
                if (future != null) {
                    future.get();
                }
                deleteFile();
            }
        } catch (RuntimeException |
                 IOException |
                 InterruptedException |
                 ExecutionException e) {//find out if it should be handled differently
            log.error(e.getMessage(), e);
            SendMessage reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText("Ошибка при скачивании файла");
            messageSender.sendTextMessage(reply);
        }
    }

    private void getFilenameByUrl(String url) throws IOException, InterruptedException {
        processBuilder.command(
                commandHolder.getProgram(),
                commandHolder.getOutputOption(),
                commandHolder.getOutputFormat(),
                url,
                commandHolder.getFilenameOption(),
                commandHolder.getRestrictFilenameOption());
        Process process = processBuilder.start();
        process.waitFor();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        process.getInputStream()));
        BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        log.info("yt-dlp error message: {}", error.readLine());
        filename = bufferedReader.readLine();
        if (filename == null) {
            SendMessage reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText("Плохая ссылка " + url + ", вставь другую");
            messageSender.sendTextMessage(reply);
        }
    }

    private void downloadTrackByUrl() throws IOException, InterruptedException {
        try {
            messageSender.sendTextMessage(messageSender.constructTextMessage(
                    "Загрузка трека\n" + filename + "\nначалась",
                    chatId
            ));

            processBuilder.command(//yt-dlp -x
                    commandHolder.getProgram(),//yt-dlp
                    commandHolder.getAudioOption(),//-x
                    commandHolder.getAudioFormatOption(),//--audio-format
                    commandHolder.getAudioFormat(),//mp3
                    commandHolder.getOutputOption(),//--output
                    commandHolder.getOutputFormat(),//"%(title)s.mp3"
                    url,
                    commandHolder.getRestrictFilenameOption());//--restrict-filename

            Process process = processBuilder.start();

            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            log.info("yt-dlp error message: {}", error.readLine());
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
            SendMessage reply = new SendMessage();
            reply.setChatId(chatId);
            reply.setText("Ошибка при скачивании файла");
            messageSender.sendTextMessage(reply);
        }
    }

    private CompletableFuture<?> sendAudio() {
        try {
            SendAudio audioToSend = new SendAudio();
            File directory = new File("/usr/app");
            for (File file : directory.listFiles()) {
                String name = file.getName();
                if (name.contains(filename)) {
                    filename = name;
                }
            }
            filename=filename.substring(0,filename.length()-1);
            audioToSend.setAudio(new InputFile(new File(filename)));
            audioToSend.setChatId(chatId);
            return messageSender.sendAudio(audioToSend);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private void deleteFile() {
        File track = new File(filename);
        track.delete();
    }
}
