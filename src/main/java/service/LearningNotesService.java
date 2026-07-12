package service;

import model.LearningTopic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Loads Learning Notes from classpath JSON without relying on filesystem paths or a third-party parser. */
public final class LearningNotesService {
    private static final String DATA_PATH = "/data/learning-notes.json";
    private LearningNotesService() { }

    public static List<LearningTopic> loadTopics() throws IOException {
        try (InputStream stream = LearningNotesService.class.getResourceAsStream(DATA_PATH)) {
            if (stream == null) throw new IOException("Missing resource: " + DATA_PATH);
            return new JsonTopicsParser(new String(stream.readAllBytes(), StandardCharsets.UTF_8)).parse();
        }
    }

    /** Small JSON reader for the fixed LearningTopic schema; it supports strings and string arrays. */
    private static final class JsonTopicsParser {
        private final String json; private int cursor;
        JsonTopicsParser(String json) { this.json = json; }
        List<LearningTopic> parse() throws IOException {
            List<LearningTopic> result = new ArrayList<>(); skipWhitespace(); expect('[');
            skipWhitespace(); while (peek() != ']') { result.add(topic()); skipWhitespace(); if (peek() == ',') { cursor++; skipWhitespace(); } }
            expect(']'); return List.copyOf(result);
        }
        private LearningTopic topic() throws IOException {
            expect('{'); String id="",title="",description="",image="",secondary="",fun="",tip=""; List<String> points=List.of();
            while (true) { skipWhitespace(); if (peek() == '}') { cursor++; break; } String key=string(); skipWhitespace(); expect(':'); skipWhitespace();
                if (key.equals("keyPoints")) points=array(); else { String value=string(); switch(key){case"id"->id=value;case"title"->title=value;case"description"->description=value;case"imagePath"->image=value;case"secondaryImagePath"->secondary=value;case"funFact"->fun=value;case"examTip"->tip=value;default->{}} }
                skipWhitespace(); if (peek() == ',') { cursor++; continue; } if (peek() == '}') { cursor++; break; } throw error("Expected comma or object end"); }
            return new LearningTopic(id,title,description,image,secondary,points,fun,tip);
        }
        private List<String> array() throws IOException { expect('['); List<String> values=new ArrayList<>();skipWhitespace();while(peek()!=']'){values.add(string());skipWhitespace();if(peek()==','){cursor++;skipWhitespace();}else if(peek()!=']')throw error("Expected array separator");}expect(']');return values; }
        private String string() throws IOException { expect('"'); StringBuilder b=new StringBuilder();while(cursor<json.length()){char c=json.charAt(cursor++);if(c=='"')return b.toString();if(c=='\\'&&cursor<json.length()){char e=json.charAt(cursor++);b.append(switch(e){case'n'->'\n';case'r'->'\r';case't'->'\t';case'"'->'"';case'\\'->'\\';default->e;});}else b.append(c);}throw error("Unterminated string"); }
        private void skipWhitespace(){while(cursor<json.length()&&Character.isWhitespace(json.charAt(cursor)))cursor++;}private char peek() throws IOException{skipWhitespace();if(cursor>=json.length())throw error("Unexpected end");return json.charAt(cursor);}private void expect(char expected)throws IOException{if(peek()!=expected)throw error("Expected '"+expected+"'");cursor++;}private IOException error(String message){return new IOException(message+" at character "+cursor);}
    }
}
