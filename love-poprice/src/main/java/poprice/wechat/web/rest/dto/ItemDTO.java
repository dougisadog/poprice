package poprice.wechat.web.rest.dto;

import javax.persistence.Transient;

/**
 * Created by monsoon on 2017/4/12.
 */
public class ItemDTO {
    private Long id;

    private String title;

    private int num;

    public ItemDTO(Long id, String title, int num) {
        this.id = id;
        this.title = title;
        this.num = num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
