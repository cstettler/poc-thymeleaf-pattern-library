package personal.cstettler.thymeleaf.patternlibrary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestPropertySource(properties = {
	"pattern-library.components-resource-path=classpath:/test"
})
class PocThymeleafPatternLibraryApplicationTests {

	@LocalServerPort
	private int localServerPort;

	private Playwright playwright;
	private Browser browser;
	private BrowserContext context;
	private Page page;

	@BeforeEach
	void setupPlaywright() {
		playwright = Playwright.create();
		browser = playwright.chromium().launch();
		context = browser.newContext(new Browser.NewContextOptions().setBaseURL("http://localhost:" + localServerPort));
		page = context.newPage();
	}

	@AfterEach
	void tearDownPlaywright() {
		page.close();
		context.close();
		browser.close();
		playwright.close();
	}

	@Test
	void applicationStartsSuccessfully() {
		page.navigate("/");
		String headerTitle = page.querySelector("header").textContent().trim();

		assertEquals("Thymeleaf Component Library (PoC)", headerTitle);
	}
}
