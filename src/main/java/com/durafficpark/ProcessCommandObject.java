package com.durafficpark;

public class ProcessCommandObject  {
    private String jsonInput;

    public ProcessCommandObject(String jsonInput) {
        this.jsonInput = jsonInput;
    }

    public String getJSONInput() {
        return this.jsonInput;
    }
}