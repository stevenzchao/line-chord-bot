package linechrordbot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LineBotController {

	@PostMapping("/callback")
	public String test() {
		System.out.println();
		return "Hey Chord Bot User!";
	}
}
