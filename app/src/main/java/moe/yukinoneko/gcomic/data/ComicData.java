package moe.yukinoneko.gcomic.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

import java.util.ArrayList;
import java.util.List;

import moe.yukinoneko.gcomic.database.model.ReadHistoryModel;

/**
 * Created by SamuelGjk on 2016/4/6.
 */
public class ComicData {
    public int id;
    public String islong;
    public int direction;
    public String title;
    @Json(name = "is_dmzj") public int isDmzj;
    public String cover;
    public String description;
    @Json(name = "last_updatetime") public int lastUpdatetime;
    public int copyright;
    @Json(name = "first_letter") public String firstLetter;
    @Json(name = "hot_num") public int hotNum;
    public Object uid;
    @Json(name = "subscribe_num") public int subscribeNum;
    public CommentBean comment;
    public List<TypesBean> types;
    public List<AuthorsBean> authors;
    public List<StatusBean> status;
    public List<ChaptersBean> chapters;

    public List<Integer> downloadedChapters = new ArrayList<>();

    public ReadHistoryModel readHistory;

    public static class CommentBean {
        @Json(name = "comment_count") public int commentCount;
        @Json(name = "latest_comment") public List<LatestCommentBean> latestComment;

        public static class LatestCommentBean {
            @Json(name = "comment_id") public int commentId;
            public int uid;
            public String content;
            public int createtime;
            public String nickname;
            @Json(name = "avatar_url") public String avatarUrl;
        }
    }

    public static class TypesBean {
        @Json(name = "tag_id") public int tagId;
        @Json(name = "tag_name") public String tagName;
    }

    public static class AuthorsBean {
        @Json(name = "tag_id") public int tagId;
        @Json(name = "tag_name") public String tagName;
    }

    public static class StatusBean {
        @Json(name = "tag_id") public int tagId;
        @Json(name = "tag_name") public String tagName;
    }

    public static class ChaptersBean {
        public String title;
        public List<ChapterBean> data;

        public static class ChapterBean implements Parcelable {
            @Json(name = "chapter_id") public int chapterId;
            @Json(name = "chapter_title") public String chapterTitle;
            public int updatetime;
            public long filesize;
            @Json(name = "chapter_order") public int chapterOrder;

            public boolean isDownloaded;

            public ChapterBean() {

            }

            protected ChapterBean(Parcel in) {
                chapterId = in.readInt();
                chapterTitle = in.readString();
                updatetime = in.readInt();
                filesize = in.readLong();
                chapterOrder = in.readInt();
            }

            public static final Creator<ChapterBean> CREATOR = new Creator<ChapterBean>() {
                @Override
                public ChapterBean createFromParcel(Parcel in) {
                    return new ChapterBean(in);
                }

                @Override
                public ChapterBean[] newArray(int size) {
                    return new ChapterBean[size];
                }
            };

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(chapterId);
                dest.writeString(chapterTitle);
                dest.writeInt(updatetime);
                dest.writeLong(filesize);
                dest.writeInt(chapterOrder);
            }
        }
    }
}
