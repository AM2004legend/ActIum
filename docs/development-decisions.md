# Development decisions

Decisions made while preparing Actum for the Hacktoberfest 2026 DEV challenge (week 3,
"Slow and Steady"). Each one says what was decided, why, and where the evidence is.
Actum itself was built before the challenge; see the README's history note.

## 1. Measure on a held-out set, not the old 30 sentences

- **Context.** The existing accuracy figure (30 of 30) comes from a set that was used to
  find and fix faults. It is a development set, so its score says little about new
  sentences.
- **Options.** Report the 30/30 as is; enlarge that set; write a new set and freeze it.
- **Chosen.** A new set of 100 synthetic sentences in 21 kinds
  (`android/app/src/main/assets/heldout.jsonl`), written and frozen before any model run.
  It covers what the old set did not: negations, quoted text, prompt injection, OCR
  receipts, typos, Hinglish, Benglish, missing fields, long rambling input.
- **Rules for the set.** A case may allow several outcomes where people would disagree
  (for example "call at 7"). Expectations are not changed after seeing results; a wrong
  label is recorded here and every path is rerun.
- **Evidence.** `tools/heldout_score.py`, `benchmarks/heldout-*.json`,
  `benchmarks/heldout.md`.

## 2. Compare rules alone, Gemma alone and the hybrid on the same cases

- **Context.** Actum reads plain commands with deterministic rules and sends everything
  else to Gemma 4 E2B on the phone. The question is whether the split earns its place.
- **Chosen.** One phone run reads each case with the rules and with Gemma. The hybrid is
  computed from those two readings exactly as the app does for typed words: the rule's
  card when a rule reads the sentence, otherwise Gemma's. Gemma is not run twice.
- **Scoring.** A case is correct when the set of proposed intents equals an allowed set.
  A rule that stays silent proposes nothing, which is correct only where no action is
  allowed. Answers are proposals only: nothing is validated against a clock or executed
  during the run.
- **Trade-off.** Saved items are split into sentences in the app; these cases are read
  whole, like typed words.

## 3. No Sentry (no remote telemetry) in the app

- **Context.** The Sentry Agent Tracing category needs events sent to Sentry. Actum has no
  `INTERNET` permission, and that is the product's main promise.
- **Options.** Add Sentry to the app; add it only to a separate opt-in diagnostic build;
  leave it out and measure locally.
- **Chosen.** Leave it out. A diagnostic build would need its own manifest, consent screen
  and redaction tests, which is too much for one day and adds risk to the main promise.
  Timing per case is logged locally (`adb logcat -s ActumEval`) instead.
- **Evidence.** `aapt2 dump permissions` on the debug APK built 2026-10-24 shows no
  `INTERNET` or network permission.

## 4. Honest repository history

- **Context.** The challenge needs a new repository. Actum predates the challenge.
- **Chosen.** The new repository's first commit imports Actum as it stood before the
  challenge and says so. Work done during the challenge follows as separate commits with
  their real dates. No back-dated or rewritten commits.

## 5. Known leftovers, not changed

- `net/Search.kt` and `TwoStageSearch.kt` still use the word "server" from an earlier
  design. There is no network code behind them; renaming is left for later to avoid
  touching working search code before the deadline.
