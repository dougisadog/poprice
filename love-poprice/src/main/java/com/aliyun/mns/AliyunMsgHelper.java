package com.aliyun.mns;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.common.utils.ServiceSettings;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;

public class AliyunMsgHelper {
	
	private static AliyunMsgHelper aliyunMsgHelper;
	
	private MNSClient client;
	
	private AliyunMsgHelper() {
        CloudAccount account = new CloudAccount(
        		AliyunMsgSetting.getMNSAccessKeyId(),
        		AliyunMsgSetting.getMNSAccessKeySecret(),
        		AliyunMsgSetting.getMNSAccountEndpoint());
        client = account.getMNSClient();
	}
	
	public static AliyunMsgHelper getInstance() {
		if (null == aliyunMsgHelper)
			aliyunMsgHelper = new AliyunMsgHelper();
		return aliyunMsgHelper;
	}
	
    public TopicMessage sendMsg(BatchSmsAttributes batchSmsAttributes) {
        /**
         * Step 1. 获取主题引用
         */
        CloudTopic topic = client.getTopicRef("sms.topic-cn-hangzhou");
        /**
         * Step 2. 设置SMS消息体（必须）
         *
         * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
         */
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("sms-message");
        /**
         * Step 3. 生成SMS消息属性
         */
        MessageAttributes messageAttributes = new MessageAttributes();
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
        TopicMessage ret = null;
        try {
            /**
             * Step 4. 发布SMS消息
             */
            ret = topic.publishMessage(msg, messageAttributes);
            System.out.println("MessageId: " + ret.getMessageId());
            System.out.println("MessageMD5: " + ret.getMessageBodyMD5());
        } catch (ServiceException se) {
            System.out.println(se.getErrorCode() + se.getRequestId());
            System.out.println(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        client.close();
        return ret;
    }

}
