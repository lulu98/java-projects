#!/usr/bin/env python3
from tika import parser
from gtts import gTTS
import sys

# For other language codes refer to the following page: https://cloud.google.com/text-to-speech/docs/voices
# For playing the mp3 on Linux in terminal: mpg123 pdg.mp3
language = 'en'
if len(sys.argv) == 3:
	language = sys.argv[2]
elif len(sys.argv) != 2:
	print("Wrong format!")
	print("Format: ./pdf_to_speech.py <path_to_pdf_file> [<language>]")
	exit(1)

pdf_file = sys.argv[1]
raw = parser.from_file(pdf_file)
tts = gTTS(text=raw['content'],lang=language)
tts.save("pdf.mp3")
