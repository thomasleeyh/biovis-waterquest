package service;

import javafx.scene.media.AudioClip;import java.net.URL;import java.util.HashMap;import java.util.Map;
public final class AudioManager {private static final Map<String,AudioClip>CACHE=new HashMap<>();private AudioManager(){} public static void play(String name){URL url=AudioManager.class.getResource("/audio/"+name+".mp3");if(url!=null)CACHE.computeIfAbsent(name,n->new AudioClip(url.toExternalForm())).play();}}
