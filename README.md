# Actum

**Share it, find it, get it done — entirely on your phone.**

Actum is an offline Android assistant that turns what you save, type or say into real
actions: calendar events, alarms, expenses, to-dos, contacts and diary entries. It reads
screenshots, PDFs and notes, finds them again by meaning, and proposes the right action as
a card you confirm with a single tap. A language model runs on the phone itself, and the
app holds **no internet permission at all**.

---

## Highlights

- **Private by construction.** No `INTERNET` permission, so Android gives the app no way to
  reach a network. No accounts, servers, analytics or ads.
- **Fast where it matters.** Plain commands are understood instantly by deterministic rules.
  Everything else is read by Gemma 4 on the phone, in **4.1 s median** per request, with
  cards appearing one by one as they are written.
- **Accurate and verifiable.** **30 of 30** on our 30-sentence benchmark on the reference
  phone and **29 of 30** on a second phone. Every date, time, amount and phone number is
  checked in code against the person's own words before it is shown.
- **Built for how people here write.** Everyday short forms ("tmrw", "2 mins later") and
  common commands in Hinglish and Benglish are understood instantly.
- **Nothing happens without a tap.** The model only proposes; the person confirms.
- **328 automated tests**, all passing, plus measured benchmarks on two phones.

---

## How it works

```text
 What you share, type or say
 (screenshot · PDF · note · text · voice)
            │
            ▼
 ┌──────────────────────────┐      plain, direct command
 │  Rules                   │ ───────────────────────────────┐
 │  English · slang ·       │      read instantly (0 s)      │
 │  Hinglish · Benglish     │                                │
 └──────────────────────────┘                                │
            │ anything else                                  │
            ▼                                                │
 ┌──────────────────────────┐                                │
 │  Gemma 4 E2B, on-device  │   cards stream in as each      │
 │  (LiteRT-LM, CPU)        │   action is written            │
 └──────────────────────────┘                                │
            │                                                │
            ▼                                                ▼
 ┌────────────────────────────────────────────────────────────────┐
 │  Validator (plain Kotlin)                                      │
 │  dates, times, amounts, phone numbers resolved and checked     │
 │  against the person's words: ✓ verified · ? question · ! missing│
 └────────────────────────────────────────────────────────────────┘
            │
            ▼
   One card per action  →  the person taps  →  it happens  →  logged
```

Design principles:

1. **The model proposes; code decides.** Gemma never sees today's date and never computes
   one. Kotlin resolves every date, time and amount.
2. **Rules are right or silent.** A rule reads a sentence only when the whole sentence fits
   a known pattern. Anything with a negation, a second request, a question or a vague time
   goes to the model or to a question on the card.
3. **Answers must be about what was said.** A model answer not grounded in the person's
   words is dropped, not shown.
4. **Confirm before execute.** No action runs without the person's tap.

---

## Features

### Keep and find

Share anything into Actum, or copy text and tap its Quick Settings tile. Pictures and PDFs
are read by on-device OCR (ML Kit). Search works by meaning, using EmbeddingGemma on the
phone ("when does my plane leave" finds a saved flight note), and by exact words.

### Turn words into actions

| Action | What confirming does |
| --- | --- |
| Calendar event | Opens your calendar app with the event filled in |
| Alarm or reminder | Opens your clock app with the time set; a reminder for another day goes to the calendar |
| Expense | Adds an entry to Actum's ledger |
| To-do | Adds items to Actum's to-do list |
| Contact | Opens your contacts app with the name and number filled in |
| Diary | Stores the entry encrypted, behind a password you choose |
| Explain | Restates a notice or document. Nothing runs, and it is never advice |
| Anything else | "Actum can't do this yet." Nothing runs |

Each field on a card shows whether it was verified against your words, needs a choice
("morning or evening?") or is missing. Any field can be edited, and edits are checked again.

### Instant commands in English, slang, Hinglish and Benglish

| You type or say | Understood instantly as |
| --- | --- |
| set an alarm 2 mins later | Alarm, two minutes from now |
| wake me up at 6:30 am 2moro | Alarm, tomorrow at 6:30 AM |
| buy eggs, bread and butter | To-do: three items |
| kal subah 7 baje alarm laga do | Alarm, tomorrow at 7:00 AM |
| kal sokal 7 tay alarm diye dao | Alarm, tomorrow at 7:00 AM |
| 5 min baad yaad dila dena | Reminder, five minutes from now |
| doodh aur ande lene hai | To-do: doodh, ande |
| Rahul ke 500 dilam | Expense: ₹500 to Rahul |
| Priya ka number save karo 98300 12345 | New contact |

Deliberately **not** read by rules: "alarm dio na" (*don't*), "buy milk and call mom" (two
requests), "remind me in a bit" (vague). These go to the model, or the card asks.

