package com.opentext.groqPoc.requestDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatRequest {

	  private String prompt;

	    // Default constructor (required for JSON deserialization)
	    public ChatRequest() {}

	    public ChatRequest(String prompt) {
	        this.prompt = prompt;
	    }

	    public String getPrompt() {
	        return prompt;
	    }

	    public void setPrompt(String prompt) {
	        this.prompt = prompt;
	    }
}
