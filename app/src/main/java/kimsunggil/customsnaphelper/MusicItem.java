package kimsunggil.customsnaphelper;

import android.content.ContentUris;
import android.net.Uri;
import android.util.Log;

public class MusicItem {
    final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

    String index;
    String dataPath;
    String title;
    String album;
    long albumId;
    Uri albumArtUri;
    String artist;
    int duration;


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        try {
            long albumIdLong = Long.parseLong(albumId);
            this.albumId = albumIdLong;

            setAlbumArtUri(albumIdLong);
        } catch (Exception e) {
            this.albumId = 0;
        }
    }

    public Uri getAlbumArtUri() {
        return albumArtUri;
    }

    public void setAlbumArtUri(long albumId) {
        this.albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        try {
            long durationLong = Long.parseLong(duration);
            durationLong = durationLong / 1000L;

            this.duration = (int)durationLong;
        } catch (Exception e) {
            Log.e("SG2","setDuration Error : ", e);
            this.duration = 0;
        }
    }
}
