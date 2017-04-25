package sergeylysyi.threescreens;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

    private String header;
    private String body;
    private int color;

    public Note(String header, String body, int color) {
        this.header = header;
        this.body = body;
        this.color = color;
    }

    protected Note(Parcel in) {
        header = in.readString();
        body = in.readString();
        color = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public void setHeader(String newHeader) {
        this.header = newHeader;
    }

    public void setBody(String newBody) {
        this.body = newBody;
    }

    public void setColor(int newColor) {
        this.color = newColor;
    }


    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public int getColor() {
        return color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(header);
        dest.writeString(body);
        dest.writeInt(color);
    }
}
