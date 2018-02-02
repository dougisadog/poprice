package poprice.wechat.web.mvc.forward;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ForwardBaseController {


    @RequestMapping(value = "mobileIndex")
    public String mobileIndex() {
        return  "mobileIndex";
    }
    
    @RequestMapping(value = "introduction")
    public String introduction() {
        return  "introduction";
    }


}

