package edu.illinois.cs.cs125.mp7;

/**
 * Base class for items to display in a RecyclerView.
 */
public class ResultsList {
    /** Name of the item. */
    private String name;
    /** URL to thumbnail of the item. */
    private String imgURL;

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgURL;
    }

    public ResultsList(String name, String imgUrl) {
        this.name = name;
        this.imgURL = imgUrl;
    }
}
