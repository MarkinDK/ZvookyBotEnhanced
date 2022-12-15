package zvooky.enhanced.configs.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "command")
@PropertySource("classpath:application.yml")
@Getter
@Setter
public class CommandPropertiesHolder {
    @Value("${program}")
    private String program;
    @Value("${output_option}")
    private String outputOption;
    @Value("${output_format}")
    private String outputFormat;
    @Value("${audio_option}")
    private String audioOption;
    @Value("${audio_format_option}")
    private String audioFormatOption;
    @Value("${audio_format}")
    private String audioFormat;
    @Value("${filename_option}")
    private String filenameOption;
    @Value("${restrict_filename_option}")
    private String restrictFilenameOption;

    public CommandPropertiesHolder() {
    }

    @Override
    public String toString() {
        return "CommandPropertiesHolder{" +
                "program='" + program + '\'' +
                ", outputOption='" + outputOption + '\'' +
                ", outputFormat='" + outputFormat + '\'' +
                ", audioOption='" + audioOption + '\'' +
                ", audioFormatOption='" + audioFormatOption + '\'' +
                ", audioFormat='" + audioFormat + '\'' +
                ", filenameOption='" + filenameOption + '\'' +
                ", restrictFilenameOption='" + restrictFilenameOption + '\'' +
                '}';
    }
}
