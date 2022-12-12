package com.amazon.ata.music.playlist.service.lambda;

import com.amazon.ata.music.playlist.service.activity.AddSongToPlaylistActivity;
import com.amazon.ata.music.playlist.service.dependency.DaggerServiceComponent;
import com.amazon.ata.music.playlist.service.dependency.ServiceComponent;
import com.amazon.ata.music.playlist.service.models.requests.AddSongToPlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.AddSongToPlaylistResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class AddSongToPlaylistActivityProvider implements RequestHandler<AddSongToPlaylistRequest, AddSongToPlaylistResult> {

    public AddSongToPlaylistActivityProvider() {
    }

    @Override
    public AddSongToPlaylistResult handleRequest(final AddSongToPlaylistRequest addSongToPlaylistRequest, Context context) {
        ServiceComponent dagger = getServiceComponent();
        AddSongToPlaylistActivity addSongToPlaylistActivity = dagger.provideAddSongToPlaylistActivity();
        dagger.inject(addSongToPlaylistActivity);
        return addSongToPlaylistActivity.handleRequest(addSongToPlaylistRequest, context);
    }

    private ServiceComponent getServiceComponent() {
        return DaggerServiceComponent.create();
    }
}
