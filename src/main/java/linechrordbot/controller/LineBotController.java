package linechrordbot.controller;

import static java.util.Collections.singletonList;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.UnknownEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.group.GroupMemberCountResponse;
import com.linecorp.bot.model.group.GroupSummaryResponse;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.ImagemapExternalLink;
import com.linecorp.bot.model.message.imagemap.ImagemapVideo;
import com.linecorp.bot.model.message.imagemap.MessageImagemapAction;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;
import com.linecorp.bot.model.message.sender.Sender;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.ConfirmTemplate;
import com.linecorp.bot.model.message.template.ImageCarouselColumn;
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.model.room.RoomMemberCountResponse;
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
		ButtonsTemplate buttonsTemplate = new ButtonsTemplate(imageUrl, "歡迎來到和弦機器人", "choose your favorite way to practice!!",
				Arrays.asList(new URIAction("更多免費音樂學習資源", URI.create("https://youtube.com"), null),
						new PostbackAction("音程練習", "音程練習"),
						new PostbackAction("和弦練習", "和弦練習", "和弦練習")));
		TemplateMessage templateMessage = new TemplateMessage("Button alt text", buttonsTemplate);
		this.reply(replyToken, templateMessage);
    }

	private void handleTextContent(String replyToken, Event event, TextMessageContent content) throws Exception {
		final String text = content.getText();

		log.info("Got text message from replyToken:{}: text:{} emojis:{}", replyToken, text, content.getEmojis());
		switch (text) {
		case "profile": {
			log.info("Invoking 'profile' command: source:{}", event.getSource());
			final String userId = event.getSource().getUserId();
			if (userId != null) {
				if (event.getSource() instanceof GroupSource) {
					lineMessagingClient.getGroupMemberProfile(((GroupSource) event.getSource()).getGroupId(), userId)
							.whenComplete((profile, throwable) -> {
								if (throwable != null) {
									this.replyText(replyToken, throwable.getMessage());
									return;
								}

								this.reply(replyToken,
										Arrays.asList(new TextMessage("(from group)"),
												new TextMessage("Display name: " + profile.getDisplayName()),
												new ImageMessage(profile.getPictureUrl(), profile.getPictureUrl())));
							});
				} else {
					lineMessagingClient.getProfile(userId).whenComplete((profile, throwable) -> {
						if (throwable != null) {
							this.replyText(replyToken, throwable.getMessage());
							return;
						}

						this.reply(replyToken,
								Arrays.asList(new TextMessage("Display name: " + profile.getDisplayName()),
										new TextMessage("Status message: " + profile.getStatusMessage())));

					});
				}
			} else {
				this.replyText(replyToken, "Bot can't use profile API without user ID");
			}
			break;
		}
		case "confirm": {
			ConfirmTemplate confirmTemplate = new ConfirmTemplate("想找地方媽媽?", new MessageAction("Yes", "Yes!"),
					new MessageAction("No", "No!"));
			TemplateMessage templateMessage = new TemplateMessage("Confirm alt text", confirmTemplate);
			this.reply(replyToken, templateMessage);
			break;
		}
		case "buttons": {
			URI imageUrl = createUri("buttons/1040.jpg");
			log.info("imageUrl: {}",imageUrl);
			ButtonsTemplate buttonsTemplate = new ButtonsTemplate(imageUrl, "My button sample", "Hello, my button",
					Arrays.asList(new URIAction("Go to line.me", URI.create("https://line.me"), null),
							new PostbackAction("Say hello1", "hello こんにちは"),
							new PostbackAction("言 hello2", "hello こんにちは", "hello こんにちは"),
							new MessageAction("Say message", "Rice=米")));
			TemplateMessage templateMessage = new TemplateMessage("Button alt text", buttonsTemplate);
			this.reply(replyToken, templateMessage);
			break;
		}
		case "carousel": {
			URI imageUrl = createUri("/static/buttons/1040.jpg");
			CarouselTemplate carouselTemplate = new CarouselTemplate(
					Arrays.asList(new CarouselColumn(imageUrl, "hoge", "fuga",
							Arrays.asList(new URIAction("Go to line.me", URI.create("https://line.me"), null),
									new URIAction("Go to line.me", URI.create("https://line.me"), null),
									new PostbackAction("Say hello1", "hello こんにちは"))),
							new CarouselColumn(
									imageUrl, "hoge", "fuga", Arrays
											.asList(new PostbackAction("言 hello2", "hello こんにちは", "hello こんにちは"),
													new PostbackAction("言 hello2", "hello こんにちは", "hello こんにちは"),
													new MessageAction("Say message", "Rice=米"))),
							new CarouselColumn(imageUrl, "Datetime Picker", "Please select a date, time or datetime",
									Arrays.asList(DatetimePickerAction.OfLocalDatetime.builder().label("Datetime")
											.data("action=sel").initial(LocalDateTime.parse("2017-06-18T06:15"))
											.min(LocalDateTime.parse("1900-01-01T00:00"))
											.max(LocalDateTime.parse("2100-12-31T23:59")).build(),
											DatetimePickerAction.OfLocalDate.builder().label("Date")
													.data("action=sel&only=date").initial(LocalDate.parse("2017-06-18"))
													.min(LocalDate.parse("1900-01-01"))
													.max(LocalDate.parse("2100-12-31")).build(),
											DatetimePickerAction.OfLocalTime.builder().label("Time")
													.data("action=sel&only=time").initial(LocalTime.parse("06:15"))
													.min(LocalTime.parse("00:00")).max(LocalTime.parse("23:59"))
													.build()))));
			TemplateMessage templateMessage = new TemplateMessage("Carousel alt text", carouselTemplate);
			this.reply(replyToken, templateMessage);
			break;
		}
		case "image_carousel": {
			URI imageUrl = createUri("/static/buttons/1040.jpg");
			ImageCarouselTemplate imageCarouselTemplate = new ImageCarouselTemplate(Arrays.asList(
					new ImageCarouselColumn(imageUrl,
							new URIAction("Goto line.me", URI.create("https://line.me"), null)),
					new ImageCarouselColumn(imageUrl, new MessageAction("Say message", "Rice=米")),
					new ImageCarouselColumn(imageUrl, new PostbackAction("言 hello2", "hello こんにちは", "hello こんにちは"))));
			TemplateMessage templateMessage = new TemplateMessage("ImageCarousel alt text", imageCarouselTemplate);
			this.reply(replyToken, templateMessage);
			break;
		}
		case "imagemap":
			// final String baseUrl,
			// final String altText,
			// final ImagemapBaseSize imagemapBaseSize,
			// final List<ImagemapAction> actions) {
			this.reply(replyToken,
					ImagemapMessage.builder().baseUrl(createUri("/static/rich")).altText("This is alt text")
							.baseSize(new ImagemapBaseSize(1040, 1040))
							.actions(Arrays.asList(
									URIImagemapAction.builder().linkUri("https://store.line.me/family/manga/en")
											.area(new ImagemapArea(0, 0, 520, 520)).build(),
									URIImagemapAction.builder().linkUri("https://store.line.me/family/music/en")
											.area(new ImagemapArea(520, 0, 520, 520)).build(),
									URIImagemapAction.builder().linkUri("https://store.line.me/family/play/en")
											.area(new ImagemapArea(0, 520, 520, 520)).build(),
									MessageImagemapAction.builder().text("URANAI!")
											.area(new ImagemapArea(520, 520, 520, 520)).build()))
							.build());
			break;
		case "imagemap_video":
			this.reply(replyToken, ImagemapMessage.builder().baseUrl(createUri("/static/imagemap_video"))
					.altText("This is an imagemap with video").baseSize(new ImagemapBaseSize(722, 1040))
					.video(ImagemapVideo.builder()
							.originalContentUrl(createUri("/static/imagemap_video/originalContent.mp4"))
							.previewImageUrl(createUri("/static/imagemap_video/previewImage.jpg"))
							.area(new ImagemapArea(40, 46, 952, 536))
							.externalLink(new ImagemapExternalLink(URI.create("https://example.com/see_more.html"),
									"See More"))
							.build())
					.actions(singletonList(MessageImagemapAction.builder().text("NIXIE CLOCK")
							.area(new ImagemapArea(260, 600, 450, 86)).build()))
					.build());
			break;
		case "flex":
			this.reply(replyToken, new ExampleFlexMessageSupplier().get());
			break;
		case "quickreply":
			this.reply(replyToken, new MessageWithQuickReplySupplier().get());
			break;
		case "no_notify":
			this.reply(replyToken, singletonList(new TextMessage("This message is send without a push notification")),
					true);
			break;
		case "icon":
			this.reply(replyToken, TextMessage.builder().text("Hello, I'm cat! Meow~")
					.sender(Sender.builder().name("Cat").iconUrl(createUri("/static/icon/cat.png")).build()).build());
			break;
		default:
			log.info("Returns echo message {}: {}", replyToken, text);
			this.replyText(replyToken, text);
			break;
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

//	@EventMapping
//	public Message handleTextMessageEvent(
//			MessageEvent<TextMessageContent> event
//	) {
//		log.info("event: " + event);
//		final String originalMessageText = event.getMessage().getText();
//		return new TextMessage(originalMessageText);
//	}
//
//	@EventMapping
//	public void handleDefaultMessageEvent(
//			Event event
//	) {
//		System.out.println("event: " + event);
//	}
}
