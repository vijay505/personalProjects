package com.opentext.deepgramAudioConv.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;

@Service
public class TextToSpeechService {

	public ByteArrayOutputStream convertTextToSpeech(String text) {
	    try {
	        // Load credentials from service-account.json in resources
	        InputStream credentialsStream = new ClassPathResource("service-account.json").getInputStream();
	        GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

	        // Build the client settings with custom credentials
	        TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
	                .setCredentialsProvider(() -> credentials)
	                .build();

	        try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create(settings)) {
	            SynthesisInput input = SynthesisInput.newBuilder()
	                    .setText(text)
	                    .build();

	            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
	                    .setLanguageCode("en-US")
	                    .setSsmlGender(SsmlVoiceGender.FEMALE)
	                    .build();

	            AudioConfig audioConfig = AudioConfig.newBuilder()
	                    .setAudioEncoding(AudioEncoding.LINEAR16)
	                    .build();

	            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

	            ByteString audioContents = response.getAudioContent();

	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            outputStream.write(audioContents.toByteArray());

	            return outputStream;
	        }
	    } catch (Exception e) {
	        System.err.println("Error during TTS conversion: " + e.getMessage());
	        e.printStackTrace();
	        return null;
	    }
	}

}
