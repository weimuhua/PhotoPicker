package me.com.photopicker.model;

import java.util.ArrayList;
import java.util.List;

public class PhotoDir {
    public String id;
    public String coverPath;
    public String name;
    public long date;
    public List<Photo> photoList = new ArrayList<>();

    public void addPhoto(int id, String path) {
        photoList.add(new Photo(id, path));
    }

    public List<String> getPhotoPaths() {
        List<String> paths = new ArrayList<>(photoList.size());
        for (Photo photo : photoList) {
            paths.add(photo.path);
        }
        return paths;
    }
}
