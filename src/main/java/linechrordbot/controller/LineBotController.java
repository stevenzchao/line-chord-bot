package linechrordbot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@LineMessageHandler
public class LineBotController {

//	@PostMapping(value = "/callback")
//	public String test(
//			HttpServletRequest request,
//			@RequestBody Object obj
//	) {
//		log.info("in callback!!!!!!!!!!");
//		System.out.println(request.toString());
//		System.out.println(obj);
//
//		return "Hey Chord Bot User!";
//	}

	@EventMapping
	public Message handleTextMessageEvent(
			MessageEvent<TextMessageContent> event
	) {
		log.info("event: " + event);
		final String originalMessageText = event.getMessage().getText();
		return new TextMessage(originalMessageText);
	}

	@EventMapping
	public void handleDefaultMessageEvent(
			Event event
	) {
		System.out.println("event: " + event);
	}
}
