package com.example.ticketreservation.model;

import jakarta.persistence.*;

@Entity
public class Singer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String singerName;
    private String genre;

    public Singer() {
    }

    public Singer(String singerName, String genre) {
        this.singerName = singerName;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
