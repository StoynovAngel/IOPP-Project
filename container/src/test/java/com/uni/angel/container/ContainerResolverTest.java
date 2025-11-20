package com.uni.angel.container;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContainerResolverTest {

	private List<String> runAndCapture(Runnable runnable) {
		Logger logger = (Logger) LoggerFactory.getLogger(ContainerResolver.class);
		ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
		listAppender.start();
		logger.addAppender(listAppender);

		try {
			runnable.run();
		} finally {
			logger.detachAppender(listAppender);
		}
		return listAppender.list.stream()
				.map(ILoggingEvent::getFormattedMessage)
				.toList();
	}

	@Test
	void solve_withExampleContainers_capacity10() {
		List<String> messages = runAndCapture(() -> ContainerResolver.solve(ExampleContainers.getContainers(), 10));
		assertTrue(messages.contains("Maximum value = 20"), "Optimal value should be 20 for capacity 10");
		List<String> solutionLines = messages.stream().filter(m -> m.startsWith("x1")).toList();
		assertFalse(solutionLines.isEmpty(), "Should log at least one solution line");
		assertTrue(solutionLines.contains("x1 = 5 x2 = 0 x3 = 0 x4 = 0 "), "Should include solution using five containers of weight 2");
	}

	@Test
	void solve_zeroCapacity() {
		List<String> messages = runAndCapture(() -> ContainerResolver.solve(ExampleContainers.getContainers(), 0));
		assertTrue(messages.contains("Maximum value = 0"));
		List<String> solutionLines = messages.stream().filter(m -> m.startsWith("x1")).toList();
		assertEquals(1, solutionLines.size());
		assertEquals("x1 = 0 x2 = 0 x3 = 0 x4 = 0 ", solutionLines.get(0));
	}

	@Test
	void solve_smallCapacity3() {
		List<String> messages = runAndCapture(() -> ContainerResolver.solve(ExampleContainers.getContainers(), 3));
		// Capacity 3 can be filled optimally by either one weight-3 container (value 6) or combinations of weight-2 (value 4) + remainder.
		assertTrue(messages.contains("Maximum value = 6"));
	}
}