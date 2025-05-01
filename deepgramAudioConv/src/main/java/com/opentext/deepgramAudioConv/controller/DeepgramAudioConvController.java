package com.opentext.deepgramAudioConv.controller;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.opentext.deepgramAudioConv.responseDTO.TranscriptionResponseDTO;
import com.opentext.deepgramAudioConv.service.DeepgramService;
import com.opentext.deepgramAudioConv.service.TextToSpeechService;

@RestController
@RequestMapping("/api/transcribe")
public class DeepgramAudioConvController {

	@Autowired
	private DeepgramService deepgramService;

	@Autowired
	private TextToSpeechService textToSpeechService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<TranscriptionResponseDTO> transcribeAudio(@RequestParam("file") MultipartFile file) {
		try {

			TranscriptionResponseDTO transcript = deepgramService.transcribe(file);
			return ResponseEntity.ok(transcript);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new TranscriptionResponseDTO("Error transcribing the audio"));
		}
	}

	@PostMapping(value = "/convert", produces = "audio/wav")
	public ResponseEntity<byte[]> convertTextToSpeech(@RequestBody String text) {
		ByteArrayOutputStream audioStream = textToSpeechService.convertTextToSpeech(text);

		if (audioStream == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("audio/wav"));
		headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.wav\"");

		return new ResponseEntity<>(audioStream.toByteArray(), headers, HttpStatus.OK);
	}

}