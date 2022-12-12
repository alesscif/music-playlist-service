package com.amazon.ata.music.playlist.service.lambda;

import com.amazon.ata.music.playlist.service.activity.CreatePlaylistActivity;
import com.amazon.ata.music.playlist.service.activity.GetPlaylistActivity;
import com.amazon.ata.music.playlist.service.dependency.DaggerServiceComponent;
import com.amazon.ata.music.playlist.service.dependency.ServiceComponent;
import com.amazon.ata.music.playlist.service.models.requests.GetPlaylistRequest;
import com.amazon.ata.music.playlist.service.models.results.GetPlaylistResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetPlaylistActivityProvider implements RequestHandler<GetPlaylistRequest, GetPlaylistResult> {

    public GetPlaylistActivityProvider() {
    }

    @Override
    public GetPlaylistResult handleRequest(final GetPlaylistRequest getPlaylistRequest, Context context) {
        ServiceComponent dagger = getServiceComponent();
        GetPlaylistActivity getPlaylistActivity = dagger.provideGetPlaylistActivity();
        dagger.inject(getPlaylistActivity);
        return getPlaylistActivity.handleRequest(getPlaylistRequest, context);
    }

    private ServiceComponent getServiceComponent() {
        return DaggerServiceComponent.create();
    }
}