### Saved items, ready before you ask

A saved screenshot, PDF or note is split into sentences; plain commands among them are
handled by the rules and the rest by Gemma. Recent items are read **in the background
shortly after they are saved**, so tapping **Find things to do in this** shows the cards
immediately. On the reference phone, a copied note that took 30 s to read now shows its
cards the moment it is opened. Anything the person asks for takes priority over this work.

### Voice

Tap the microphone and speak. Android's **on-device** speech recognizer turns speech into
words, which appear live and can be corrected. Speak a request, or tap **Save to diary**
to store your words exactly as said. Audio is never kept.

### A private diary

Diary entries are encrypted on the phone (RSA-2048 with OAEP and AES-GCM; the private key
protected by the diary password through PBKDF2 with 310,000 rounds, and again by the
Android Keystore). The password can be changed. It cannot be recovered, by design.

### Lists, calendar and log

To-dos, a month calendar marking confirmed events and reminders, the expense ledger, the
diary, and a log of every confirmed action. Light (beige) and dark looks.

### Learning from misses — safely

When a request needed the model but could have been read by a rule, or a rule's card was
corrected, Actum keeps that sentence **on the phone only** and lists it in the log, where
it can be cleared. These guide the next rules written by developers. **Actum never changes
its own behaviour**, and diary text is never kept.

---

## Privacy

- The installed app has **no `INTERNET` permission**; the **privacy** screen reads the
  app's permissions live and lists them.
- Models are copied onto the phone over USB, never downloaded by the app.
- Speech recognition uses only the on-device recognizer; Actum refuses to listen if the
  phone has none.
- App backups are disabled.
- Events and contacts you confirm are handed to your calendar and contacts apps, which may
  sync them to their own accounts. That is the one way data leaves Actum, and only on your
  tap.

---

## Results

### Speed and accuracy — reference phone

Nothing Phone A001T · MediaTek Dimensity 7300 · 8 GB RAM · Android 16 · Gemma 4 E2B on the
CPU through LiteRT-LM 0.18.0 · 14 October 2026 · on USB power, battery 39 °C.

| Benchmark | Median | Worst | Correct |
| --- | --- | --- | --- |
| One sentence with three actions, 5 runs | **8.5 s** | 10.4 s | **5 of 5** |
| 30-sentence test set, per sentence | **4.1 s** | 10.6 s | **30 of 30** |

- Rules alone read 9 of the 30 sentences correctly, instantly.
- Cards stream in: for the three-action sentence they appeared 0 s, 2.5 s and 7.8 s after
  the tap.
- Requests are read ahead while you type or as soon as speech becomes text; when the tap
  comes a few seconds later, the measured wait was under 0.1 s.
- The model is loaded and its instructions prepared when Actum opens (about 4 s), so that
  is not part of these figures.

### Progress over development

| Version | Three-action sentence | 30-sentence set | Correct |
| --- | --- | --- | --- |
| First working version | 31.3 s | not measured | 5 of 5 |
| Shorter prompt, one-line answers | 16.6 s | 11.2 s | 29 of 30 |
| Model loaded at launch, instructions read ahead | 12.0 s | 6.0 s | 29 of 30 |
| **Shorter answers, repair improvements (current)** | **8.5 s** | **4.1 s** | **30 of 30** |

**3.7× faster than the first version**, with accuracy improving rather than declining.

### Second device

| Phone | Three-action sentence | 30-sentence set | Battery |
| --- | --- | --- | --- |
| Motorola Edge 50 Pro · Snapdragon 7 Gen 3 · 12 GB · Android 15 | 10.8 s median, 5 of 5 | 5.3 s median, **29 of 30** | 27–31 °C |

Measured with the version before the latest speed work. Ten direct commands typed by hand
on this phone: **10 of 10** handled correctly; the seven plain ones instantly, and the
three that should go to the model, did.

### Smaller models

| Model (untrained) | Size | 30-sentence set | Median |
| --- | --- | --- | --- |
| **Gemma 4 E2B (used)** | 2.6 GB | **30 of 30** | 4.1 s |
| Gemma 3 1B | 584 MB | 14 of 30 | 7.0 s |
| Gemma 3 270M | 304 MB | 5 of 30 | 4.3 s |

Smaller models are not accurate enough out of the box. Fine-tuning Gemma 3 270M for
Actum's task is in progress.

### Automated tests

**328 unit tests in 48 suites, all passing.**

