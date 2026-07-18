# Hangman Web App — Guide Questions (Manual Section 2.10)

> These are draft answers grounded in the actual code in this project. Read
> them, check them against your own understanding, and put them in your own
> words before you paste anything into your report — see the note at the
> bottom of this file about why that matters for this course specifically.

## Question 1

**In the context of this application, compare developing the application
from scratch using plain Java with developing it using Spring Boot in terms
of the amount of work, configuration, and built-in features available to the
developer. Explain your answer using specific examples from the Hangman app,
and ensure that the comparison is fair by focusing on equivalent
functionality rather than differences in application type.**

Building a web-facing version of Hangman in plain Java (no framework) would
require hand-writing everything Spring Boot currently provides automatically.
To let a browser reach the game at all, the app would need its own
`ServerSocket` listening on a port, code to parse raw HTTP request lines and
headers, code to route the parsed path (e.g. `/game/guess`) to the right
handler, and code to build well-formed HTTP responses — none of which exists
in the console version, since `Scanner`-based console I/O has no notion of
"requests" at all. Spring Boot replaces all of that with an embedded Tomcat
server plus `@Controller`/`@GetMapping`/`@PostMapping` annotations:
`GameController`'s seven handler methods *are* the entire routing table.

Similarly, the console app formatted every screen with `IO.println` calls; a
hand-rolled web version would need to build HTML through string
concatenation for every single response. Spring Boot's Thymeleaf integration
lets `play.html` read `state`, `hint`, `hangmanArt`, and `alphabet` directly
through `th:text`/`th:each` expressions instead of printed strings.

The console app also had no notion of "the current player's game" persisting
between actions — a plain Java web version would need its own
session-tracking mechanism (a cookie or token mapped to a `GameState` in some
in-memory `Map`) to remember whose turn it is between one HTTP request and
the next. Spring's `HttpSession` (backed by the servlet container) gives that
for free — it's why `GameController` simply takes `HttpSession session` as a
parameter and never manages a session map itself.

Importantly, the actual *game rules* — `createHint`, `getRandomWord`,
`AsciiArtRenderer`, `WordRepository` — carried over from the console app
almost unchanged. Spring Boot did not reduce the amount of game-logic code;
it eliminated the large amount of networking, parsing, templating, and
session-management code that has nothing to do with Hangman itself.

## Question 2

**Give at least five (5) examples of Spring Boot annotations (@...) from
this Application and explain their meaning or purpose.**

| Annotation Example/Snippet | Purpose/Meaning |
|---|---|
| `@SpringBootApplication`<br>`public class HangmanSpringBootApplication {...}` | Marks `HangmanSpringBootApplication` as the main entry point of the Spring Boot app and enables automatic configuration, component scanning, and application startup with a single annotation. |
| `@Controller`<br>`public class GameController {...}` | Marks `GameController` as a Spring MVC controller whose methods return *view names* (e.g. `"play"`) that Thymeleaf resolves to templates, rather than raw data. |
| `@GetMapping("/game/play")`<br>`public String play(...)` | Maps HTTP `GET` requests for `/game/play` to this method. Used for read-only requests that should be safe to refresh or bookmark. |
| `@PostMapping("/game/guess")`<br>`public String guess(...)` | Maps HTTP `POST` requests for `/game/guess` to this method. Used for requests that change state (recording a letter guess), following the Post/Redirect/Get pattern. |
| `@RequestParam("letter")`<br>`String letterInput` | Binds a submitted form field named `letter` to the `letterInput` method parameter, so Spring extracts it from the request body/query string automatically. |
| `@Service`<br>`public class HangmanService {...}` | Marks `HangmanService` as a Spring-managed bean in the business-logic layer, making it eligible for constructor injection into `GameController`. |
| `@Bean`<br>`public WordRepository wordRepository(Random random) {...}` | Tells Spring to register the object this method returns as a managed bean, used here because `ClasspathWordRepository` needs a constructor argument (a classpath path) that Spring cannot infer on its own. |
| `@Component`<br>`public class StatisticsWriter {...}` | Marks `StatisticsWriter` as a generic Spring-managed bean (infrastructure/utility code, as opposed to `@Service` for business logic or `@Controller` for the web layer). |

---

**Why "put them in your own words" matters here specifically:** your
course's lab manual requires an `// AI-CHECK:` annotation plus a verification
statement on any AI-generated code, and your Module 4 report already flagged
a transparency concern the last time a project was fully AI-generated without
that disclosure. Every file in this project carries an `// AI-CHECK:` comment
naming Claude as the source and pointing at something specific to verify —
but *verifying* is something only you can actually do. Before submission,
open each file, confirm the `// AI-CHECK:` claim is true for you (that you
ran it, read it, and understood it), and rewrite it in your own words if your
instructor expects that. The same goes for the two answers above: they're
accurate, but "accurate" and "in your own words, showing you understand it"
are different bars, and the second one is what guide questions are actually
for.
