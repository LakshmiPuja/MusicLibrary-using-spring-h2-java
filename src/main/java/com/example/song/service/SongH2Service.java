package com.example.song.service;

 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.HttpStatus;
  import org.springframework.jdbc.core.JdbcTemplate;
  import org.springframework.stereotype.Service;
  import org.springframework.web.server.ResponseStatusException;
  import java.util.ArrayList;

import com.example.song.repository.*;
import com.example.song.model.*;


// Write your code here
@Service
public class SongH2Service implements SongRepository{

@Autowired
private JdbcTemplate db;

@Override
public ArrayList<Song> getSongs(){
    ArrayList<Song> songsList = new ArrayList<>(db.query("SELECT * FROM playlist", new SongRowMapper()));
    return songsList;
}

@Override
public Song addSong(Song song){
db.update("INSERT INTO playlist(songName, lyricist, singer,musicDirector) VALUES(?,?,?,?)",
song.getSongName(),song.getLyricist(),song.getSinger(),song.getMusicDirector());

Song newSong = db.queryForObject("SELECT * FROM playlist WHERE songName = ? AND lyricist =? AND singer = ? AND musicDirector = ?",
new SongRowMapper(), song.getSongName(),song.getLyricist(),song.getSinger(),song.getMusicDirector());
return newSong;
}

@Override
public Song getSong(int songId){
    try{
        Song song = db.queryForObject("SELECT * FROM playlist WHERE songId =?", new SongRowMapper(), songId);
        return song;
    }catch(Exception e){
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}

@Override
public Song updateSong(int songId, Song song){
    try{
        Song existingSong = db.queryForObject("SELECT * FROM playlist WHERE songId = ?", new SongRowMapper(), songId);
    }catch(Exception e){
         throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    if (song.getSongName() != null) db.update("UPDATE playlist SET songName = ? WHERE songId=?",song.getSongName(),songId);
    if (song.getLyricist() != null) db.update("UPDATE playlist SET lyricist=? WHERE songId=?",song.getLyricist(),songId);
    if (song.getSinger() != null) db.update("UPDATE playlist SET singer=? WHERE songId=?",song.getSinger(),songId);
    if (song.getMusicDirector() != null) db.update("UPDATE playlist SET musicDirector = ? WHERE songId=?",song.getMusicDirector(),songId);
    return getSong(songId);
}

@Override 
public void deleteSong(int songId){
    db.update("DELETE FROM playlist WHERE songId = ?", songId);
    
}






}