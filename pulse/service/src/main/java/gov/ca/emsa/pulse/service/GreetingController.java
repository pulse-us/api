package gov.ca.emsa.pulse.service;

import java.util.concurrent.atomic.AtomicLong;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import gov.ca.emsa.pulse.auth.Greeting;

@Api(value = "greeting")
@RestController
@RequestMapping("/greeting")
public class GreetingController {
    private final static String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

	@ApiOperation(value="Get greeting",
                  notes="Users can optionally add their name to the greeting, using the 'name' parameter.")
    @RequestMapping("/greeting")
    public Greeting greeting (@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}
