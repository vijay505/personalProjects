package com.opentext;

public class TranscriptionResponseDTO {
	private String transcript;

	public TranscriptionResponseDTO(String transcript) {
		this.transcript = transcript;
	}

	public String getTranscript() {
		return transcript;
	}

	public void setTranscript(String transcript) {
		this.transcript = transcript;
	}
}