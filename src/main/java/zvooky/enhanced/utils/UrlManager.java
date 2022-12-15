package zvooky.enhanced.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@ConfigurationProperties(prefix = "url-patterns")
@Getter
@Setter
@PropertySource("classpath:application.yml")
public class UrlManager {
    //    @Value("${standard}")
    private String STANDARD_URL_PATTERN;// = "(https://www\\.youtube\\.com/watch\\?v=)[\\w]{11}";
    //    @Value("${m}")
    private String M_URL_PATTERN;// = "(https://m\\.youtube\\.com/watch\\?v=)[\\w]{11}";
    //    @Value("${be}")
    private String BE_URL_PATTERN;// = "(https://youtu\\.be/)[\\w]{11}";

    private final Pattern patternStandard;
    private final Pattern patternM;
    private final Pattern patternBe;

    public UrlManager(
            @Value("${standard}") String STANDARD_URL_PATTERN,
            @Value("${m}") String M_URL_PATTERN,
            @Value("${be}") String BE_URL_PATTERN
    ) {
        patternStandard = Pattern.compile(STANDARD_URL_PATTERN);
        patternM = Pattern.compile(M_URL_PATTERN);
        patternBe = Pattern.compile(BE_URL_PATTERN);
        this.STANDARD_URL_PATTERN = STANDARD_URL_PATTERN;
        this.M_URL_PATTERN = M_URL_PATTERN;
        this.BE_URL_PATTERN = BE_URL_PATTERN;
    }

    public boolean isUrlCorrect(String url) {

        Matcher matcherStandard = patternStandard.matcher(url);
        Matcher matcherM = patternM.matcher(url);
        Matcher matcherBe = patternBe.matcher(url);
        return url.contains("https://www.youtube.com/watch")
                || url.contains("https://m.youtube.com/watch")
                || url.contains("https://youtu.be/");
    }

    public String refineUrl(String text) {

        Matcher matcherStandard = patternStandard.matcher(text);
        Matcher matcherM = patternM.matcher(text);
        Matcher matcherBe = patternBe.matcher(text);
        String result = "";
        if (matcherStandard.find()) {
            result = matcherStandard.group();
        } else if (matcherM.find()) {
            result = matcherM.group();
        } else if (matcherBe.find()) {
            result = matcherBe.group();
        }
        return result;
    }

}