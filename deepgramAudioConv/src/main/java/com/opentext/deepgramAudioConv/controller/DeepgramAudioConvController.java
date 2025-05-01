package com.opentext.deepgramAudioConv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.opentext.TranscriptionResponseDTO;
import com.opentext.deepgramAudioConv.service.DeepgramService;

@RestController
@RequestMapping("/api/transcribe")
public class DeepgramAudioConvController {

	@Autowired
	private DeepgramService deepgramService;

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

}