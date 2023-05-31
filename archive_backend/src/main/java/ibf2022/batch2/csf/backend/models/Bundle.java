package ibf2022.batch2.csf.backend.models;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Bundle {
    private String bundleId;
    private LocalDate date;
    private String title;
    private String name;
    private String comments;
    private List<String> urls;

    public Bundle() {
    }

    public Bundle(String bundleId, LocalDate date, String title, String name, String comments, List<String> urls) {
        this.bundleId = bundleId;
        this.date = date;
        this.title = title;
        this.name = name;
        this.comments = comments;
        this.urls = urls;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "Bundle [bundleId=" + bundleId + ", date=" + date.toString() + ", title=" + title + ", name=" + name
                + ", comments="
                + comments + ", urls=" + urls + "]";
    }

    public JsonObject toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder()
                .add("bundleId", getBundleId())
                .add("date", getDate().toString())
                .add("title", getTitle());

        if (!(getName() == null && getUrls() == null)) {
            job.add("name", getName())
                .add("comments", getComments())
                .add("urls", Json.createArrayBuilder(getUrls()));
        }
        return job.build();
    }

    public static Bundle create(String json) {
        return create(Json.createReader(new StringReader(json)).readObject());
    }

    public static Bundle create(JsonObject o) {
        try {
            JsonArray jArr = o.getJsonArray("urls");
            List<String> urls = new LinkedList<>();
            for (int i = 0; i < jArr.size(); i++) {
                urls.add(jArr.getString(i));
            }

            return new Bundle(
                    o.getString("bundleId"),
                    LocalDate.parse(o.getString("date")),
                    o.getString("title"),
                    o.getString("name"),
                    o.getString("comments"),
                    urls);
        } catch (NullPointerException e) {
            return new Bundle(
                    o.getString("bundleId"),
                    LocalDate.parse(o.getString("date")),
                    o.getString("title"),
                    null,
                    null,
                    null);
        }
    }
}
