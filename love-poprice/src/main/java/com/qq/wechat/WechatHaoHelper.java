package com.qq.wechat;

import java.util.List;

import com.qq.wechat.aes.AesException;
import com.qq.wechat.aes.WXBizMsgCrypt;

import me.hao0.wechat.core.Callback;
import me.hao0.wechat.core.MenuBuilder;
import me.hao0.wechat.core.Wechat;
import me.hao0.wechat.core.WechatBuilder;
import me.hao0.wechat.model.js.TicketType;
import me.hao0.wechat.model.menu.Menu;
import me.hao0.wechat.model.message.receive.RecvMessage;
import me.hao0.wechat.model.message.receive.event.RecvMenuEvent;
import me.hao0.wechat.model.message.resp.Article;
import me.hao0.wechat.model.message.send.TemplateField;
import me.hao0.wechat.model.user.User;
import me.hao0.wechat.model.user.UserList;

public class WechatHaoHelper {

	private static WechatHaoHelper wechatHaoHelper;
	
	/**
	 * wechahao的组件
	 */
    private Wechat wechat;
    
    /**
     * 加密用组件
     */
    private WXBizMsgCrypt pc;
    
    //测试号
//    public final static String APP_ID = "wx39bb940a2cc30c6b";
//    
//    public final static String APP_SECRET = "03ff7c0fedfa63c6d54294e08b6333b6";
    
    //正式号
//    public final static String APP_ID = "wxa6b714bed90c83a9";
//    
//    public final static String APP_SECRET = "c6d065f28b4715f3ce71e9103845f870";
//    
//    private final static String APP_MSG_KEY = "eUtDeu8eXVp4BEgb8qciUL3hiXNqGoSHFzDd9Ps4WxF";
//    
//    private final static String APP_TOKEN = "poprice";
    
    public final static String APP_ID = WechatSetting.getAppId();
    
    public final static String APP_SECRET = WechatSetting.getAppSecret();
    
    private final static String APP_MSG_KEY = WechatSetting.getAES();
    
    private final static String APP_TOKEN = WechatSetting.getToken();
    
    /**
     * 当前是否开启加密
     */
    private final static boolean NEED_AES = WechatSetting.needAES();
    
    private final static boolean DEBUG = WechatSetting.isDebug();
    
    public final static String WX_LOGIN_KEY = "WX_LOGIN_KEY";
    
