package linechrordbot.controller;

import static java.util.Collections.singletonList;

import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.UnknownEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@LineMessageHandler
public class LineBotController {

	@Autowired
	private LineMessagingClient lineMessagingClient;

	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		TextMessageContent message = event.getMessage();
		handleTextContent(event.getReplyToken(), event, message);

	}
	
    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        log.info("unfollowed this bot: {}", event);
    }

    @EventMapping
    public void handleUnknownEvent(UnknownEvent event) {
        log.info("Got an unknown event!!!!! : {}", event);
    }
    
    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        String replyToken = event.getReplyToken();
		URI imageUrl = createUri("buttons/1.jpg");
		log.info("imageUrl: {}",imageUrl);
		this.reply(replyToken, new ExampleFlexMessageSupplier().get());

    }

	private void handleTextContent(String replyToken, Event event, TextMessageContent content) throws Exception {
		final String text = content.getText();

		log.info("Got text message from replyToken:{}: text:{} emojis:{}", replyToken, text, content.getEmojis());
		
		if("和弦查詢".equals(text)) {
            this.replyText(
                    replyToken,
                    "請輸入 find + (想查詢的和弦)： \r\n Ex: find Cm  ->> C(根音)m7(和弦種類)"
            );	
		}else if(text.contains("find")) {
			log.info("search for chord {}: {}", replyToken, text);
			log.info("search for chord {}: {}", replyToken, text.substring(5));
            this.reply(replyToken, new ChordFlexMessageSupplier().apply(text.substring(5)));
		}else {
			log.info("Returns echo message {}: {}", replyToken, text);
          this.reply(replyToken, new ExampleFlexMessageSupplier().get());
		}
		
	}

	private void replyText(@NonNull String replyToken, @NonNull String message) {
		if (replyToken.isEmpty()) {
			throw new IllegalArgumentException("replyToken must not be empty");
		}
		if (message.length() > 1000) {
			message = message.substring(0, 1000 - 2) + "……";
		}
		this.reply(replyToken, new TextMessage(message));
	}

	private void reply(@NonNull String replyToken, @NonNull Message message) {
		reply(replyToken, singletonList(message));
	}

	private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
		reply(replyToken, messages, false);
	}

	private void reply(@NonNull String replyToken, @NonNull List<Message> messages, boolean notificationDisabled) {
		try {
			BotApiResponse apiResponse = lineMessagingClient
					.replyMessage(new ReplyMessage(replyToken, messages, notificationDisabled)).get();
			log.info("Sent messages: {}", apiResponse);
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

	private static URI createUri(String path) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().scheme("https").path(path).build().toUri();
	}
}
