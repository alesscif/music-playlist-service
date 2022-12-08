package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazon.ata.music.playlist.service.models.requests.CreatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.CreatePlaylistResult;
import com.amazonaws.services.lambda.runtime.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class CreatePlaylistActivityTest {
    @Mock
    PlaylistDao playlistDao;

    @Mock
    Context context;

    @InjectMocks
    CreatePlaylistActivity createPlaylistActivity;

    String validName = "validName";
    String validCustomerId = "validCustomerId";
    List<String> tags = List.of("tag1", "tag2", "tag3");

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void handleRequest_withInvalidNameOrCustomerId_throwsInvalidAttributeValueException() {
        // GIVEN
        String invalidName = "\\?";
        String invalidCustomerId = "\\?";

        CreatePlaylistRequest createPlaylistRequest = CreatePlaylistRequest.builder()
                .withName(invalidName)
                .withCustomerId(validCustomerId)
                .build();

        CreatePlaylistRequest createPlaylistRequest1 = CreatePlaylistRequest.builder()
                .withName(validName)
                .withCustomerId(invalidCustomerId)
                .build();


        // WHEN + THEN
        assertThrows(InvalidAttributeValueException.class,
                () -> createPlaylistActivity.handleRequest(createPlaylistRequest, context),
                "Expected InvalidAttributeValueException to be thrown when invalid name is provided"
        );
        assertThrows(InvalidAttributeValueException.class,
                () -> createPlaylistActivity.handleRequest(createPlaylistRequest1, context),
                "Expected InvalidAttributeValueException to be thrown when invalid customerId is provided"
        );
    }

    @Test
   void handleRequest_withValidNameAndCustomerId_returnsValidCreatePlaylistResultObjectWithExpectedValues() {
        // GIVEN
        CreatePlaylistRequest createPlaylistRequest = CreatePlaylistRequest.builder()
                .withName(validName)
                .withCustomerId(validCustomerId)
                .withTags(tags)
                .build();

        when(playlistDao.savePlaylist(any(Playlist.class))).thenReturn(null);

        // WHEN
        CreatePlaylistResult actual = createPlaylistActivity.handleRequest(createPlaylistRequest, context);

        // THEN
        assertEquals(validName, actual.getPlaylist().getName(), "Expected result name and request name to match");
        assertEquals(validCustomerId, actual.getPlaylist().getCustomerId(), "Expected result customerId and request CustomerId to match");
        assertEquals(tags, actual.getPlaylist().getTags(), "Expected result tags and request tags to match");
    }

    @Test
    void handleRequest_withNoTags_returnsValidCreatePlaylistResultObjectWithNullTagsAndExpectedValues() {
        // GIVEN
        CreatePlaylistRequest createPlaylistRequest = CreatePlaylistRequest.builder()
                .withName(validName)
                .withCustomerId(validCustomerId)
                .build();

        when(playlistDao.savePlaylist(any(Playlist.class))).thenReturn(null);

        // WHEN
        CreatePlaylistResult actual = createPlaylistActivity.handleRequest(createPlaylistRequest, context);

        // THEN
        assertEquals(validName, actual.getPlaylist().getName(), "Expected result name and request name to match");
        assertEquals(validCustomerId, actual.getPlaylist().getCustomerId(), "Expected result customerId and request CustomerId to match");
        assertNull(actual.getPlaylist().getTags(), "Expected result tags to be null");
    }
}

