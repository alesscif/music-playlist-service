package com.amazon.ata.music.playlist.service.converters;

import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.models.SongModel;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelConverterTest {
    private ModelConverter modelConverter = new ModelConverter();
    private Playlist playlist = new Playlist();

    void taggedPlaylistPrep() {
        playlist = new Playlist();
        playlist.setId("id");
        playlist.setName("name");
        playlist.setCustomerId("customerId");
        playlist.setSongCount(42);
        Set<String> tags = new HashSet<>();
        tags.add("string1");
        tags.add("string2");
        playlist.setTags(tags);
        List<AlbumTrack> songs = new ArrayList<>();
        songs.add(new AlbumTrack());
        playlist.setSongList(songs);
    }

    void untaggedPlaylistPrep() {
        playlist = new Playlist();
        playlist.setId("id");
        playlist.setName("name");
        playlist.setCustomerId("customerId");
        playlist.setSongCount(42);
        Set<String> tags = new HashSet<>();
        playlist.setTags(tags);
        List<AlbumTrack> songs = new ArrayList<>();
        songs.add(new AlbumTrack());
        playlist.setSongList(songs);
    }

    @Test
    void toPlaylistModel_givenPlaylistWithTags_returnsPlaylistModelWithMatchingFieldsAndTags() {
        // GIVEN
        taggedPlaylistPrep();

        // WHEN
        PlaylistModel actual = modelConverter.toPlaylistModel(playlist);

        // THEN
        assertEquals("string1", actual.getTags().get(0));
        assertEquals("string2", actual.getTags().get(1));
        assertEquals("id", actual.getId());
        assertEquals("name", actual.getName());
        assertEquals("customerId", actual.getCustomerId());
        assertEquals(42, actual.getSongCount());

    }

    @Test
    void toPlaylistModel_givenPlaylistWithoutTags_returnsPlaylistModelWithMatchingFieldsAndNullTags() {
        // GIVEN
        untaggedPlaylistPrep();

        // WHEN
        PlaylistModel actual = modelConverter.toPlaylistModel(playlist);

        // THEN
        assertNull(actual.getTags());
        assertEquals("id", actual.getId());
        assertEquals("name", actual.getName());
        assertEquals("customerId", actual.getCustomerId());
        assertEquals(42, actual.getSongCount());

    }

    @Test
    void toSongModel_givenAlbumTrack_returnsSongModelWithMatchingFields() {
        // GIVEN
        AlbumTrack albumTrack = new AlbumTrack();
        albumTrack.setAsin("0");
        albumTrack.setTrackNumber(0);
        albumTrack.setAlbumName("album");
        albumTrack.setSongTitle("title");

        // WHEN
        SongModel actual = modelConverter.toSongModel(albumTrack);

        // THEN
        assertEquals("0", actual.getAsin());
        assertEquals(0, actual.getTrackNumber());
        assertEquals("album", actual.getAlbum());
        assertEquals("title", actual.getTitle());
    }
}
