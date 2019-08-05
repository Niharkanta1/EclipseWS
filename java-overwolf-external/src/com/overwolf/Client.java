package com.overwolf;

import javax.script.Invocable;
import javax.script.ScriptEngine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import com.overwolf.util.StringFormat;

public class Client {
	public boolean isInitialized = false;
	public boolean isRunning = false;
	public ModuleManager moduleManager;
	public EventHandler eventHandler;
	public static final Client theClient = new Client();
	public StringFormat stringFormat;
	public CommandManager commandManager;
	public ConfigManager configManager;
	public KeyboardHandler keyboardHandler;

	public Gson gson;
	public Gson nicegson;
	public JsonParser jsonParser;
	public ScriptEngine jsengine;
	public Invocable jsinvocable;
	public boolean jsinitialized;

	public Client startClient() {
		this.moduleManager = new ModuleManager();
		this.eventHandler = new EventHandler();
		this.stringFormat = new StringFormat();
		this.commandManager = new CommandManager();
		//this.configManager = new ConfigManager();
		this.keyboardHandler = new KeyboardHandler();

		this.nicegson = new GsonBuilder().setPrettyPrinting().create();
		this.gson = new Gson();

		this.isInitialized = true;
		this.isRunning = true;
		return this;
	}

	public void restartClient() {
		this.nicegson = null;
		this.gson = null;
		this.stringFormat = null;
		this.configManager = null;
		this.moduleManager = null;
		this.eventHandler = null;
		this.commandManager = null;
		this.jsengine = null;
		this.jsinvocable = null;
		this.keyboardHandler = null;
		startClient();
	}
	
	public void shutdownClient() {
		Client.theClient.eventHandler.onClientShutdown();
		this.isRunning = false;
	}

}