| Area | Tests | Covers |
| --- | --- | --- |
| Understanding requests | 52 | Rules (what they must read and what they must leave alone), slang, Hinglish and Benglish, sentence splitting for saved items, answer repair, streaming, grounding |
| Dates, times, amounts, validation | 24 | Relative days (kal, parshu, tmrw), clock times (subah 7 baje, sokal 7 tay), stretches from now, amounts, phone numbers, field verdicts |
| Diary encryption | 5 | Sealing, unlocking, password change, wrong password |
| Capture, OCR and import | 91 | Sharing, clipboard, PDFs, image handling, originals |
| Search and storage | 41 | Full-text queries, on-device embeddings, ranking |
| Interface logic | 115 | Search results, detail views, sheets, undo |

Every benchmark sentence is also run through the rules on each test run; a single misread
fails the suite. Every fault found on a real phone is added as a test before it is fixed.

Full measurement history: [`benchmarks/`](benchmarks/).

> The 30-sentence set was used during development to find and fix faults, so its score is
> not a held-out measure.

---

## Limitations

- English is primary. Common Hinglish and Benglish commands are covered by rules; the
  model's handling of those languages has no accuracy figure yet.
- OCR reads Latin script only; Hindi and Bengali script in images is not read.
- Voice requires Android 12+ and an English on-device speech pack.
- Only the first 1,200 characters of a long document are read for actions.
- Only the diary is encrypted; other data sits in the app's private storage.
- Tested on phones with 8 GB and 12 GB of RAM.

---

## Getting started

### Requirements

- Android 10 or later, 64-bit ARM; about **3.5 GB free** for the models and app
- A computer with JDK 21, the Android SDK (API 36.1) and `adb`

### Build and install

```sh
cd android
./gradlew installDebug
```

### Add the models

```sh
mkdir -p models && cd models
curl -L -O https://huggingface.co/litert-community/gemma-4-E2B-it-litert-lm/resolve/b3ca0d2f076785a8f4b2219ddbd2bdb99954eae1/gemma-4-E2B-it.litertlm
curl -L -O https://huggingface.co/litert-community/embeddinggemma-2-text-vision-440m-litert-lm/resolve/e301f74d5551b0c2641bd5cb4652a76239d5c5f8/embeddinggemma-2-text-vision-440m.litertlm
sha256sum *.litertlm
```

| File | Bytes | SHA-256 |
| --- | --- | --- |
| `gemma-4-E2B-it.litertlm` | 2,588,147,712 | `181938105e0eefd105961417e8da75903eacda102c4fce9ce90f50b97139a63c` |
| `embeddinggemma-2-text-vision-440m.litertlm` | 387,710,976 | `92dcbea108899e5d6e30d919b0744f90d9967e80c67a4ab5503ac16d54f62eb0` |

Open Actum once, then copy the models to the phone:

```sh
adb push models/gemma-4-E2B-it.litertlm /sdcard/Android/data/com.actum.app/files/
adb push models/embeddinggemma-2-text-vision-440m.litertlm /sdcard/Android/data/com.actum.app/files/
```

Actum verifies the model's SHA-256 before loading it. Without the embedding model, search
works by words only; without Gemma, direct commands still work.

### Run the tests and benchmarks

```sh
cd android && ./gradlew :app:testDebugUnitTest
```

Benchmarks run on a connected phone; see [`benchmarks/README.md`](benchmarks/README.md).

---

## Project structure

All app code is in `android/app/src/main/java/com/actum/`.

| Package | Responsibility |
| --- | --- |
| `model/` | On-device Gemma, prompt and streaming; instant rules (English, slang, Hinglish, Benglish); answer repair and grounding |
| `parse/`, `validate/` | Date, time, amount and phone parsing; field verification |
| `act/` | The request flow, confirmation, background preparation of saved items |
| `voice/` | On-device speech recognition |
| `data/` | Room database: lists, ledger, calendar marks, encrypted diary, log |
| `capture/`, `ocr/`, `embed/` | Sharing in, text recognition, on-device semantic search |
| `ui/` | Jetpack Compose screens and theme |

Built with Kotlin, Jetpack Compose, Room, WorkManager, LiteRT-LM and ML Kit.
[`AGENTS.md`](AGENTS.md) documents the codebase for contributors, and
[`CLAUDE.md`](CLAUDE.md) states the product rules every change must keep.

---

## Roadmap

- A fine-tuned Gemma 3 270M model as a faster, cooler first reader, with Gemma 4 as the
  fallback
- A Hinglish and Benglish test set with published accuracy
- OCR for Hindi and Bengali script
- More instant commands, guided by the misses Actum records

---

## Acknowledgements

Gemma 4 E2B and EmbeddingGemma by Google (Apache 2.0), run through LiteRT-LM. Text
recognition by ML Kit. Fonts: Young Serif, Instrument Sans and IBM Plex Mono (SIL Open Font
License; texts in `android/app/src/main/assets/licenses/`).
