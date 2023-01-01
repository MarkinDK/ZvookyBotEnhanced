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
    private String STANDARD_URL_PATTERN;// = "(https://www\\.youtube\\.com/watch\\?v=)[\\w]{11}";
    private String M_URL_PATTERN;// = "(https://m\\.youtube\\.com/watch\\?v=)[\\w]{11}";
    private String BE_URL_PATTERN;// = "(https://youtu\\.be/)[\\w]{11}";
    private String SHORTS_URL_PATTERN;// = "(https://youtube\\.com/shorts/)[\\w|\\-|_]{11}";
    private String WSHORTS_URL_PATTERN;// = "(https://www\\.youtube\\.com/shorts/)[\\w|\\-|_]{11}";

    private final Pattern patternStandard;
    private final Pattern patternM;
    private final Pattern patternBe;
    private final Pattern patternShorts;
    private final Pattern patternWShorts;

    public UrlManager(
            @Value("${standard}") String STANDARD_URL_PATTERN,
            @Value("${m}") String M_URL_PATTERN,
            @Value("${be}") String BE_URL_PATTERN,
            @Value("${shorts}") String SHORTS_URL_PATTERN,
            @Value("${wshorts}") String WSHORTS_URL_PATTERN
    ) {
        patternStandard = Pattern.compile(STANDARD_URL_PATTERN);
        patternM = Pattern.compile(M_URL_PATTERN);
        patternBe = Pattern.compile(BE_URL_PATTERN);
        patternShorts = Pattern.compile(SHORTS_URL_PATTERN);
        patternWShorts = Pattern.compile(WSHORTS_URL_PATTERN);
        this.STANDARD_URL_PATTERN = STANDARD_URL_PATTERN;
        this.M_URL_PATTERN = M_URL_PATTERN;
        this.BE_URL_PATTERN = BE_URL_PATTERN;
        this.SHORTS_URL_PATTERN = SHORTS_URL_PATTERN;
        this.WSHORTS_URL_PATTERN = WSHORTS_URL_PATTERN;

    }

    public String refineUrl(String text) {

        Matcher matcherStandard = patternStandard.matcher(text);
        Matcher matcherM = patternM.matcher(text);
        Matcher matcherBe = patternBe.matcher(text);
        Matcher matcherShorts = patternShorts.matcher(text);
        Matcher matcherWShorts = patternWShorts.matcher(text);
        String result = "";
        if (matcherStandard.find()) {
            result = matcherStandard.group();
        } else if (matcherM.find()) {
            result = matcherM.group();
        } else if (matcherBe.find()) {
            result = matcherBe.group();
        }else if (matcherShorts.find()) {
            result = matcherShorts.group();
        }else if (matcherWShorts.find()) {
            result = matcherWShorts.group();
        }
        return result;
    }

}