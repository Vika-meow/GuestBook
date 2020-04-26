package Code.Domain;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String userName;
    private String email;
    private String homepage;

    @Column(length = 1024)
    private String text;

    private Date date;
    private String ip;
    private String browserVesrion;

    public Message() {
    }

    public Message(String userName, String email, String text, Date date, String ip, String browserVesrion) {
        this.userName = userName;
        this.email = email;
        this.text = text;
        this.date = date;
        this.ip = ip;
        this.browserVesrion = browserVesrion;
    }

    public Message(String userName, String email, String homepage, String text, Date date, String ip, String browserVesrion) {
        this.userName = userName;
        this.email = email;
        this.homepage = homepage;
        this.text = text;
        this.date = date;
        this.ip = ip;
        this.browserVesrion = browserVesrion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getBrowserVesrion() {
        return browserVesrion;
    }

    public void setBrowserVesrion(String browserVesrion) {
        this.browserVesrion = browserVesrion;
    }
}
