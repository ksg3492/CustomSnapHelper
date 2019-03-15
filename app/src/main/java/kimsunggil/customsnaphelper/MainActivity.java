package kimsunggil.customsnaphelper;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main);

        ArrayList<MusicItem> items = loadLocalData(this);
        ArrayList<String> thumbItems = new ArrayList<>();

        for (MusicItem m : items) {
            thumbItems.add(m.getAlbumArtUri().toString());
        }

        CustomImageScrollView customImageScrollView = findViewById(R.id.customImageScrollView);
        customImageScrollView.updateList(thumbItems);
    }

    public ArrayList<MusicItem> loadLocalData(Context context) {
        ArrayList<MusicItem> musicItems;

        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        // 찾고자하는 파일 확장자명.
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");

        String[] selectionArgsMp3 = new String[]{ mimeType };

        Cursor c = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.DURATION

                }, selectionMimeType, selectionArgsMp3, null);

        if (c.getCount() == 0) {
            return new ArrayList<>();
        }

        musicItems = new ArrayList<>();
        while (c.moveToNext()) {
            MusicItem item = new MusicItem();

            // 경로 데이터 셋팅.
            item.setDataPath(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
            item.setTitle(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
            item.setArtist(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
            item.setAlbum(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
            item.setAlbumId(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
            item.setDuration(c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));

            musicItems.add(item);
        }

        return musicItems;
    }
}