    /**
     * appid appsecret可修改为 文件props读取
     */
	private WechatHaoHelper() {
		wechat = WechatBuilder.newBuilder(APP_ID, APP_SECRET).msgKey(APP_MSG_KEY).build();
		if (NEED_AES) {
			try {
				pc = new WXBizMsgCrypt(APP_TOKEN, APP_MSG_KEY, APP_ID);
			} catch (AesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	};
	
	public String getTicket() {
		return wechat.js().getTicket(TicketType.JSAPI).getTicket();
	}
	
	public Wechat getWechat(){
		return wechat;
	}
	
	/**
	 * 构建加密信息
	 * @param orginXml 原始未加密xml
	 * @param timeStamp 时间戳，可以自己生成，也可以用URL参数的timestamp
	 * @param nonce 随机串，可以自己生成，也可以用URL参数的nonce
	 * @return 已加密结果
	 * @throws Exception
	 */
	public String encodeXml(String orginXml, String timestamp, String nonce) throws Exception {
		if (!NEED_AES) return orginXml;
		String mingwen = pc.encryptMsg(orginXml, timestamp, nonce);
		if(DEBUG) {
			System.out.println("加密后: " + mingwen);
		}
		return mingwen;
	}
	
	/**
	 * 解密微信推送的加密内容
	 * @param orginXml  第三方收到公众号平台发送的消息
	 * @param msgSignature url传递的加密签名key
	 * @param timeStamp 时间戳，可以自己生成，也可以用URL参数的timestamp
	 * @param nonce 随机串，可以自己生成，也可以用URL参数的nonce
	 * @return 解密内容xml
	 * @throws Exception
	 */
	public String decodeXml(String orginXml, String msgSignature, String timestamp, String nonce) throws Exception {
		if (!NEED_AES) return orginXml;

		// 第三方收到公众号平台发送的消息
		String mingwen = pc.decryptMsg(msgSignature, timestamp, nonce, orginXml);
		if(DEBUG) {
			System.out.println("解密后明文: " + mingwen);
		}
		return mingwen;
	}

	public static WechatHaoHelper getInstance() {
		if (null == wechatHaoHelper)
			wechatHaoHelper = new WechatHaoHelper();
		return wechatHaoHelper;
	}
	
	/**
	 * 处理文字消息
	 * @param xml
	 * @return
	 */
	public RecvMessage parseMessageXml(String xml) {
		RecvMessage message = wechat.msg().receive(xml);
		return message;
	}
	
	/**
	 * 返回模板消息
	 * @param openId 接收方openid
	 * @param templateId 模板id
	 * @param fields 模板参数
	 * @return
	 */
	public String replyXmlTemplate(String openId, String templateId, List<TemplateField> fields) {
		Long msgId = wechat.msg().sendTemplate(openId, templateId, fields);
		return msgId + "";
	}
	
	/**
	 * 返回模板消息
	 * @param openId 接收方openid
	 * @param templateId 模板id
	 * @param fields 模板参数
	 * @param link 模板跳转地址
	 * @return
	 */
	public String replyXmlTemplate(String openId, String templateId, List<TemplateField> fields, String link) {
		Long msgId = wechat.msg().sendTemplate(openId, templateId, fields, link);
		return msgId + "";
	}
	
	/**
	 * 返回用户详情(其中包含UnionId)
	 * @param openId
	 * @return
	 */
	public User getUserDetail(String openId) {
		 return wechat.user().getUser(openId);
	}
	
	/**
	 * 返回关注的用户列表（只包含openid）
	 * @return
	 */
	public UserList getUsers() {
		 return wechat.user().getUsers(null);
	}
	
	 
	
	/**
	 * 回复文字
	 * @param message 请求方信息
	 * @param content 回复内容
	 * @return
	 */
	public String replyXmlText(RecvMessage message, String content) {
		String result = wechat.msg().respText(message, content);
		return result;
	}
	
	/**
	 * 回复图文
	 * @param message 请求方信息
	 * @param articles 图文内容
	 * @return
	 */
	public String replyXmlArticles(RecvMessage message, List<Article> articles) {
		String result = null;
		//对话的时间推送 此处仅为文字 更多扩展见RecvMsg
		if (message instanceof RecvMenuEvent) {
			result = wechat.msg().respNews(message,articles);
		}
		return result;
	}
	
	/**
	 * 创建菜单
	 * @param menuItems
	 * @param cb
	 */
	public void createMenu(List<MenuItem> menuItems, Callback<Boolean> cb) {
		String jsonMenu = getBuildMenu(menuItems);
		if (null == cb) {
			wechat.menu().create(jsonMenu);
		}
		else {
			wechat.menu().create(jsonMenu,cb);
		}
	}
	
	/**
	 * 删除菜单
	 * @param menuItems
	 * @param cb
	 */
	public void deleteMenu(Callback<Boolean> cb) {
		if (null == cb) {
			wechat.menu().delete();
		}
		else {
			wechat.menu().delete(cb);
		}
	}
	/**
	 * check根项按钮
	 * @param menuItems
	 * @return true 是  false 否
	 */
	private boolean checkMenuItems(List<MenuItem> menuItems) {
		return null == menuItems || menuItems.size() == 0;
	}
	
	/**
	 * 构建菜单json
	 * @param menuItems
	 * @return
	 */
	private String getBuildMenu(List<MenuItem> menuItems) {
		if (menuItems.size() == 0) {
			return null;
		}
		MenuBuilder menuBuilder = MenuBuilder.newBuilder();
		for (MenuItem menuItem : menuItems) {
			if (checkMenuItems(menuItem.getMenuItems()) && menuItem.getType() != MenuItem.PARENT) {
				if (menuItem.getType() == MenuItem.VIEW) {
					menuBuilder.view(menuItem.getName(), menuItem.getKey());
				}
				else if (menuItem.getType() == MenuItem.CLICK) {
					menuBuilder.click(menuItem.getName(), menuItem.getKey());
				}
			}
			else {
				Menu more = menuBuilder.newParentMenu(menuItem.getName());
				 for (MenuItem childMenuItem : menuItem.getMenuItems()) {
					 if (childMenuItem.getType() == MenuItem.CLICK) {
						 menuBuilder.click(more, childMenuItem.getName(), childMenuItem.getKey());
					 }
					 else if (childMenuItem.getType() == MenuItem.VIEW) {
						 menuBuilder.view(more, childMenuItem.getName(), childMenuItem.getKey());
					 }
				}
				 menuBuilder.menu(more);
			}
		}
        String jsonMenu = menuBuilder.build();
        return jsonMenu;
	}
	
	public String getRedirectUrl(String url){
		return getRedirectUrl(url, false);
	}
	
    public String getRedirectUrl(String url, boolean quiet){

        return wechat.base().authUrl(url, quiet);
    }
    
    public boolean checkSignature(String timestamp, String nonce, String signature) {
        try {
          return SHA1.gen(APP_TOKEN, timestamp, nonce)
            .equals(signature);
        } catch (Exception e) {
          return false;
        }
      }

}
