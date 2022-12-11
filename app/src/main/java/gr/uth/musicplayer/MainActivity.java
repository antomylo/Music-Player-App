package gr.uth.musicplayer;


import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listViewSong);

        runtimePermission();
    }

    public void runtimePermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        displaySongs();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

    }

//    public ArrayList<File> findSong (File file){
//        ArrayList<File> arrayList = new ArrayList<>();
//        File[] files = file.listFiles();
//
//        for (File singlefile: files) {
//            if (singlefile.isDirectory() && !singlefile.isHidden()) {
//                arrayList.addAll(findSong(singlefile));
//            } else {
//                if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")) {
//                    arrayList.add(singlefile);
//                }
//            }
//        }
//        return arrayList;
//    }

    private List<File> findSongsFolders(File rootFolder) {
        List<File> songFolders = new ArrayList<>();
        for (File file : rootFolder.listFiles()) {
            if (file.isDirectory() && !file.isHidden()) {
                if (file != null) {
                    for (File insideFile : file.listFiles()) {
                        if (insideFile.getName().endsWith(".mp3") || insideFile.getName().endsWith("wav")) {
                            songFolders.add(file);
                        }
                    }
                }
            }
        }
        return songFolders;
    }

    private List<File> findSongs(List<File> songFolders) {
        if (songFolders == null || songFolders.size() == 0) return null;
        List<File> songs = new ArrayList<>();
        for (File folder : songFolders) {
            if (folder.isDirectory() && !folder.isHidden()) {
                for (File song : folder.listFiles()) {
                    if (song.getName().endsWith(".mp3") || song.getName().endsWith("wav")) {
                        songs.add(song);
                    }
                }
            }
        }
        return songs;
    }

    void displaySongs(){
//        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        final List<File> mySongs = findSongs(findSongsFolders(Environment.getExternalStorageDirectory()));

        if (mySongs != null && mySongs.size() > 0) {
            String[] items = new String[mySongs.size()];
            for(int i = 0; i < mySongs.size(); i++){
                items[i] = mySongs.get(i).toString()
                        .substring(mySongs.get(i).toString().lastIndexOf("/")+1)
                        .replace(".mp3", "").replace(".wav", "");
            }
            ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
            listView.setAdapter(myAdapter);
        } else {
            // todo: implement later
        }
    }
}