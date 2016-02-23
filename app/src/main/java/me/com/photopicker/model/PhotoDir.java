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

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoDir)) return false;

        PhotoDir dir = (PhotoDir) o;

        return name.equals(dir.name) && id.equals(dir.id);
    }
}
