package linechrordbot.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class WakeUpScheduler {

	@Scheduled(initialDelay = 3000, fixedDelay = 180000)
	public void keepHerokuAlive() {
		log.info("in keepHerokuAlive keepHerokuAlivekeepHerokuAlivekeepHerokuAlivekeepHerokuAlivekeepHerokuAlive ");
		RestTemplate rs = new RestTemplate();
		String result = rs.getForObject("https://blueberry-crumble-01036.herokuapp.com/callback keepHerokuAlive", String.class);
		log.info(result);
	}

}
