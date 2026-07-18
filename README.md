# LBYCPOB Module 5A — Hangman Web App

Spring Boot + Thymeleaf migration of the console Hangman game, built against
`LBYCPOB_Manual_MODULE5A-HANGMAN` and reusing the word lists / ASCII art from
the Module 4 console project (`HangmanGameApp.zip`).

## Before you open this in IntelliJ

1. Edit `src/main/resources/templates/fragments/layout.html` and replace
   `ACHID — replace with your Full Name, Section` with your actual
   `FirstName M.I. Surname, LBYCPOB-Section ID#` — this is the "display your
   name in the header/footer" identification the deliverables sheet asks for.
2. **Build your git history before opening IntelliJ**, so IntelliJ doesn't
   lock any files: from a terminal, inside this folder, run:
   ```
   ./rebuild_history.sh
   ```
   This splits the project into 34 commits (nothing over 100 changed lines,
   tested and verified - see the script's own header comment) instead of one
   dump, using your own `git config user.name`/`user.email`. Then create an
   empty repo on GitHub (no README/gitignore from GitHub's side) and:
   ```
   git remote add origin <your-repo-url>
   git branch -M main
   git push -u origin main
   ```
3. Open the folder as a Maven project in IntelliJ (JDK 25, as in the manual).
4. Run `HangmanSpringBootApplication`. Take your "About IntelliJ" screenshot
   at this point, per the deliverables sheet.
5. Open `http://localhost:8080/` in a browser.

## What's done

- All 11 Java classes, transcribed from the manual's reference
  implementation package-for-package (`controller`, `model`, `render`,
  `repository`, `service`, `statistics`, `utils`).
- `pom.xml` and `application.properties`, matching the manual.
- `index.html`, `play.html`, `stats.html`, and a shared `fragments/layout.html`
  header/footer — these are **not** given in the manual (only shown as
  screenshots), so this is an original implementation of that layout.
- `static/css/style.css` — original styling to match the manual's screenshots.
- Word lists (`test.txt`, `words.txt`, `large.txt`) and ASCII art
  (`display0.txt`–`display8.txt`) copied unchanged from your Module 4 console
  project.
- `GUIDE_QUESTIONS.md` — draft answers to manual section 2.10.
- Every file carries an `// AI-CHECK:` comment identifying it as AI-assisted
  and naming something specific to verify, per your course's own annotation
  policy (see `GUIDE_QUESTIONS.md` for why this matters this time around).

## What you still need to do yourself

These all require your own IDE, browser, and GitHub account — nothing here
can substitute for them:

- **2.5/2.6 screenshots** — the IntelliJ "About" run screenshot, the
  welcome page, and two full playthroughs (one win, one loss) plus the
  statistics page.
- **2.7 UML sequence diagram** — the manual already shows a "Play One Game"
  sequence diagram that matches this code's request flow; redraw it yourself
  in [diagrams.net](https://app.diagrams.net/) with your name on it, since
  the deliverable is explicitly "draw your own."
- **2.8 Git commit history + contribution graph** — run `rebuild_history.sh`
  (see above) to get a real, verified 34-commit history, push it to GitHub,
  then screenshot the commit history and, if this is group work, the
  Contributors graph under your repo's Insights tab.
- **2.9 UML class diagram** — same idea as the sequence diagram: the manual's
  example class diagram already matches this project's classes; redraw it
  yourself with your "About" identification.
- **2.10 Guide questions** — review `GUIDE_QUESTIONS.md` and rewrite the
  answers in your own words before submitting.
