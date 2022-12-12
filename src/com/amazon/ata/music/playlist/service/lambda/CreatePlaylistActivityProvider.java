package com.amazon.ata.music.playlist.service.lambda;

import com.amazon.ata.music.playlist.service.activity.CreatePlaylistActivity;
import com.amazon.ata.music.playlist.service.dependency.DaggerServiceComponent;
import com.amazon.ata.music.playlist.service.dependency.ServiceComponent;
import com.amazon.ata.music.playlist.service.models.requests.CreatePlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.CreatePlaylistResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreatePlaylistActivityProvider implements RequestHandler<CreatePlaylistRequest, CreatePlaylistResult> {

    public CreatePlaylistActivityProvider() {
    }

    @Override
    public CreatePlaylistResult handleRequest(final CreatePlaylistRequest createPlaylistRequest, Context context) {
        ServiceComponent dagger = getServiceComponent();
        CreatePlaylistActivity createPlaylistActivity = dagger.provideCreatePlaylistActivity();
        dagger.inject(createPlaylistActivity);
        return createPlaylistActivity.handleRequest(createPlaylistRequest, context);
    }

    private ServiceComponent getServiceComponent() {
        return DaggerServiceComponent.create();
    }
}
