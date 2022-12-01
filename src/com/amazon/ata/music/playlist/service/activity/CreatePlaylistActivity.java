package com.amazon.ata.music.playlist.service.activity;

import com.amazon.ata.music.playlist.service.converters.ModelConverter;
import com.amazon.ata.music.playlist.service.dynamodb.models.AlbumTrack;
import com.amazon.ata.music.playlist.service.dynamodb.models.Playlist;
import com.amazon.ata.music.playlist.service.exceptions.InvalidAttributeValueException;
import com.amazon.ata.music.playlist.service.models.requests.CreatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.CreatePlaylistResult;
import com.amazon.ata.music.playlist.service.models.PlaylistModel;
import com.amazon.ata.music.playlist.service.dynamodb.PlaylistDao;

import com.amazon.ata.music.playlist.service.util.MusicPlaylistServiceUtils;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the CreatePlaylistActivity for the MusicPlaylistService's CreatePlaylist API.
 *
 * This API allows the customer to create a new playlist with no songs.
 */
public class CreatePlaylistActivity implements RequestHandler<CreatePlaylistRequest, CreatePlaylistResult> {
    private final Logger log = LogManager.getLogger();
    private final PlaylistDao playlistDao;

    /**
     * Instantiates a new CreatePlaylistActivity object.
     *
     * @param playlistDao PlaylistDao to access the playlists table.
     */
    public CreatePlaylistActivity(PlaylistDao playlistDao) {
        this.playlistDao = playlistDao;
    }

    /**
     * This method handles the incoming request by persisting a new playlist
     * with the provided playlist name and customer ID from the request.
     * <p>
     * It then returns the newly created playlist.
     * <p>
     * If the provided playlist name or customer ID has invalid characters, throws an
     * InvalidAttributeValueException
     *
     * @param createPlaylistRequest request object containing the playlist name and customer ID
     *                              associated with it
     * @return createPlaylistResult result object containing the API defined {@link PlaylistModel}
     */
    @Override
    public CreatePlaylistResult handleRequest(final CreatePlaylistRequest createPlaylistRequest, Context context) throws InvalidAttributeValueException {
        log.info("Received CreatePlaylistRequest {}", createPlaylistRequest);

        String name = createPlaylistRequest.getName();
        String customerId = createPlaylistRequest.getCustomerId();

        if (!MusicPlaylistServiceUtils.isValidString(name) || !MusicPlaylistServiceUtils.isValidString(customerId)) {
            throw new InvalidAttributeValueException("Request includes invalid characters.");
        }

        String id = MusicPlaylistServiceUtils.generatePlaylistId();

        List<String> originalTags = createPlaylistRequest.getTags();
        Set<String> tags = new HashSet<>();
        if (originalTags != null) {
            tags = new HashSet<>(originalTags);
        }

        Playlist playlist = new Playlist();

        playlist.setId(id);
        playlist.setName(name);
        playlist.setCustomerId(customerId);
        playlist.setSongCount(0);
        List<AlbumTrack> songs = new ArrayList<>();
        playlist.setSongList(songs);
        playlist.setTags(tags);

        playlistDao.savePlaylist(playlist);

        ModelConverter modelConverter = new ModelConverter();
        PlaylistModel playlistModel = modelConverter.toPlaylistModel(playlist);

        return CreatePlaylistResult.builder()
                .withPlaylist(playlistModel)
                .build();
    }
}
