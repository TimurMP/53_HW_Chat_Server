package telran.chat.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {
    private static final long serialVersionUID = 1;
    String nickName;
    LocalDateTime time;
    String message;
    static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");



    public Message(String nickName, String message) {
        this.nickName = nickName;
        this.message = message;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setMessage(String message) {
        this.message = message;
        this.time = LocalDateTime.now();

    }

    public void setTime(boolean set) {
        this.time = LocalDateTime.now();

    }

    public String getNickName() {
        return nickName;
    }

    public String getTime() {
        return time.format(dateFormatter);
    }

    public String getMessage() {
        return message;
    }


    @Override
    public String toString() {
        return time.format(dateFormatter) + "  |  " + nickName + "\n" + message + "\n----------------------";


//        return "Message{" +
//                "nickName='" + nickName + '\'' +
//                ", time=" + time +
//                ", message='" + message + '\'' +
//                '}';
    }
}
